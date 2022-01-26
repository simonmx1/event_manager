package at.qe.event_manager.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import at.qe.event_manager.model.Event;
import at.qe.event_manager.services.EventService;

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
    public void testDeleteEvent(){
		Integer eventId = 1;
        Event event = eventService.loadEvent(eventId);
        assertNotNull(event, "Event could not be loaded from test data source");
        int eventSize = eventService.getAllEvents().size();

        eventService.deleteEvent(event);

        assertEquals(eventSize-1, eventService.getAllEvents().size(), "No event has been deleted after calling eventService.deleteEvent");
        assertNull(eventService.loadEvent(eventId), "Deleted Event \"" + event + "\" could still be loaded from test data source via eventService.loadEvent");

        for (Event remainingEvent : eventService.getAllEvents()) {
            assertNotEquals(event.getId(), remainingEvent.getId(), "Deleted Event \"" + event + "\" could still be loaded from test data source via eventService.getAllEvents()");
        }
	}
}
