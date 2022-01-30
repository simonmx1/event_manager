package at.qe.event_manager.services;

import at.qe.event_manager.model.Timeslot;
import at.qe.event_manager.repositories.TimeslotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.io.Serializable;
import java.util.Date;

/**
 * This class is part of the event manager project which was programmed during the
 * "PS Software Architecture" course in the winter semester 2021/2022 at the University of Innsbruck.
 * 
 * @author Matthias Komar
 * @author Manuel Reichegger
 * @author Simon Muscatello
 * @author Stefan Wagner
 * 
 * Service for accessing and manipulating timeslot data.
 */
@Component
@Scope("application")
public class TimeslotService implements Serializable {

	private static final long serialVersionUID = 1L;

    @Autowired
    private TimeslotRepository timeslotRepository;
    
    /**
     * Loads a single timeslot identified by its id.
     *
     * @param timeslotId the timeslotId to search for
     * @return the timeslot with the given id
     */
    public Timeslot loadTimeslotByLocationId(Integer timeslotId) {
        return timeslotRepository.findFirstById(timeslotId);
    }
    
    /**
     * Saves the given Timeslot. This method will also set the event createDate for new
     * entities.
     *
     * @param timeslot the timeslot to save
     * @return the saved timeslot
     */
    public Timeslot saveTimeslot(Timeslot timeslot) {
        if (timeslot.isNew()) {
            timeslot.setCreateDate(new Date());
        }
        return timeslotRepository.save(timeslot);
    }
}
