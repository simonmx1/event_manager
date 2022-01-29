package at.qe.event_manager.repositories;

import java.io.Serializable;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import at.qe.event_manager.model.User;
import at.qe.event_manager.model.UserRole;

/**
 * This interface is part of the event manager project which was programmed during the
 * "PS Software Architecture" course in the winter semester 2021/2022 at the University of Innsbruck.
 * 
 * @author Matthias Komar
 * @author Manuel Reichegger
 * @author Simon Muscatello
 * @author Stefan Wagner
 * 
 * Repository for managing {@link User} entities.
 */
public interface UserRepository extends AbstractRepository<User, String>, Serializable {

    @Query("SELECT u FROM User u WHERE :username = u.username")
    User findFirstByUsername(String username);

    List<User> findByUsernameContaining(String username);

    @Query("SELECT u FROM User u WHERE CONCAT(u.firstName, ' ', u.lastName) = :wholeName")
    List<User> findByWholeNameConcat(@Param("wholeName") String wholeName);

    @Query("SELECT u FROM User u WHERE :role = u.role")
    List<User> findByRole(@Param("role") UserRole role);

}
