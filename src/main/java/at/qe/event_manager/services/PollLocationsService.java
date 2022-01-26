package at.qe.event_manager.services;

import java.io.Serializable;
import java.util.Collection;
import at.qe.event_manager.model.PollLocations;
import at.qe.event_manager.repositories.PollLocationsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Service for accessing and manipulating PollLocations data.
 */
@Component
@Scope("application")
public class PollLocationsService implements Serializable {

    private static final long serialVersionUID = 1L;

    @Autowired
    private PollLocationsRepository pollLocationsRepository;

    /**
     * Loads all pollLocations from the database
     *
     * @return a Collection of all PollLocations
     */
    public Collection<PollLocations> getAllPollLocations() {
        return pollLocationsRepository.findAll();
    }

    /**
     * Loads a single pollLocations identified by the pollId and the locationId.
     *
     * @param pollId, locationId the pollId and locationId to search for
     * @return the PollLocations with the given pollId and locationId
     */
    public PollLocations loadPollLocation(Integer pollId, Integer locationId) {
        return pollLocationsRepository.findFirstByIds(pollId, locationId);
    }

    /**
     * Saves the given pollLocation.
     *
     * @param pollLocations the event to save
     * @return the saved PollLocations
     */
    public PollLocations savePollLocations(PollLocations pollLocations) {
        return pollLocationsRepository.save(pollLocations);
    }

    /**
     * Deletes the PollLocation.
     *
     * @param pollLocations the event to delete
     */
    public void deletePollLocations(PollLocations pollLocations) {
        pollLocationsRepository.delete(pollLocations);
    }
}
