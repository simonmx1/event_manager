package at.qe.event_manager.tests;

import at.qe.event_manager.model.*;
import at.qe.event_manager.payload.request.EventCreationRequest;
import at.qe.event_manager.services.*;
import at.qe.event_manager.ui.controllers.EventManagementController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This class is part of the event manager project which was programmed during the
 * "PS Software Architecture" course in the winter semester 2021/2022 at the University of Innsbruck.
 * 
 * @author Matthias Komar
 * @author Manuel Reichegger
 * @author Simon Muscatello
 * @author Stefan Wagner
 * 
 * Some very basic tests for {@link EventManagementController}.
 */
@SpringBootTest
@WebAppConfiguration
public class EventManagementControllerTest {

    @Autowired
    EventManagementController eventManagementController;

    @Autowired
    private UserService userService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private PollService pollService;

    @Autowired
    private TimeslotService timeslotService;

    @BeforeEach
    public void disableMailServiceAndSchedulerEventService() {
        MailService.disable();
        SchedulerEventService.disable();
    }

    @DirtiesContext
    @Test
    @Transactional
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    public void testInitEventData() {
        assertTrue(eventManagementController.getEvents().size() >= 5, "Insufficient amount of events initialized for test data source");
        for (Event event : eventManagementController.getEvents()) {
            assertNotNull(event.getName(), "Event does not have name defined");
            assertNotNull(event.getCreator(), "Event \"" + event.getName() + "\" does not have a Creator defined");
            assertNotNull(event.getParticipants(), "Event \"" + event.getName() + "\" does not have Participants defined");
            assertTrue(event.getParticipants().size() >= 2, "Event \"" + event.getName() + "\" does not have enough Participants defined");
            assertNotNull(event.getPolls(), "Event \"" + event.getName() + "\" does not have Polls defined");
            assertTrue(event.getPolls().size() >= 2, "Event \"" + event.getName() + "\" does not have enough Polls defined");
            assertNotNull(event.getPollEndDate(), "Event \"" + event.getName() + "\" does not have a PollEndDate defined");
            assertNotNull(event.getCreateDate(), "Event \"" + event.getName() + "\" does not have a CreateDate defined");
        }
    }
    
    @DirtiesContext
    @Test
    @Transactional
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    public void testCreateEvent() {
    	int eventSize = eventManagementController.getEvents().size();
    	String name = "Test";
    	String creatorUsername = "admin";
    	JSONArray participants = new JSONArray();
    	participants.put("user1");
    	participants.put("user2");
    	JSONArray locations = new JSONArray();
    	locations.put(2);
    	locations.put(3);
    	JSONArray timeslots = new JSONArray();
    	timeslots.put(new JSONObject().put("start", "2022-02-01T11:00:00.000Z").put("end", "2022-02-01T14:30:00.000Z"));
    	timeslots.put(new JSONObject().put("start", "2022-02-02T17:30:00.000Z").put("end", "2022-02-02T20:30:00.000Z"));
    	Boolean creatorIsPreferred = true;
    	String pollEndDate = "2022-01-30T23:00:00.000Z";
    	EventCreationRequest request = new EventCreationRequest(name, creatorUsername, participants, locations, timeslots, creatorIsPreferred, pollEndDate);
    	assertEquals(HttpStatus.CREATED, eventManagementController.create(request).getStatusCode(),
				"Call to eventManagementController.create should work with proper timeslots");
    	assertEquals(eventSize+1, eventManagementController.getEvents().size(), "Call to eventManagementController.create did not create a new event");
    }

    @DirtiesContext
    @Test
    @Transactional
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    public void testDeleteEvent() {
        Integer eventId = 1;
        Event event = eventManagementController.get(eventId);
        assertNotNull(event, "Event could not be loaded from test data source");
        int eventSize = eventManagementController.getEvents().size();

        eventManagementController.delete(event.getId());

        assertEquals(eventSize - 1, eventManagementController.getEvents().size(), "No event has been deleted after calling eventManagementController.deleteEvent");
        assertNull(eventManagementController.get(eventId), "Deleted Event \"" + event + "\" could still be loaded from test data source via eventManagementController.get");

        for (Event remainingEvent : eventManagementController.getEvents()) {
            assertNotEquals(event.getId(), remainingEvent.getId(), "Deleted Event \"" + event + "\" could still be loaded from test data source via eventManagementController.getEvents");
        }
    }

    @Test
    @WithMockUser(username = "elvis", authorities = {"ROLE_USER"})
    public void testUnauthorizedDeleteEvent() {
        Event toNotBeDeletedEvent  = eventManagementController.get(2);
        assertEquals("admin", toNotBeDeletedEvent.getCreator().getUsername());
        assertThrows(AccessDeniedException.class, () -> eventManagementController.delete(toNotBeDeletedEvent.getId()),
                "Call to eventManagementController.delete should not work without proper authorization");
    }

    @DirtiesContext
    @Test
    @Transactional
    public void testEvaluatePollsWithCreatorPreferred() {
        Event toBeEvaluatedEvent = eventManagementController.get(1);
        assertEquals(userService.loadUserByUsername("admin"), toBeEvaluatedEvent.getCreator());
        toBeEvaluatedEvent.setCreatorIsPreferred(true);

        Poll user2_poll = pollService.loadPollByEventAndUser(toBeEvaluatedEvent, userService.loadUserByUsername("user2"));
        List<PollLocations> user2_pollLocations = new ArrayList<>(user2_poll.getPollLocations());
        for (PollLocations user2_pollLocation : user2_pollLocations) {
            if (user2_pollLocation.getLocation().compareTo(locationService.loadLocationByLocationId(1)) == 0) {
                assertEquals(3, user2_pollLocation.getPoints(), "Location \"" + user2_pollLocation.getLocation().getName() + "\" is not correctly initialised with the right points.");
            }
            if (user2_pollLocation.getLocation().compareTo(locationService.loadLocationByLocationId(2)) == 0) {
                assertEquals(2, user2_pollLocation.getPoints(), "Location \"" + user2_pollLocation.getLocation().getName() + "\" is not correctly initialised with the right points.");
            }
            if (user2_pollLocation.getLocation().compareTo(locationService.loadLocationByLocationId(3)) == 0) {
                assertEquals(1, user2_pollLocation.getPoints(), "Location \"" + user2_pollLocation.getLocation().getName() + "\" is not correctly initialised with the right points.");
            }
        }
        List<PollTimeslots> user2_pollTimeslots = new ArrayList<>(user2_poll.getPollTimeslots());
        for (PollTimeslots user2_pollTimeslot : user2_pollTimeslots) {
            if (user2_pollTimeslot.getTimeslot().compareTo(timeslotService.loadTimeslotByLocationId(1)) == 0) {
                assertEquals(1, user2_pollTimeslot.getPoints(), "Timeslot \"" + user2_pollTimeslot.getTimeslot().getId() + "\" is not correctly initialised with the right points.");
            }
            if (user2_pollTimeslot.getTimeslot().compareTo(timeslotService.loadTimeslotByLocationId(2)) == 0) {
                assertEquals(2, user2_pollTimeslot.getPoints(), "Timeslot \"" + user2_pollTimeslot.getTimeslot().getId() + "\" is not correctly initialised with the right points.");
            }
            if (user2_pollTimeslot.getTimeslot().compareTo(timeslotService.loadTimeslotByLocationId(3)) == 0) {
                assertEquals(0, user2_pollTimeslot.getPoints(), "Timeslot \"" + user2_pollTimeslot.getTimeslot().getId() + "\" is not correctly initialised with the right points.");
            }
        }

        Poll admin_poll = pollService.loadPollByEventAndUser(toBeEvaluatedEvent, userService.loadUserByUsername("admin"));
        List<PollLocations> admin_pollLocations = new ArrayList<>(admin_poll.getPollLocations());
        for (PollLocations admin_pollLocation : admin_pollLocations) {
            if (admin_pollLocation.getLocation().compareTo(locationService.loadLocationByLocationId(1)) == 0) {
                assertEquals(1, admin_pollLocation.getPoints(), "Location \"" + admin_pollLocation.getLocation().getName() + "\" is not correctly initialised with the right points.");
            }
            if (admin_pollLocation.getLocation().compareTo(locationService.loadLocationByLocationId(2)) == 0) {
                assertEquals(3, admin_pollLocation.getPoints(), "Location \"" + admin_pollLocation.getLocation().getName() + "\" is not correctly initialised with the right points.");
            }
            if (admin_pollLocation.getLocation().compareTo(locationService.loadLocationByLocationId(3)) == 0) {
                assertEquals(2, admin_pollLocation.getPoints(), "Location \"" + admin_pollLocation.getLocation().getName() + "\" is not correctly initialised with the right points.");
            }
        }
        List<PollTimeslots> admin_pollTimeslots = new ArrayList<>(admin_poll.getPollTimeslots());
        for (PollTimeslots admin_pollTimeslot : admin_pollTimeslots) {
            if (admin_pollTimeslot.getTimeslot().compareTo(timeslotService.loadTimeslotByLocationId(1)) == 0) {
                assertEquals(2, admin_pollTimeslot.getPoints(), "Timeslot \"" + admin_pollTimeslot.getTimeslot().getId() + "\" is not correctly initialised with the right points.");
            }
            if (admin_pollTimeslot.getTimeslot().compareTo(timeslotService.loadTimeslotByLocationId(2)) == 0) {
                assertEquals(1, admin_pollTimeslot.getPoints(), "Timeslot \"" + admin_pollTimeslot.getTimeslot().getId() + "\" is not correctly initialised with the right points.");
            }
            if (admin_pollTimeslot.getTimeslot().compareTo(timeslotService.loadTimeslotByLocationId(3)) == 0) {
                assertEquals(3, admin_pollTimeslot.getPoints(), "Timeslot \"" + admin_pollTimeslot.getTimeslot().getId() + "\" is not correctly initialised with the right points.");
            }
        }

        /*
         * Location Winner should be McDonalds (Id = 2) with 5 points
         * Timeslot Winner should be Id = 1 => there's a conflict
         * Id: 1 -> points: 3;
         * Id: 2 -> points: 3;
         * Creator preferred Id 1
         */
        eventManagementController.evaluatePolls(toBeEvaluatedEvent.getId());
        assertEquals(locationService.loadLocationByLocationId(2), toBeEvaluatedEvent.getLocation(), "Location Winner is wrong");
        assertEquals(timeslotService.loadTimeslotByLocationId(1), toBeEvaluatedEvent.getTimeslot(), "Timeslot Winner is wrong");
    }

    @DirtiesContext
    @Transactional
    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    public void testEvaluatePollsWithNoAvailableTimeslot() {
        int eventSize = eventManagementController.getEvents().size();
        Event toBeEvaluatedEvent = eventManagementController.get(1);
        assertNotNull(toBeEvaluatedEvent, "event loaded from database should not be null");

        Poll user2_poll = pollService.loadPollByEventAndUser(toBeEvaluatedEvent, userService.loadUserByUsername("user2"));
        for (PollTimeslots user2_pollTimeslot : user2_poll.getPollTimeslots()) {
            user2_pollTimeslot.setPoints(0);
        }

        //No available timeslot means -> the event cannot be held and therefore will be deleted
        eventManagementController.evaluatePolls(toBeEvaluatedEvent.getId());
        assertEquals(eventSize - 1, eventManagementController.getEvents().size(), "eventSize should have decreased by one because of event deletion");
    }

    @DirtiesContext
    @Transactional
    @Test
    public void testGetAllEventFromUser() {
        String username = "admin";
        User admin = userService.loadUserByUsername(username);
        assertNotNull(admin, "Admin user could not be loaded from test data source");
        Collection<Event> events = eventManagementController.getEventsFromUser(admin.getUsername());
        assertEquals(4, events.size(), "eventService.getAllEventsFromUser has been loaded less or more events then the Admin has");
        for (Event event : events) {
            if (!event.getName().equals("Dinner") &&
                    !event.getName().equals("Caffe Time") &&
                    !event.getName().equals("Breakfast") &&
                    !event.getName().equals("50 Years Party")){
                fail("Loaded the wrong events for Admin");
            }
        }
    }
}
