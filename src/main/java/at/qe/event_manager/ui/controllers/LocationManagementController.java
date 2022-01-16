package at.qe.event_manager.ui.controllers;

import at.qe.event_manager.model.Location;
import at.qe.event_manager.payload.response.MessageResponse;
import at.qe.event_manager.services.LocationService;
import java.io.Serializable;
import java.util.Collection;
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
    /*
        @GetMapping("/get")
    @ResponseBody
    public Location get(@RequestParam(name = "name") String name) {
        return locationService.loadLocationByLocationId(Integer.parseInt(name));
    }
    */

    @PostMapping("/edit")
    public ResponseEntity<?> edit(@RequestBody Location location) {
        if(locationService.saveLocation(location) == null) {
            return ResponseEntity.ok(new MessageResponse("Error: Location does not exist!"));
        } else {
            return ResponseEntity.ok(new MessageResponse("Location edited successfully!"));
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody String locationId) {
        Location location = locationService.loadLocationByLocationId(new JSONObject(locationId).getInt("locationId"));
        locationService.deleteLocation(location);
        return ResponseEntity.ok(new MessageResponse("Location deleted successfully!"));
    }

    /*
    @PostMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody String name) {
    	Integer id = Integer.parseInt(new JSONObject(name).getString("name"));
        locationService.deleteLocation(locationService.loadLocationByLocationId(id));
        return ResponseEntity.ok(new MessageResponse("Location deleted successfully!"));
    }
     */

    @PostMapping("/create")
    public ResponseEntity<?> createLocation(@RequestBody Location location) {
        if (locationService.createLocation(location) == null) {
            return new ResponseEntity<>("Error While creating the Location!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Location created successfully!", HttpStatus.CREATED);
        }
    }

}
