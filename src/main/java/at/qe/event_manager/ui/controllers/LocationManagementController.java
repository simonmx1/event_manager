package at.qe.event_manager.ui.controllers;

import at.qe.event_manager.model.Location;
import at.qe.event_manager.model.OpeningTime;
import at.qe.event_manager.payload.request.LocationCreationRequest;
import at.qe.event_manager.payload.response.MessageResponse;
import at.qe.event_manager.services.LocationService;
import at.qe.event_manager.services.OpeningTimeService;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.primefaces.shaded.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for the user list view.
 */
@RestController
@RequestMapping("/api/location")
public class LocationManagementController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
    private LocationService locationService;
	
	@Autowired
	private OpeningTimeService openingTimeService;
	
    /**
     * Returns a list of all users.
     *
     * @return
     */

    @GetMapping("/getAll")
    public Collection<Location> getLocations() {
        return locationService.getAllLocations();
    }

    @GetMapping("/get")
    @ResponseBody
    public Location get(@RequestParam(name = "locationId") Integer locationId) {
        return locationService.loadLocationByLocationId(locationId);
    }

    @PostMapping("/edit")
    public ResponseEntity<MessageResponse> edit(@RequestBody LocationCreationRequest locationRequest) {
        Location location = locationService.loadLocationByLocationId(locationRequest.getLocationId());
        for(OpeningTime openingTime : location.getOpeningTimes()) {
        	openingTimeService.deleteOpeningTime(openingTime);
        }
        location.setOpeningTimes(locationRequest.getOpeningTimes());
        for(OpeningTime op : location.getOpeningTimes()) {
            op.setLocation(location);
        }
        if(locationService.saveLocation(location) == null) {
            return ResponseEntity.ok(new MessageResponse("Error: Location does not exist!"));
        } else {
            return ResponseEntity.ok(new MessageResponse("Location edited successfully!"));
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<MessageResponse> delete(@RequestBody String locationId) {
        locationService.deleteLocation(locationService.loadLocationByLocationId(new JSONObject(locationId).getInt("locationId")));
        return ResponseEntity.ok(new MessageResponse("Location deleted successfully!"));
    }

    @PostMapping("/create")
    public ResponseEntity<String> createLocation(@RequestBody LocationCreationRequest locationRequest) {
        Location location = new Location(locationRequest);
        for(OpeningTime op : location.getOpeningTimes()) {
            op.setLocation(location);
        }
        if (locationService.createLocation(location) == null) {
            return new ResponseEntity<>("Error While creating the Location!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Location created successfully!", HttpStatus.CREATED);
        }
    }

}
