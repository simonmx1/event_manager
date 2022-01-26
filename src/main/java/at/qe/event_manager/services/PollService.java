package at.qe.event_manager.services;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import at.qe.event_manager.model.*;
import at.qe.event_manager.repositories.PollRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Service for accessing and manipulating poll data.
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
     * Loads all polls from the database
     *
     * @return a Collection of all polls
     */
    public Collection<Poll> getAllPolls() {
        return pollRepository.findAll();
    }

    /**
     * Loads a single poll identified by its id.
     *
     * @param id the pollId to search for
     * @return the poll with the given id
     */
    public Poll loadPollByPollId(Integer id) {
        return pollRepository.findById(id);
    }

    /**
     * Loads a single poll identified by its event and user.
     *
     * @param event, user the event and user to search for
     * @return the poll with the given event and user
     */
    public Poll loadPollByEventAndUser(Event event, User user) {
        return pollRepository.findFirstByEventAndUser(event, user);
    }

    /**
     * Saves the given poll. This method will also set the event createDate for new
     * entities.
     *
     * @param poll the event to save
     * @return the saved poll
     */
    public Poll savePoll(Poll poll) {
        if (poll.isNew()) {
            poll.setCreateDate(new Date());
        }
        return pollRepository.save(poll);
    }

    /**
     * Deletes the poll.
     *
     * @param poll the event to delete
     */
    public void deletePoll(Poll poll) {
    	poll.getPollLocations().forEach(pl -> pollLocationsService.deletePollLocations(pl));
    	poll.getPollTimeslots().forEach(pt -> pollTimeslotsService.deletePollTimeslots(pt));
        pollRepository.delete(poll);
    }

    /**
     * Delete policy for the Polls, where the given user is owner.
     * Deletes the users Polls.
     *
     * @param user the user of the Poll
     */
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
}
