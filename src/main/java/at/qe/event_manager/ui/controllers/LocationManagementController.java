package at.qe.event_manager.ui.controllers;

import at.qe.event_manager.model.Location;
import at.qe.event_manager.services.LocationService;

import java.io.Serializable;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Controller for the user list view.
 * <p>
 * This class is part of the skeleton project provided for students of the
 * courses "Software Architecture" and "Software Engineering" offered by the
 * University of Innsbruck.
 */
@Component
@Scope("view")
public class LocationManagementController implements Serializable {

    @Autowired
    private LocationService locationService;

    /**
     * Returns a list of all users.
     *
     * @return
     */
    public Collection<Location> getLocations() {
        return locationService.getAllLocations();
    }

}
