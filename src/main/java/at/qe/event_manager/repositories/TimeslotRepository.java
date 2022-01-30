package at.qe.event_manager.repositories;

import at.qe.event_manager.model.Timeslot;
import org.springframework.data.jpa.repository.Query;

import java.io.Serializable;

/**
 * This interface is part of the event manager project which was programmed during the
 * "PS Software Architecture" course in the winter semester 2021/2022 at the University of Innsbruck.
 * 
 * @author Matthias Komar
 * @author Manuel Reichegger
 * @author Simon Muscatello
 * @author Stefan Wagner
 * 
 * Repository for managing {@link Timeslot} entities.
 */

public interface TimeslotRepository extends AbstractRepository<Timeslot, Integer>, Serializable {

    @Query("SELECT t FROM Timeslot t WHERE :id = t.id")
    Timeslot findFirstById(Integer id);

}
