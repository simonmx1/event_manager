package at.qe.event_manager.tests;

import at.qe.event_manager.model.Location;
import at.qe.event_manager.model.OpeningTime;
import at.qe.event_manager.model.Tag;
import at.qe.event_manager.services.LocationService;
import at.qe.event_manager.services.MailService;
import at.qe.event_manager.services.SchedulerEventService;
import at.qe.event_manager.services.TagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * This class is part of the event manager project which was programmed during the
 * "PS Software Architecture" course in the winter semester 2021/2022 at the University of Innsbruck.
 * 
 * @author Matthias Komar
 * @author Manuel Reichegger
 * @author Simon Muscatello
 * @author Stefan Wagner
 * 
 * Some very basic tests for {@link LocationService}.
 */
@SpringBootTest
@WebAppConfiguration
public class LocationServiceTest {

    @Autowired
    LocationService locationService;
    
    @Autowired
    TagService tagService;
    
    @BeforeEach
	public void disableMailServiceAndSchedulerEventService() {
		MailService.disable();
		SchedulerEventService.disable();
	}
    
    @Test
    public void testInitLocationData() {
    	assertTrue(locationService.getAllLocations().size() >= 5, "Insufficient amount of locations initialized for test data source");
        for(Location location : locationService.getAllLocations()) {
            assertNotNull(location.getName(), "Location does not have name defined");
            assertNotNull(location.getCreateDate(), "Location \"" + location.getName() + "\" does not have createDate defined");
            assertNotNull(location.getGeolocation(), "Location \"" + location.getName() + "\" does not have geoLocation defined");
            assertNotNull(location.getMenu(), "Location \"" + location.getName() + "\" does not have menu defined");
        }
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "user2", authorities = {"ROLE_LOCATION_MANAGER"})
    public void testDeleteLocation(){
        Integer locationId = 1;
        Location location = locationService.loadLocationByLocationId(locationId);
        assertNotNull(location, "Location could not be loaded from test data source");
        int locationSize = locationService.getAllLocations().size();

        locationService.deleteLocation(location);

        assertEquals(locationSize-1, locationService.getAllLocations().size(), "No location has been deleted after calling LocationService.deleteLocation");
        assertNull(locationService.loadLocationByLocationId(locationId), "Deleted Location \"" + location + "\" could still be loaded from test data source via locationService.loadLocationByLocationId");

        for (Location remainingLocation : locationService.getAllLocations()) {
            assertNotEquals(location.getLocationId(), remainingLocation.getLocationId(), "Deleted Location \"" + location + "\" could still be loaded from test data source via locationService.getAllLocations()");
        }
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "user2", authorities = {"ROLE_LOCATION_MANAGER"})
    public void testUpdateLocation() {
        Integer locationId = 1;
        Location location = locationService.loadLocationByLocationId(locationId);
        assertNotNull(location, "Location could not be loaded from test data source");
        Location toBeUpdatedLocation = locationService.loadLocationByLocationId(locationId);
        assertNotNull(location, "Location \"" + location + "\" could not be loaded from test data source");

        toBeUpdatedLocation.setName("TestName");
        locationService.saveLocation(toBeUpdatedLocation);

        Location freshlyLoadedLocation = locationService.loadLocationByLocationId(locationId);
        assertEquals(5, locationService.getAllLocations().size(), "Size of locations did change in database. Location was inserted, not updated");
        assertNotNull(freshlyLoadedLocation, "Location \"" + location + "\" could not be loaded from test data source after being saved");
        assertEquals("TestName", freshlyLoadedLocation.getName(), "Location \"" + location + "\" does not have a the correct name attribute stored being saved");
    }
    
    @DirtiesContext
    @Test
    @Transactional
    @WithMockUser(username = "user2", authorities = {"ROLE_LOCATION_MANAGER"})
    public void testCreateLocation() {
        int locationSize = locationService.getAllLocations().size();
        
        String name = "TEST_LOCATION";
        String menu = "www.test.at";
        String geoLocation = "goo.gl/maps/j4QqpPUHtUoqqyEp9";
        Location toBeCreatedLocation = new Location();
        toBeCreatedLocation.setName(name);
        toBeCreatedLocation.setMenu(menu);
        toBeCreatedLocation.setEnabled(true);
        toBeCreatedLocation.setGeolocation(geoLocation);
        Set<Tag> tags = new HashSet<>();
        Tag tag1 = new Tag();
        tag1.setText("TAG1");
        tag1 = tagService.saveTag(tag1);
        Tag tag2 = new Tag();
        tag2.setText("TAG2");
        tag2 = tagService.saveTag(tag2);
        tags.add(tag1);
        tags.add(tag2);
        toBeCreatedLocation.setTags(tags);
        List<OpeningTime> openingTimes = new ArrayList<>();
        OpeningTime ot = new OpeningTime();
        ot.setWeekday(0);
        ot.setStart(Time.valueOf("12:00:00"));
        ot.setEnd(Time.valueOf("14:00:00"));
        ot.setLocation(toBeCreatedLocation);
        ot.setCreateDate(new Date());
        openingTimes.add(ot);
        toBeCreatedLocation.setOpeningTimes(openingTimes);
        toBeCreatedLocation = locationService.createLocation(toBeCreatedLocation);
        
        Location freshlyCreatedLocation = locationService.loadLocationByLocationId(toBeCreatedLocation.getId());
        assertEquals(locationSize+1, locationService.getAllLocations().size(), "No location has been added after calling LocationService.saveLocation");
        assertNotNull(freshlyCreatedLocation, "New location could not be loaded from test data source after being saved");
        assertEquals(name, freshlyCreatedLocation.getName(), "New location could not be loaded from test data source after being saved");
        assertEquals(menu, freshlyCreatedLocation.getMenu(), "Location \"" + name + "\" does not have a the correct menu attribute stored being saved");
        assertEquals(geoLocation, freshlyCreatedLocation.getGeolocation(), "Location \"" + name + "\" does not have a the correct geolocation attribute stored being saved");
        assertEquals(tags.size(), freshlyCreatedLocation.getTags().size(), "Location \"" + name + "\" does not have a the correct amount of tags stored when being saved");
        assertEquals(openingTimes.size(), freshlyCreatedLocation.getOpeningTimes().size(), "Location \"" + name + "\" does not have a the correct amount of openingTimes stored when being saved");
        assertNotNull(freshlyCreatedLocation.getCreateDate(), "Location \"" + name + "\" does not have a createDate defined after being saved");
    }
    
    @Test
	@WithMockUser(username = "elvis", authorities = {"ROLE_USER"})
	public void testUnauthorizedSaveLocation() {
		Location toNotBeSavedLocation = locationService.loadLocationByLocationId(1);
		assertThrows(AccessDeniedException.class, () -> locationService.createLocation(toNotBeSavedLocation),
			 "Call to LocationService.saveLocation should not work without proper authorization");
	}
    
    @Test
    @WithMockUser(username = "elvis", authorities = {"ROLE_USER"})
    public void testUnauthorizedDeleteLocation() {
    	Location toNotBeSavedLocation = locationService.loadLocationByLocationId(1);
		assertThrows(AccessDeniedException.class, () -> locationService.deleteLocation(toNotBeSavedLocation),
				 "Call to LocationService.deleteLocation should not work without proper authorization");
    }
}
