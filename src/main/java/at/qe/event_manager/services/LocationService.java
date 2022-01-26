package at.qe.event_manager.services;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import at.qe.event_manager.model.Location;
import at.qe.event_manager.model.Tag;
import at.qe.event_manager.repositories.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Service for accessing and manipulating location data.
 */
@Component
@Scope("application")
public class LocationService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
    private LocationRepository locationRepository;
	
	@Autowired
	private EventService eventService;

    /**
     * Loads all location from the database
     *
     * @return Collection of all locations
     */
    public Collection<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    /**
     * Loads a single location identified by its id.
     *
     * @param locationId the locationId to search for
     * @return the location with the given id
     */
    public Location loadLocationByLocationId(Integer locationId) {
        return locationRepository.findFirstByLocationId(locationId);
    }

    /**
     * Saves the given location. This method will also set the location createDate for new
     * entities.
     *
     * @param location the location to save
     * @return the saved location
     */
    public Location saveLocation(Location location) {
        if (location.isNew()) {
            location.setCreateDate(new Date());
        }
        return locationRepository.save(location);
    }

    /**
     * Creates the given location.
     *
     * @param location the location to create
     * @return the created location
     */
    public Location createLocation(Location location) {
        return saveLocation(location);
    }

    /**
     * Deletes the location.
     *
     * @param location the location to delete
     */
    public void deleteLocation(Location location) {
    	eventService.cleanUpForLocationDeletion(location);
        locationRepository.delete(location);
    }

    /**
     * Delete policy for the Location, where the given Tag is set.
     * Removes the Tag from the List of Tags in the location.
     *
     * @param tag the tag to delete from locations
     */
    public void cleanUpForTagDeletion(Tag tag) {
    	// Delete Policy for Tag in Locations
    	for(Location location : getAllLocations()) {
    		location.getTags().remove(tag);
    	}
    }
}
