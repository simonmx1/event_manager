package at.qe.event_manager.repositories;

import at.qe.event_manager.model.Timeslot;
import org.springframework.data.jpa.repository.Query;

import java.io.Serializable;

/**
 * Repository for managing {@link Timeslot} entities.
 */

public interface TimeslotRepository extends AbstractRepository<Timeslot, Integer>, Serializable {

    @Query("SELECT t FROM Timeslot t WHERE :id = t.id")
    Timeslot findFirstById(Integer id);

}
