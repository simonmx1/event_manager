package at.qe.event_manager.repositories;

import java.io.Serializable;
import at.qe.event_manager.model.Location;
import org.springframework.data.jpa.repository.Query;

/**
 * Repository for managing {@link Location} entities.
 */

public interface LocationRepository extends AbstractRepository<Location, Integer>, Serializable {

    @Query("SELECT l FROM Location l WHERE :locationId = l.locationId")
    Location findFirstByLocationId(Integer locationId);
}
