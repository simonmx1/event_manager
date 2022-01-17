package at.qe.event_manager.repositories;

import at.qe.event_manager.model.PollLocations;
import at.qe.event_manager.model.PollTimeslots;
import org.springframework.data.jpa.repository.Query;

import java.io.Serializable;

public interface PollTimeslotsRepository extends AbstractRepository<PollTimeslots, Integer>, Serializable {

    @Query("SELECT pt FROM PollTimeslots pt WHERE :tid = pt.timeslot.timeslotId AND :pid = pt.poll.pollId")
    PollTimeslots findFirstByIds(Integer pid, Integer tid);
}
