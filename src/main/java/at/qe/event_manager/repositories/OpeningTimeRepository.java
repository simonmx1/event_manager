package at.qe.event_manager.repositories;

import java.io.Serializable;
import java.util.List;

import at.qe.event_manager.model.Location;
import org.springframework.data.jpa.repository.Query;
import at.qe.event_manager.model.OpeningTime;

/**
 * This interface is part of the event manager project which was programmed during the
 * "PS Software Architecture" course in the winter semester 2021/2022 at the University of Innsbruck.
 * 
 * @author Matthias Komar
 * @author Manuel Reichegger
 * @author Simon Muscatello
 * @author Stefan Wagner
 * 
 * Repository for managing {@link OpeningTime} entities.
 */

public interface OpeningTimeRepository extends AbstractRepository<OpeningTime, Integer>, Serializable {
	
	@Query("SELECT ot FROM OpeningTime ot WHERE :openingTimeId = ot.openingTimeId")
	OpeningTime findFirstByOpeningTimeId(Integer openingTimeId);

	List<OpeningTime> findAllByLocation(Location locationId);
}
