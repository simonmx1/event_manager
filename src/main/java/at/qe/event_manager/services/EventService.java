package at.qe.event_manager.services;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

import at.qe.event_manager.model.Event;
import at.qe.event_manager.model.Poll;
import at.qe.event_manager.model.Poll_Locations;
import at.qe.event_manager.model.Poll_Timeslots;
import at.qe.event_manager.model.User;
import at.qe.event_manager.repositories.EventRepository;
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
    				pollLocationsWinner.get(pollLocationsWinner.indexOf(pollLocation)).addPoints(pollLocation);;
    			}
    		}
    		for(Poll_Timeslots pollTimeslot : poll.getPoll_timeslots()) {
    			if(!pollTimeslotsWinner.contains(pollTimeslot)) {
    				pollTimeslotsWinner.add(pollTimeslot);
    			}
    			else {
    				pollTimeslotsWinner.get(pollTimeslotsWinner.indexOf(pollTimeslot)).addPoints(pollTimeslot);;
    			}
    		}
    	}
    	System.out.println(pollLocationsWinner);
    	System.out.println(pollTimeslotsWinner);
    	
    }
}
