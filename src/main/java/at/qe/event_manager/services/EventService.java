package at.qe.event_manager.services;

import java.io.Serializable;
import java.util.*;

import at.qe.event_manager.model.*;
import at.qe.event_manager.repositories.EventRepository;
import at.qe.event_manager.repositories.PollRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Service for accessing and manipulating user data.
 * <p>
 * This class is part of the skeleton project provided for students of the
 * courses "Software Architecture" and "Software Engineering" offered by the
 * University of Innsbruck.
 */
@Component
@Scope("application")
public class EventService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Autowired
    private EventRepository eventRepository;
	
    @Autowired
    private PollRepository pollRepository;
    
    @Autowired
    private PollService pollService;
    
    /**
     * Returns a collection of all users.
     *
     * @return
     */
    public Collection<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    /**
     * Loads a single user identified by its username.
     *
     * @param username the username to search for
     * @return the user with the given username
     */
    public Event loadEvent(Integer id) {
        return eventRepository.findFirstByEventId(id);
    }

    public Event saveEvent(Event event) {
        if (event.isNew()) {
            event.setCreateDate(new Date());
        }
        return eventRepository.save(event);
    }

    public Event createEvent(Event event) {
        return saveEvent(event);
    }

    /**
     * Deletes the user.
     *
     * @param user the user to delete
     */
    public void deleteEvent(Event event) {
    	if(!event.getPolls().isEmpty()) {
    		event.getPolls().forEach(p -> pollService.deletePoll(p));
    	}
    	System.out.println(event.getName());
        eventRepository.delete(event);
    }
    
    public void cleanUpForParticipantDeletion(User user) {
    	// Delete Policy for User in Events
    	for(Event event : getAllEvents()) {
    		if(event.getCreator().getUsername().compareTo(user.getUsername()) == 0) {
    			// :TODO: delete Event or somehow cancel event
    		}
        	Set<User> participants = event.getParticipants();
        	if(participants.contains(user)) {
        		participants.remove(user);
        		if(participants.size() < 2) {
        			// :TODO: Event can't be held
        			// :TODO: Send Mail to last participant
        		}
        	}
        }
    	pollService.cleanUpForParticipantDeletion(user);
    }
    
    public void cleanUpForLocationDeletion(Location location) {
//    	for(Event event : getAllEvents()) {
//    		if(event.getLocation().compareTo(location) == 0) {
    			// :TODO: delete Event or somehow cancel event
//    		}
//    	}
    	pollService.cleanUpForLocationDeletion(location);
    }
    
    public void evaluatePolls(Event event) {
    	Set<Poll> polls = event.getPolls();
    	ArrayList<PollLocations> pollLocationsWinner = new ArrayList<>();
    	ArrayList<PollTimeslots> pollTimeslotsWinner = new ArrayList<>();
    	
    	for(Poll poll : polls) {
    		for(PollLocations pollLocation : poll.getPollLocations()) {
    			if(!pollLocationsWinner.contains(pollLocation)) {
    				pollLocationsWinner.add(pollLocation);
    			}
    			else {
    				pollLocationsWinner.get(pollLocationsWinner.indexOf(pollLocation)).addPoints(pollLocation);
    			}
    		}
    		for(PollTimeslots pollTimeslot : poll.getPollTimeslots()) {
    			if(!pollTimeslotsWinner.contains(pollTimeslot)) {
                    pollTimeslotsWinner.add(pollTimeslot);
    			}
                else if (pollTimeslotsWinner.get(pollTimeslotsWinner.indexOf(pollTimeslot)).getPoints() == 0 || pollTimeslot.getPoints() == 0){
                    pollTimeslotsWinner.get(pollTimeslotsWinner.indexOf(pollTimeslot)).setPoints(0);
                }
    			else {
    				pollTimeslotsWinner.get(pollTimeslotsWinner.indexOf(pollTimeslot)).addPoints(pollTimeslot);
    			}
    		}
    	}
        Comparator<PollLocations> pollLocationsComparator = new PollLocationsComparator();
        Comparator<PollTimeslots> pollTimeslotsComparator = new PollTimeslotsComparator();
        pollLocationsWinner.sort(pollLocationsComparator);
        pollTimeslotsWinner.sort(pollTimeslotsComparator);
        if (pollTimeslotsWinner.get(0).getPoints() == 0) {
            // :TODO: sent email to participants, event is evaluated but will not be held
            event.setEvaluated(true);
        } else {
            if (pollLocationsWinner.get(0).getPoints().intValue() == pollLocationsWinner.get(1).getPoints().intValue()) {
                ArrayList<PollLocations> temp = new ArrayList<>();
                for (PollLocations poll_location : pollLocationsWinner) {
                    if (poll_location.getPoints().intValue() == pollLocationsWinner.get(0).getPoints().intValue()) {
                        temp.add(poll_location);
                    }
                }
                if (event.isCreatorIsPreferred()) {
                    ArrayList<PollLocations> tempCreator = new ArrayList<>(pollRepository.findFirstByEventAndUser(event, event.getCreator()).getPollLocations());
                    for (PollLocations pl : temp) {
                        for (PollLocations plCreator : tempCreator) {
                            if (pl.equals(plCreator)) {
                                temp.set(temp.indexOf(pl), plCreator);
                            }
                        }
                    }
                    temp.sort(pollLocationsComparator);
                    event.setLocation(temp.get(0).getLocation());
                } else {
                    Random random = new Random();
                    int min = 0;
                    int max = temp.size() - 1;
                    int index = random.nextInt(max + min) + min;
                    event.setLocation(temp.get(index).getLocation());
                }
            } else {
                event.setLocation(pollLocationsWinner.get(0).getLocation());
            }
            if (pollTimeslotsWinner.get(0).getPoints().intValue() == pollTimeslotsWinner.get(1).getPoints().intValue()) {
                ArrayList<PollTimeslots> temp = new ArrayList<>();
                for (PollTimeslots poll_timeslot : pollTimeslotsWinner) {
                    if (poll_timeslot.getPoints().intValue() == pollTimeslotsWinner.get(0).getPoints().intValue()) {
                        temp.add(poll_timeslot);
                    }
                }
                if (event.isCreatorIsPreferred()) {
                    ArrayList<PollTimeslots> tempCreator = new ArrayList<>(pollRepository.findFirstByEventAndUser(event, event.getCreator()).getPollTimeslots());
                    for (PollTimeslots pt : temp) {
                        for (PollTimeslots ptCreator : tempCreator) {
                            if (pt.equals(ptCreator)) {
                                temp.set(temp.indexOf(pt), ptCreator);
                            }
                        }
                    }
                    temp.sort(pollTimeslotsComparator);
                    event.setTimeslot(temp.get(0).getTimeslot());
                } else {
                    Random random = new Random();
                    int min = 0;
                    int max = temp.size() - 1;
                    int index = random.nextInt(max + min) + min;
                    event.setTimeslot(temp.get(index).getTimeslot());
                }
            } else {
                event.setTimeslot(pollTimeslotsWinner.get(0).getTimeslot());
            }
            event.setEvaluated(true);
            event.getParticipants().forEach(user -> MailService.sendEventEvaluationMessage(user, event));
        }
        eventRepository.save(event);
    }
}
