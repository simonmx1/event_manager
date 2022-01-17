package at.qe.event_manager.services;

import at.qe.event_manager.model.Timeslot;
import at.qe.event_manager.repositories.TimeslotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

@Component
@Scope("application")
public class TimeslotService implements Serializable {

	private static final long serialVersionUID = 1L;

    @Autowired
    private TimeslotRepository timeslotRepository;

    public Collection<Timeslot> getAllTimeslots() {
        return timeslotRepository.findAll();
    }

    public Timeslot loadTimeslotById(Integer id) {
        return timeslotRepository.findFirstById(id);
    }

    public Timeslot saveTimeslot(Timeslot timeslot) {
        if (timeslot.isNew()) {
            timeslot.setCreateDate(new Date());
        }
        return timeslotRepository.save(timeslot);
    }

    public Timeslot createTimeslot(Timeslot timeslot) {
        return saveTimeslot(timeslot);
    }

    public void deleteTimeslot(Timeslot timeslot) {
        timeslotRepository.delete(timeslot);
    }

}
