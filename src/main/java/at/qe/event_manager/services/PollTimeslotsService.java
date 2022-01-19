package at.qe.event_manager.services;

import java.io.Serializable;
import java.util.Collection;
import at.qe.event_manager.model.PollTimeslots;
import at.qe.event_manager.repositories.PollTimeslotsRepository;
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
public class PollTimeslotsService implements Serializable {

    private static final long serialVersionUID = 1L;

    @Autowired
    private PollTimeslotsRepository pollTimeslotsRepository;

    /**
     * Returns a collection of all users.
     *
     * @return
     */
    public Collection<PollTimeslots> getAllPollTimeslots() {
        return pollTimeslotsRepository.findAll();
    }

    /**
     * Loads a single user identified by its username.
     *
     * @param username the username to search for
     * @return the user with the given username
     */
    public PollTimeslots loadPollTimeslots(Integer pid, Integer tid) {
        return pollTimeslotsRepository.findFirstByIds(pid, tid);
    }

    /**
     * @param user the user to save
     * @return the updated user
     */
    public PollTimeslots savePollTimeslots(PollTimeslots pollTimeslots) {
        return pollTimeslotsRepository.save(pollTimeslots);
    }

    /**
     * @param user the user to save
     * @return the updated user
     */
    public PollTimeslots createPollTimeslots(PollTimeslots pollTimeslots) {
        return savePollTimeslots(pollTimeslots);
    }

    /**
     * Deletes the user.
     *
     * @param user the user to delete
     */
    public void deletePollTimeslots(PollTimeslots pollTimeslots) {
        pollTimeslotsRepository.delete(pollTimeslots);
    }
}
