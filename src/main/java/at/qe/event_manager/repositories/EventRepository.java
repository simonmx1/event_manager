package at.qe.event_manager.repositories;

import java.io.Serializable;
import java.util.List;

import at.qe.event_manager.model.Event;
import at.qe.event_manager.model.User;
import org.springframework.data.jpa.repository.Query;

/**
 * Repository for managing {@link Event} entities.
 */

public interface EventRepository extends AbstractRepository<Event, Integer>, Serializable {

    @Query("SELECT e FROM Event e WHERE :id = e.id")
    Event findFirstByEventId(Integer id);

    List<Event> findAllByParticipants(User user);
}
