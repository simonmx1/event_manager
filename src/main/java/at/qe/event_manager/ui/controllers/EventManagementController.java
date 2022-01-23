package at.qe.event_manager.ui.controllers;

import at.qe.event_manager.model.*;
import at.qe.event_manager.payload.request.EventCreationRequest;
import at.qe.event_manager.payload.response.MessageResponse;
import at.qe.event_manager.services.*;
import org.primefaces.shaded.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.sql.Timestamp;
import java.time.Instant;
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

	@GetMapping("/getAllFromUser")
	public Collection<Event> getEventsFromUser(@RequestParam(name = "username") String username) {
		return eventService.getAllEventFromUser(userService.loadUserByUsername(username));
	}

	@GetMapping("/get")
	@ResponseBody
	public Event get(@RequestParam(name = "id") Integer id) {
		return eventService.loadEvent(id);
	}

	@PostMapping("/create")
	public ResponseEntity<String> create(@RequestBody EventCreationRequest eventCreationRequest) {
		if (!checkTimeslotsWithLocationOpeningTimes(eventCreationRequest.getLocations(),
				eventCreationRequest.getTimeslots())) {
			return new ResponseEntity<>("At least one Timeslot is not matching with at least one Location opening time",
					HttpStatus.OK);
		}
		Event event = new Event();
		event.setName(eventCreationRequest.getName());
		event.setCreator(userService.loadUserByUsername(eventCreationRequest.getCreatorUsername()));
		Set<User> participants = new HashSet<>();
		participants.add(event.getCreator());
		eventCreationRequest.getParticipants().forEach(p -> participants.add(userService.loadUserByUsername(p)));
		event.setParticipants(participants);
		event.setLocation(null);
		event.setTimeslot(null);
		event.setCreatorIsPreferred(eventCreationRequest.getCreatorIsPreferred());
		event.setPollEndDate(convertStringDateToDate(eventCreationRequest.getPollEndDate()));
		event = eventService.saveEvent(event);
		createPollPerParticipant(event, eventCreationRequest.getLocations(), eventCreationRequest.getTimeslots());
		return new ResponseEntity<>("Event has been created successfully!", HttpStatus.CREATED);
	}

	private Boolean checkTimeslotsWithLocationOpeningTimes(List<Integer> locationIds, List<String> timeslotsStrings) {
		List<Location> locations = new ArrayList<>();
		locationIds.forEach(l -> locations.add(locationService.loadLocationByLocationId(l)));
		List<Timeslot> timeslots = new ArrayList<>();
		timeslotsStrings.forEach(t -> {
			Timeslot timeslot = new Timeslot();
			timeslot.setStart(convertStringDateToDate(new JSONObject(t).getString("start")));
			timeslot.setEnd(convertStringDateToDate(new JSONObject(t).getString("end")));
			timeslots.add(timeslot);
		});
		boolean correctTimeslot = false;
		for (Location l : locations) {
			List<OpeningTime> openingTimes = l.getOpeningTimes();
			for(Timeslot timeslot : timeslots) {
				correctTimeslot = false;
				for(OpeningTime openingTime : openingTimes) {
					Timestamp ts = timeslot.getStart();
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(ts);
					int weekday = calendar.get(Calendar.DAY_OF_WEEK)-2;
					if(openingTime.getWeekday() == weekday) {
						if(openingTime.getStart().toLocalTime().compareTo(timeslot.getStart().toLocalDateTime().toLocalTime()) <= 0 && openingTime.getEnd().toLocalTime().compareTo(timeslot.getEnd().toLocalDateTime().toLocalTime()) >= 0)  {
							correctTimeslot = true;
						}
					}
				}
				if(!correctTimeslot) return false;
			}
		}
		return true;
	}

	private Timestamp convertStringDateToDate(String date) {
		return Timestamp.from(Instant.parse(date));
	}

	private void createPollPerParticipant(Event event, List<Integer> locations, List<String> timeslots) {
		Set<Location> pollLocations = new HashSet<>();
		locations.forEach(l -> pollLocations.add(locationService.loadLocationByLocationId(l)));
		Set<Timeslot> pollTimeslots = new HashSet<>();
		timeslots.forEach(t -> {
			Timeslot timeslot = new Timeslot();
			timeslot.setStart(convertStringDateToDate(new JSONObject(t).getString("start")));
			timeslot.setEnd(convertStringDateToDate(new JSONObject(t).getString("end")));
			pollTimeslots.add(timeslotService.saveTimeslot(timeslot));
		});

		for (User participant : event.getParticipants()) {
			Poll poll = new Poll();
			poll.setUser(participant);
			poll.setEvent(event);
			final Poll finalPoll = pollService.savePoll(poll);
			int temp = 1;
			for (Location l : pollLocations) {
				PollLocations pl = new PollLocations();
				pl.setPoll(finalPoll);
				pl.setPoints(temp++);
				pl.setLocation(l);
				pollLocationsService.savePollLocations(pl);
			}
			temp = 1;
			for (Timeslot t : pollTimeslots) {
				PollTimeslots pt = new PollTimeslots();
				pt.setPoll(finalPoll);
				pt.setPoints(temp++);
				pt.setTimeslot(t);
				pollTimeslotsService.savePollTimeslots(pt);
			}
			MailService.sendEventCreationMessage(participant, event);
		}
	}

	@PostMapping("/edit")
	public ResponseEntity<MessageResponse> edit(@RequestBody Event event) {
		if (eventService.saveEvent(event) == null) {
			return ResponseEntity.ok(new MessageResponse("Error: Event does not exist!"));
		} else {
			return ResponseEntity.ok(new MessageResponse("Event edited successfully!"));
		}
	}

	@PostMapping("/delete")
	public ResponseEntity<MessageResponse> delete(@RequestBody Integer id) {
		eventService.deleteEvent(eventService.loadEvent(id));
		return ResponseEntity.ok(new MessageResponse("Event deleted successfully!"));
	}

	@PostMapping("/evaluatePolls")
	public ResponseEntity<MessageResponse> evaluatePolls(@RequestBody Integer id) {
		eventService.evaluatePolls(eventService.loadEvent(id));
		return ResponseEntity.ok(new MessageResponse("Event evaluated successfully!"));
	}
}
