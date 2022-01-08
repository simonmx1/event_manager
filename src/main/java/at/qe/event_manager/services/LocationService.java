package at.qe.event_manager.services;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import at.qe.event_manager.model.Location;
import at.qe.event_manager.repositories.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
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
public class LocationService implements Serializable {

    @Autowired
    private LocationRepository locationRepository;

    /**
     * Returns a collection of all users.
     *
     * @return
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public Collection<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    /**
     * Loads a single user identified by its username.
     *
     * @param username the username to search for
     * @return the user with the given username
     */
    @PreAuthorize("hasAuthority('ADMIN') or principal.username eq #username")
    public Location loadLocation(String name) {
        return locationRepository.findFirstByName(name);
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
    @PreAuthorize("hasAuthority('ADMIN')")
    public Location saveLocation(Location location) {
        if (location.isNew()) {
            location.setCreateDate(new Date());
        }
        return locationRepository.save(location);
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
    public Location createLocation(Location location) {
        return saveLocation(location);
    }

    /**
     * Deletes the user.
     *
     * @param user the user to delete
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteUser(Location location) {
        locationRepository.delete(location);
        // :TODO: write some audit log stating who and when this user was permanently deleted.
    }

}
