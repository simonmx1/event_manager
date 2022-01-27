package at.qe.event_manager.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import at.qe.event_manager.model.Event;
import at.qe.event_manager.model.Location;
import at.qe.event_manager.model.Poll;
import at.qe.event_manager.model.PollLocations;
import at.qe.event_manager.model.PollTimeslots;
import at.qe.event_manager.model.Timeslot;
import at.qe.event_manager.model.User;
import at.qe.event_manager.services.EventService;
import at.qe.event_manager.services.LocationService;
import at.qe.event_manager.services.MailService;
import at.qe.event_manager.services.PollLocationsService;
import at.qe.event_manager.services.PollService;
import at.qe.event_manager.services.PollTimeslotsService;
import at.qe.event_manager.services.SchedulerEventService;
import at.qe.event_manager.services.TimeslotService;
import at.qe.event_manager.services.UserService;

/**
 * Some very basic tests for {@link EventService}.
 *
 * This class is part of the event_manager project.
 */
@SpringBootTest
@WebAppConfiguration
public class EventServiceTest {
	
	@Autowired
	EventService eventService;
	
	@Autowired
	PollService pollService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	TimeslotService timeslotService;
	
	@Autowired
	LocationService locationService;
	
	@Autowired
	PollTimeslotsService pollTimeslotsService;
	
	@Autowired
	PollLocationsService pollLocationsService;
	
	@BeforeEach
	public void disableMailServiceAndSchedulerEventService() {
		MailService.disable();
		SchedulerEventService.disable();
	}
	
	@Test
    public void testInitEventData() {
    	assertTrue(eventService.getAllEvents().size() >= 5, "Insufficient amount of events initialized for test data source");
        for(Event event : eventService.getAllEvents()) {
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
    public void testDeleteEvent() {
		Integer eventId = 1;
        Event event = eventService.loadEventByEventId(eventId);
        assertNotNull(event, "Event could not be loaded from test data source");
        int eventSize = eventService.getAllEvents().size();

        eventService.deleteEvent(event);

        assertEquals(eventSize-1, eventService.getAllEvents().size(), "No event has been deleted after calling eventService.deleteEvent");
        assertNull(eventService.loadEventByEventId(eventId), "Deleted Event \"" + event + "\" could still be loaded from test data source via eventService.loadEvent");

        for (Event remainingEvent : eventService.getAllEvents()) {
            assertNotEquals(event.getId(), remainingEvent.getId(), "Deleted Event \"" + event + "\" could still be loaded from test data source via eventService.getAllEvents()");
        }
	}
	
	@DirtiesContext
    @Test
    @WithMockUser(username = "elvis", authorities = {"ROLE_USER"})
    public void testCreateEvent() {
		int eventSize = eventService.getAllEvents().size();
		
		String name = "TEST_EVENT";
		User creator = userService.loadUserByUsername("elvis");
		Event event = new Event();
		event.setName(name);
		event.setCreator(creator);
		event.setCreatorIsPreferred(true);
		event.setPollEndDate(Date.valueOf("2023-1-1"));
		event = eventService.saveEvent(event);
		
		Set<User> participants = new HashSet<>();
		User p1 = userService.loadUserByUsername("admin");
		participants.add(creator);
		participants.add(p1);
		event.setParticipants(participants);
		
		Set<Poll> polls = new HashSet<>();
		Poll poll1 = new Poll();
		poll1.setUser(creator);
		poll1.setEvent(event);
		poll1 = pollService.savePoll(poll1);
		polls.add(poll1);
		Poll poll2 = new Poll();
		poll2.setUser(p1);
		poll2.setEvent(event);
		poll2 = pollService.savePoll(poll2);
		polls.add(poll2);
		
		Timeslot t = new Timeslot();
		t.setStart(Timestamp.valueOf("2023-8-1 12:00:00"));
		t.setEnd(Timestamp.valueOf("2023-8-1 14:00:00"));
		t = timeslotService.saveTimeslot(t);
		
		Set<PollTimeslots> poll1Timeslots = new HashSet<>();
		PollTimeslots pt1 = new PollTimeslots();
		pt1.setPoll(poll1);
		pt1.setTimeslot(t);
		pt1.setPoints(1);
		pt1 = pollTimeslotsService.savePollTimeslots(pt1);
		poll1Timeslots.add(pt1);
		poll1.setPollTimeslots(poll1Timeslots);
		
		Set<PollTimeslots> poll2Timeslots = new HashSet<>();
		PollTimeslots pt2 = new PollTimeslots();
		pt2.setPoll(poll2);
		pt2.setTimeslot(t);
		pt2.setPoints(2);
		pt2 = pollTimeslotsService.savePollTimeslots(pt2);
		poll2Timeslots.add(pt2);
		poll2.setPollTimeslots(poll2Timeslots);
		
		Location l = locationService.loadLocationByLocationId(2);
		
		Set<PollLocations> poll1Locations = new HashSet<>();
		PollLocations pl1 = new PollLocations();
		pl1.setPoll(poll1);
		pl1.setLocation(l);
		pl1.setPoints(1);
		pl1 = pollLocationsService.savePollLocations(pl1);
		poll1Locations.add(pl1);
		poll1.setPollLocations(poll1Locations);
		
		Set<PollLocations> poll2Locations = new HashSet<>();
		PollLocations pl2 = new PollLocations();
		pl2.setPoll(poll2);
		pl2.setLocation(l);
		pl2.setPoints(2);
		pl2 = pollLocationsService.savePollLocations(pl2);
		poll2Locations.add(pl2);
		poll2.setPollLocations(poll2Locations);
		
		event.setPolls(polls);
		event = eventService.saveEvent(event);
		
		Event freshlyCreatedEvent = eventService.loadEventByEventId(event.getId());
		assertEquals(eventSize+1, eventService.getAllEvents().size(), "No event has been added after calling EventService.saveEvent");
		assertNotNull(freshlyCreatedEvent, "New event could not be loaded from test data source after being saved");
        assertEquals(name, freshlyCreatedEvent.getName(), "New event could not be loaded from test data source after being saved");
        assertEquals(creator, freshlyCreatedEvent.getCreator(), "Event \"" + name + "\" does not have a the correct creator attribute stored being saved");
        assertNotNull(event.getParticipants(), "Event \"" + name + "\" does not have Participants stored when being saved");
        assertEquals(2, event.getParticipants().size(), "Event \"" + name + "\" does not have enough Participants stored when being saved");
        assertNotNull(event.getPolls(), "Event \"" + name + "\" does not have Polls stored when being saved");
		assertEquals( 2, event.getPolls().size(), "Event \"" + name + "\" does not have enough Polls stored when being saved");
        assertNotNull(freshlyCreatedEvent.getCreateDate(), "Event \"" + name + "\" does not have a createDate defined after being saved");
	}
	
	@Test
    @WithMockUser(username = "elvis", authorities = {"ROLE_USER"})
    public void testUnauthorizedDeleteLocation() {
    	Event toNotBeSavedEvent = eventService.loadEventByEventId(2);
    	assertEquals("admin", toNotBeSavedEvent.getCreator().getUsername());
		assertThrows(AccessDeniedException.class, () -> eventService.deleteEvent(toNotBeSavedEvent),
				 "Call to LocationService.deleteLocation should not work without proper authorization");
    }
	
	@DirtiesContext
	@Test
	public void testEvaluatePolls() {
		Event toBeEvaluatedEvent = eventService.loadEventByEventId(1);
		toBeEvaluatedEvent.setCreatorIsPreferred(true);
		eventService.evaluatePolls(toBeEvaluatedEvent);
		assertEquals(locationService.loadLocationByLocationId(2), toBeEvaluatedEvent.getLocation());
		assertEquals(timeslotService.loadTimeslotByLocationId(1), toBeEvaluatedEvent.getTimeslot());
	}
}
