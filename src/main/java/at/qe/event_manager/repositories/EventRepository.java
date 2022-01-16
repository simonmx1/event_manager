package at.qe.event_manager.repositories;

import java.io.Serializable;
import at.qe.event_manager.model.Event;
import org.springframework.data.jpa.repository.Query;

/**
 * Repository for managing {@link User} entities.
 * <p>
 * This class is part of the skeleton project provided for students of the
 * courses "Software Architecture" and "Software Engineering" offered by the
 * University of Innsbruck.
 */

public interface EventRepository extends AbstractRepository<Event, Integer>, Serializable {

    @Query("SELECT e FROM Event e WHERE :id = e.id")
    Event findFirstById(Integer id);
}
