package at.qe.skeleton.repositories;

import at.qe.skeleton.model.User;
import at.qe.skeleton.model.UserRole;

import java.io.Serializable;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository for managing {@link User} entities.
 *
 * This class is part of the skeleton project provided for students of the
 * courses "Software Architecture" and "Software Engineering" offered by the
 * University of Innsbruck.
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
