package at.qe.event_manager.ui.controllers;

import at.qe.event_manager.model.Location;
import at.qe.event_manager.model.User;
import at.qe.event_manager.payload.response.MessageResponse;
import at.qe.event_manager.services.LocationService;

import java.io.Serializable;
import java.util.Collection;

import org.primefaces.shaded.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for the user list view.
 */
@RestController
@RequestMapping("/api/location")
public class LocationManagementController implements Serializable {

    @Autowired
    private LocationService locationService;

    /**
     * Returns a list of all users.
     *
     * @return
     */

    @GetMapping("/getAll")
    public Collection<Location> getLocations() {
        System.out.println(locationService.getAllLocations());
        return locationService.getAllLocations();
    }

    @GetMapping("/get")
    @ResponseBody
    public Location get(@RequestParam(name = "name") String name) {
        Location l = locationService.loadLocationByLocationName(name);
        return l;
    }

    @PostMapping("/edit")
    public ResponseEntity<?> edit(@RequestBody Location location) {
        if(locationService.saveLocation(location) == null) {
            return ResponseEntity.ok(new MessageResponse("Error: Location does not exist!"));
        } else {
            return ResponseEntity.ok(new MessageResponse("Location edited successfully!"));
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody String name) {
        locationService.deleteLocation(locationService.loadLocationByLocationName(new JSONObject(name).getString("name")));
        return ResponseEntity.ok(new MessageResponse("Location deleted successfully!"));
    }

}
