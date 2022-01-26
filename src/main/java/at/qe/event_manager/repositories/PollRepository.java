package at.qe.event_manager.repositories;

import java.io.Serializable;

import at.qe.event_manager.model.Event;
import at.qe.event_manager.model.Poll;
import at.qe.event_manager.model.User;
import org.springframework.data.jpa.repository.Query;

/**
 * Repository for managing {@link Poll} entities.
 */

public interface PollRepository extends AbstractRepository<Poll, Integer>, Serializable {

    @Query("SELECT p FROM Poll p WHERE :id = p.id")
    Poll findFirstById(Integer id);

    Poll findFirstByEventAndUser(Event event, User user);
}
