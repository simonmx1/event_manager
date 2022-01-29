package at.qe.event_manager.repositories;

import java.io.Serializable;
import java.util.List;

import at.qe.event_manager.model.Event;
import at.qe.event_manager.model.User;
import org.springframework.data.jpa.repository.Query;

/**
 * This interface is part of the event manager project which was programmed during the
 * "PS Software Architecture" course in the winter semester 2021/2022 at the University of Innsbruck.
 * 
 * @author Matthias Komar
 * @author Manuel Reichegger
 * @author Simon Muscatello
 * @author Stefan Wagner
 * 
 * Repository for managing {@link Event} entities.
 */

public interface EventRepository extends AbstractRepository<Event, Integer>, Serializable {

    @Query("SELECT e FROM Event e WHERE :id = e.id")
    Event findFirstByEventId(Integer id);

    List<Event> findAllByParticipants(User user);
}
