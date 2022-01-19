package at.qe.event_manager.services;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

import at.qe.event_manager.model.*;
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
public class PollService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Autowired
    private PollRepository pollRepository;
	
	@Autowired
    private PollLocationsService pollLocationsService;
    
    @Autowired
    private PollTimeslotsService pollTimeslotsService;

    /**
     * Returns a collection of all users.
     *
     * @return
     */
    public Collection<Poll> getAllPolls() {
        return pollRepository.findAll();
    }

    /**
     * Loads a single user identified by its username.
     *
     * @param username the username to search for
     * @return the user with the given username
     */
    public Poll loadPoll(Integer id) {
        return pollRepository.findById(id);
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
    public Poll savePoll(Poll poll) {
        if (poll.isNew()) {
            poll.setCreateDate(new Date());
        }
        return pollRepository.save(poll);
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
    public Poll createPoll(Poll poll) {
        return savePoll(poll);
    }

    /**
     * Deletes the user.
     *
     * @param user the user to delete
     */
    public void deletePoll(Poll poll) {
        pollRepository.delete(poll);
    }
    
    public void cleanUpForParticipantDeletion(User user) {
    	// Delete Policy for User in Polls
    	for(Poll poll : getAllPolls()) {
    		if(poll.getUser().getUsername().compareTo(user.getUsername()) == 0) {
    			poll.getPollLocations().forEach(pl -> pollLocationsService.deletePollLocations(pl));
    			poll.getPollTimeslots().forEach(pt -> pollTimeslotsService.deletePollTimeslots(pt));
    			deletePoll(poll);
    		}
    	}
    }
    
    public void cleanUpForLocationDeletion(Location location) {
    	// Delete Policy for Location in Polls
    	for(Poll poll : getAllPolls()) {
    		Set<PollLocations> pollLocations = poll.getPollLocations();
    		for(PollLocations pollLocation : pollLocations) {
    			if(pollLocation.getLocation().compareTo(location) == 0) {
    				pollLocationsService.deletePollLocations(pollLocation);
    			}
    		}
    		if (pollLocations.isEmpty()) {
    			// :TODO: Event can't be held -> delete or somehow cancel event
    		}
    	}
    }

    public Poll loadPollByEventIdAndUsername(Event event, User user) {
        return pollRepository.findFirstByEventAndUser(event, user);
    }
}
