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
        return eventRepository.findById(id);
    }

    /**
     * Saves the user. This method will also set {@link User#createDate} for new
     * entities or {@link User#updateDate} for updated entities. The user
     * requesting this operation will also be stored as {@link User#createDate}
     * or {@link User#updateUser} respectively.
     *
     * @param user the user to save
     * @return the updated user
     */
    public Event saveEvent(Event event) {
        if (event.isNew()) {
            event.setCreateDate(new Date());
        }
        return eventRepository.save(event);
    }

    /**
     * Saves the user. This method will also set {@link User#createDate} for new
     * entities or {@link User#updateDate} for updated entities. The user
     * requesting this operation will also be stored as {@link User#createDate}
     * or {@link User#updateUser} respectively.
     *
     * @param user the user to save
     * @return the updated user
     */
    public Event createEvent(Event event) {
        return saveEvent(event);
    }

    /**
     * Deletes the user.
     *
     * @param user the user to delete
     */
    public void deleteEvent(Event event) {
        eventRepository.delete(event);
        // :TODO: write some audit log stating who and when this user was permanently deleted.
    }
    
    public void evaluatePolls(Event event) {
    	Set<Poll> polls = event.getPolls();
    	ArrayList<Poll_Locations> pollLocationsWinner = new ArrayList<>();
    	ArrayList<Poll_Timeslots> pollTimeslotsWinner = new ArrayList<>();
    	
    	for(Poll poll : polls) {
    		for(Poll_Locations pollLocation : poll.getPoll_locations()) {
    			if(!pollLocationsWinner.contains(pollLocation)) {
    				pollLocationsWinner.add(pollLocation);
    			}
    			else {
    				pollLocationsWinner.get(pollLocationsWinner.indexOf(pollLocation)).addPoints(pollLocation);
    			}
    		}
    		for(Poll_Timeslots pollTimeslot : poll.getPoll_timeslots()) {
    			if(!pollTimeslotsWinner.contains(pollTimeslot)) {
                    pollTimeslotsWinner.add(pollTimeslot);
    			}
                else if (pollTimeslotsWinner.get(pollTimeslotsWinner.indexOf(pollTimeslot)).getPoints() == 0 || pollTimeslot.getPoints() == 0){
                    pollTimeslotsWinner.get(pollTimeslotsWinner.indexOf(pollTimeslot)).setPoints(0);
                }
    			else {
    				pollTimeslotsWinner.get(pollTimeslotsWinner.indexOf(pollTimeslot)).addPoints(pollTimeslot);;
    			}
    		}
    	}
        Comparator<Poll_Locations> poll_LocationsComparator = new Poll_LocationsComparator();
        Comparator<Poll_Timeslots> poll_timeslotsComparator = new Poll_TimsSlotsComparator();
        Collections.sort(pollLocationsWinner, poll_LocationsComparator);
        Collections.sort(pollTimeslotsWinner, poll_timeslotsComparator);
        if (pollTimeslotsWinner.get(0).getPoints() == 0) {
            // :TODO: sent email to participants, event is evaluated but will not be held
            event.setEvaluated(true);
        } else {
            if (pollLocationsWinner.get(0).getPoints().intValue() == pollLocationsWinner.get(1).getPoints().intValue()) {
                ArrayList<Poll_Locations> temp = new ArrayList<>();
                for (Poll_Locations poll_location : pollLocationsWinner) {
                    if (poll_location.getPoints().intValue() == pollLocationsWinner.get(0).getPoints().intValue()) {
                        temp.add(poll_location);
                    }
                }
                if (event.isCreatorIsPreferred()) {
                    ArrayList<Poll_Locations> tempCreator = new ArrayList<>(pollRepository.findFirstByUserUsername(event.getCreator().getUsername()).getPoll_locations());
                    for (Poll_Locations pl : temp) {
                        for (Poll_Locations plCreator : tempCreator) {
                            if (pl.equals(plCreator)) {
                                temp.set(temp.indexOf(pl), plCreator);
                            }
                        }
                    }
                    temp.sort(poll_LocationsComparator);
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
                ArrayList<Poll_Timeslots> temp = new ArrayList<>();
                for (Poll_Timeslots poll_timeslot : pollTimeslotsWinner) {
                    if (poll_timeslot.getPoints().intValue() == pollTimeslotsWinner.get(0).getPoints().intValue()) {
                        temp.add(poll_timeslot);
                    }
                }
                if (event.isCreatorIsPreferred()) {
                    ArrayList<Poll_Timeslots> tempCreator = new ArrayList<>(pollRepository.findFirstByUserUsername(event.getCreator().getUsername()).getPoll_timeslots());
                    for (Poll_Timeslots pt : temp) {
                        for (Poll_Timeslots ptCreator : tempCreator) {
                            if (pt.equals(ptCreator)) {
                                temp.set(temp.indexOf(pt), ptCreator);
                            }
                        }
                    }
                    temp.sort(poll_timeslotsComparator);
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
            // :TODO: sent email to participants, event is evaluated
            event.setEvaluated(true);
        }
        eventRepository.save(event);
    }
}
