package at.qe.event_manager.repositories;

import at.qe.event_manager.model.PollTimeslots;
import org.springframework.data.jpa.repository.Query;

import java.io.Serializable;

/**
 * This interface is part of the event manager project which was programmed during the
 * "PS Software Architecture" course in the winter semester 2021/2022 at the University of Innsbruck.
 * 
 * @author Matthias Komar
 * @author Manuel Reichegger
 * @author Simon Muscatello
 * @author Stefan Wagner
 * 
 * Repository for managing {@link PollTimeslots} entities.
 */

public interface PollTimeslotsRepository extends AbstractRepository<PollTimeslots, Integer>, Serializable {

    @Query("SELECT pt FROM PollTimeslots pt WHERE :tid = pt.timeslot.timeslotId AND :pid = pt.poll.pollId")
    PollTimeslots findFirstByIds(Integer pid, Integer tid);
}
