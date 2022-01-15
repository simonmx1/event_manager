package at.qe.event_manager.repositories;

import java.io.Serializable;
import at.qe.event_manager.model.Poll;

import org.springframework.data.jpa.repository.Query;

/**
 * Repository for managing {@link User} entities.
 * <p>
 * This class is part of the skeleton project provided for students of the
 * courses "Software Architecture" and "Software Engineering" offered by the
 * University of Innsbruck.
 */

public interface PollRepository extends AbstractRepository<Poll, Integer>, Serializable {

    @Query("SELECT p FROM Poll p WHERE :id = p.id")
    Poll findFirstById(Integer id);

    @Query("SELECT p FROM Poll p WHERE p.user_username = :user_username")
    Poll findFirstByUsername(String user_username);
}
