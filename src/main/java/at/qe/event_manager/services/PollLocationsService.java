package at.qe.event_manager.services;

import java.io.Serializable;
import java.util.Collection;
import at.qe.event_manager.model.PollLocations;
import at.qe.event_manager.repositories.PollLocationsRepository;
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
public class PollLocationsService implements Serializable {

    private static final long serialVersionUID = 1L;

    @Autowired
    private PollLocationsRepository pollLocationsRepository;

    /**
     * Returns a collection of all users.
     *
     * @return
     */
    public Collection<PollLocations> getAllPollLocations() {
        return pollLocationsRepository.findAll();
    }

    public PollLocations get(Integer pollId, Integer locationId) {
        return pollLocationsRepository.findFirstByIds(pollId, locationId);
    }

    /**
     * Loads a single user identified by its username.
     *
     * @param username the username to search for
     * @return the user with the given username
     */
    public PollLocations loadPollLocations(Integer pid, Integer lid) {
        return pollLocationsRepository.findFirstByIds(pid, lid);
    }

    /**
     * @param user the user to save
     * @return the updated user
     */
    public PollLocations savePollLocations(PollLocations pollLocations) {
        return pollLocationsRepository.save(pollLocations);
    }

    /**
     * @param user the user to save
     * @return the updated user
     */
    public PollLocations createPollLocations(PollLocations pollLocations) {
        return savePollLocations(pollLocations);
    }

    /**
     * Deletes the user.
     *
     * @param user the user to delete
     */
    public void deletePollLocations(PollLocations pollLocations) {
        pollLocationsRepository.delete(pollLocations);
    }
}
