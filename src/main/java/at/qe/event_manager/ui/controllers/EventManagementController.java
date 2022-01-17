package at.qe.event_manager.ui.controllers;

import at.qe.event_manager.model.*;
import at.qe.event_manager.payload.request.EventCreationRequest;
import at.qe.event_manager.payload.response.MessageResponse;
import at.qe.event_manager.services.*;
import org.primefaces.shaded.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/event")
public class EventManagementController {

    @Autowired
    private EventService eventService;

    @Autowired
    private UserService userService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private PollService pollService;

    @Autowired
    private TimeslotService timeslotService;

    @Autowired
    private PollLocationsService pollLocationsService;

    @Autowired
    private PollTimeslotsService pollTimeslotsService;

    @GetMapping("/getAll")
    public Collection<Event> getEvents() {
        return eventService.getAllEvents();
    }

    @GetMapping("/get")
    @ResponseBody
    public Event get(@RequestParam(name = "id") Integer id) {
        return eventService.loadEvent(id);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody EventCreationRequest eventCreationRequest) {
        System.out.println(eventCreationRequest.toString());

        Event event = new Event();
        event.setName(eventCreationRequest.getName());
        Set<User> participants = new HashSet<>();
        eventCreationRequest.getParticipants().forEach(p -> participants.add(userService.loadUserByUsername(p)));
        event.setParticipants(participants);
        event.setLocation(null);
        event.setTimeslot(null);
        event.setCreator(userService.loadUserByUsername(eventCreationRequest.getCreatorUsername()));
        event.setCreatorIsPreferred(eventCreationRequest.getCreatorIsPreferred());
        event.setPollEndDate(convertStringDateToDate(eventCreationRequest.getPollEndDate()));
        event = eventService.saveEvent(event);
        createPollPerParticipant(event, eventCreationRequest.getLocations(), eventCreationRequest.getTimeslots());
        return null;
    }

    private Date convertStringDateToDate(String date) {
        return (Timestamp.valueOf(LocalDateTime.parse(date.substring(0, 19))));
    }

    private void createPollPerParticipant(Event event, List<Integer> locations, List<String> timeslots) {
        Set<Location> pollLocations = new HashSet<>();
        locations.forEach(l -> pollLocations.add(locationService.loadLocationByLocationId(l)));
        Set<Timeslot> pollTimeslots = new HashSet<>();
        timeslots.forEach(t -> {
            Timeslot timeslot = new Timeslot();
            timeslot.setStart((Timestamp) convertStringDateToDate(new JSONObject(t).getString("start")));
            timeslot.setEnd((Timestamp) convertStringDateToDate(new JSONObject(t).getString("end")));
            pollTimeslots.add(timeslotService.saveTimeslot(timeslot));
        });

        for (User participant : event.getParticipants()) {
            Poll poll = new Poll();
            poll.setUser(participant);
            poll.setEvent(event);
            final Poll finalPoll = pollService.savePoll(poll);

            pollLocations.forEach(l -> {
                PollLocations pl = new PollLocations();
                pl.setPoll(finalPoll);
                pl.setLocation(l);
                pollLocationsService.savePollLocations(pl);
            });
            pollTimeslots.forEach(t -> {
                PollTimeslots pt = new PollTimeslots();
                pt.setPoll(finalPoll);
                pt.setTimeslot(t);
                pollTimeslotsService.savePollTimeslots(pt);
            });
        }
    }

    @PostMapping("/edit")
    public ResponseEntity<?> edit(@RequestBody Event event) {
        if (eventService.saveEvent(event) == null) {
            return ResponseEntity.ok(new MessageResponse("Error: Event does not exist!"));
        } else {
            return ResponseEntity.ok(new MessageResponse("Event edited successfully!"));
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody Integer id) {
        eventService.deleteEvent(eventService.loadEvent(id));
        return ResponseEntity.ok(new MessageResponse("Event deleted successfully!"));
    }
}
