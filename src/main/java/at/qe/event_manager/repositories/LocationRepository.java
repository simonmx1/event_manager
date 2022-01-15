package at.qe.event_manager.repositories;

import java.io.Serializable;
import at.qe.event_manager.model.Location;
import org.springframework.data.jpa.repository.Query;

/**
 * Repository for managing {@link User} entities.
 * <p>
 * This class is part of the skeleton project provided for students of the
 * courses "Software Architecture" and "Software Engineering" offered by the
 * University of Innsbruck.
 */

public interface LocationRepository extends AbstractRepository<Location, Integer>, Serializable {

    @Query("SELECT l FROM Location l WHERE :name = l.name")
    Location findFirstByName(String name);
}
