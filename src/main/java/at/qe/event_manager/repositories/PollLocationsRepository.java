package at.qe.event_manager.repositories;

import at.qe.event_manager.model.PollLocations;
import org.springframework.data.jpa.repository.Query;

import java.io.Serializable;

public interface PollLocationsRepository extends AbstractRepository<PollLocations, Integer>, Serializable {

    @Query("SELECT pl FROM PollLocations pl WHERE :lid = pl.location.locationId AND :pid = pl.poll.pollId")
    PollLocations findFirstByIds(Integer pid, Integer lid);

}
