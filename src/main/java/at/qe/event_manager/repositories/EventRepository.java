package at.qe.event_manager.repositories;

import java.io.Serializable;
import java.util.List;

import at.qe.event_manager.model.Event;
import at.qe.event_manager.model.Location;
import org.springframework.data.jpa.repository.Query;

/**
 * Repository for managing {@link User} entities.
 * <p>
 * This class is part of the skeleton project provided for students of the
 * courses "Software Architecture" and "Software Engineering" offered by the
 * University of Innsbruck.
 */

public interface EventRepository extends AbstractRepository<Event, Integer>, Serializable {

    @Query("SELECT e FROM Event e WHERE :name = e.name")
    Event findFirstByName(String name);
}
