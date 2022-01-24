package at.qe.event_manager.tests;

import at.qe.event_manager.model.Location;
import at.qe.event_manager.services.LocationService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Some very basic tests for {@link LocationService}.
 *
 * This class is part of the event_manager project.
 */
@SpringBootTest
@WebAppConfiguration
public class LocationServiceTest {

    @Autowired
    LocationService locationService;

    @Test
    @WithMockUser(username = "user1", authorities = {"ROLE_LOCATION_MANAGER"})
    public void testInitLocationData() {
        assertEquals(5,locationService.getAllLocations().size(), "Insufficient amount of locations initialized for test data source");
        for(Location location : locationService.getAllLocations()) {
            assertNotNull(location.getName(), "Location \"" + location + "\" does not have name defined");
            assertNotNull(location.getCreateDate(), "Location \"" + location + "\" does not have createDate defined");
            assertNotNull(location.getGeolocation(), "Location \"" + location + "\" does not have geoLocation defined");
            assertNotNull(location.getMenu(), "Location \"" + location + "\" does not have menu defined");
        }
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_LOCATION_MANAGER"})
    public void testDeleteLocation(){
        Integer locationId = 1;
        Location location = locationService.loadLocationByLocationId(locationId);
        assertNotNull(location, "Location could not be loaded from test data source");

        locationService.deleteLocation(location);

        assertEquals(4, locationService.getAllLocations().size(), "No location has been deleted after calling LocationService.deleteLocation");
        assertNull(locationService.loadLocationByLocationId(locationId), "Deleted Location \"" + location + "\" could still be loaded from test data source via locationService.loadLocationByLocationId");

        for (Location remainingLocation : locationService.getAllLocations()) {
            assertNotEquals(location.getLocationId(), remainingLocation.getLocationId(), "Deleted Location \"" + location + "\" could still be loaded from test data source via locationService.getAllLocations()");
        }
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    public void testUpdateLocation() {
        Integer locationId = 1;
        Location location = locationService.loadLocation(locationId);
        assertNotNull(location, "Location could not be loaded from test data source");
        Location toBeUpdatedLocation = locationService.loadLocation(locationId);
        assertNotNull(location, "Location \"" + location + "\" could not be loaded from test data source");

        toBeUpdatedLocation.setName("TestName");
        locationService.saveLocation(toBeUpdatedLocation);

        Location freshlyLoadedLocation = locationService.loadLocation(locationId);
        assertEquals(5, locationService.getAllLocations().size(), "Size of locations did change in database. Location was inserted, not updated");
        assertNotNull(freshlyLoadedLocation, "Location \"" + location + "\" could not be loaded from test data source after being saved");
        assertEquals("TestName", freshlyLoadedLocation.getName(), "Location \"" + location + "\" does not have a the correct name attribute stored being saved");
    }
}
