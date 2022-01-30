package at.qe.event_manager.services;

import java.io.Serializable;
import java.util.Collection;
import at.qe.event_manager.model.PollTimeslots;
import at.qe.event_manager.repositories.PollTimeslotsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * This class is part of the event manager project which was programmed during the
 * "PS Software Architecture" course in the winter semester 2021/2022 at the University of Innsbruck.
 * 
 * @author Matthias Komar
 * @author Manuel Reichegger
 * @author Simon Muscatello
 * @author Stefan Wagner
 * 
 * Service for accessing and manipulating PollTimeslots data.
 */
@Component
@Scope("application")
public class PollTimeslotsService implements Serializable {

    private static final long serialVersionUID = 1L;

    @Autowired
    private PollTimeslotsRepository pollTimeslotsRepository;

    /**
     * Loads all pollTimeslots from the database
     *
     * @return a Collection of all PollTimeslots
     */
    public Collection<PollTimeslots> getAllPollTimeslots() {
        return pollTimeslotsRepository.findAll();
    }

    public PollTimeslots loadPollTimeslot(Integer pollId, Integer timeslotId) {
        return pollTimeslotsRepository.findFirstByIds(pollId, timeslotId);
    }

    /**
     * Saves the given pollTimeslot.
     *
     * @param pollTimeslots the event to save
     * @return the saved PollTimeslots
     */
    public PollTimeslots savePollTimeslots(PollTimeslots pollTimeslots) {
        return pollTimeslotsRepository.save(pollTimeslots);
    }

    /**
     * Deletes the PollTimeslots.
     *
     * @param pollTimeslots the event to delete
     */
    public void deletePollTimeslots(PollTimeslots pollTimeslots) {
        pollTimeslotsRepository.delete(pollTimeslots);
    }
}
