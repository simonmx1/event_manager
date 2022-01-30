package at.qe.event_manager.repositories;

import at.qe.event_manager.model.PollLocations;
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
 * Repository for managing {@link PollLocations} entities.
 */

public interface PollLocationsRepository extends AbstractRepository<PollLocations, Integer>, Serializable {

    @Query("SELECT pl FROM PollLocations pl WHERE :lid = pl.location.locationId AND :pid = pl.poll.pollId")
    PollLocations findFirstByIds(Integer pid, Integer lid);

}
