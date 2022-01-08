package at.qe.event_manager.services;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import at.qe.event_manager.model.Event;
import at.qe.event_manager.model.User;
import at.qe.event_manager.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Service for accessing and manipulating user data.
 * <p>
 * This class is part of the skeleton project provided for students of the
 * courses "Software Architecture" and "Software Engineering" offered by the
 * University of Innsbruck.
 */
@Component
@Scope("application")
public class EventService implements Serializable {

    @Autowired
    private EventRepository eventRepository;

    /**
     * Returns a collection of all users.
     *
     * @return
     */
    public Collection<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    /**
     * Loads a single user identified by its username.
     *
     * @param username the username to search for
     * @return the user with the given username
     */
    public Event loadEvent(String name) {
        return eventRepository.findFirstByName(name);
    }

    /**
     * Saves the user. This method will also set {@link User#createDate} for new
     * entities or {@link User#updateDate} for updated entities. The user
     * requesting this operation will also be stored as {@link User#createDate}
     * or {@link User#updateUser} respectively.
     *
     * @param user the user to save
     * @return the updated user
     */
    public Event saveEvent(Event event) {
        if (event.isNew()) {
            event.setCreateDate(new Date());
        }
        return eventRepository.save(event);
    }

    /**
     * Saves the user. This method will also set {@link User#createDate} for new
     * entities or {@link User#updateDate} for updated entities. The user
     * requesting this operation will also be stored as {@link User#createDate}
     * or {@link User#updateUser} respectively.
     *
     * @param user the user to save
     * @return the updated user
     */
    public Event createEvent(Event event) {
        return saveEvent(event);
    }

    /**
     * Deletes the user.
     *
     * @param user the user to delete
     */
    public void deleteEvent(Event event) {
        eventRepository.delete(event);
        // :TODO: write some audit log stating who and when this user was permanently deleted.
    }

    public Event loadeventByeventName(String name)  {
        return eventRepository.findFirstByName(name);
    }
}
