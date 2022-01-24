package at.qe.event_manager.repositories;

import java.io.Serializable;
import org.springframework.data.jpa.repository.Query;
import at.qe.event_manager.model.OpeningTime;

public interface OpeningTimeRepository extends AbstractRepository<OpeningTime, Integer>, Serializable {
	
	@Query("SELECT ot FROM OpeningTime ot WHERE :openingTimeId = ot.openingTimeId")
	OpeningTime findFirstByOpeningTimeId(Integer openingTimeId);
}
