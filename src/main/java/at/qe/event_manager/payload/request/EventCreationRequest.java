package at.qe.event_manager.payload.request;

import org.primefaces.shaded.json.JSONArray;
import java.util.*;

/**
 * This class is part of the event manager project which was programmed during
 * the "PS Software Architecture" course in the winter semester 2021/2022 at the
 * University of Innsbruck.
 * 
 * @author Matthias Komar
 * @author Manuel Reichegger
 * @author Simon Muscatello
 * @author Stefan Wagner
 * 
 * This class represents an event creation request from the frontend.
 */
public class EventCreationRequest {

	private String name;
	private String creatorUsername;
	private List<String> participants = new ArrayList<>();
	private List<Integer> locations = new ArrayList<>();
	private List<String> timeslots = new ArrayList<>();
	private Boolean creatorIsPreferred;
	private String pollEndDate;

	public EventCreationRequest(String name, String creatorUsername, JSONArray participants, JSONArray locations,
			JSONArray timeslots, Boolean creatorIsPreferred, String pollEndDate) {
		
		this.name = name;
		this.creatorUsername = creatorUsername;
		participants.forEach(user -> this.participants.add(user.toString()));
		locations.forEach(location -> this.locations.add(Integer.valueOf(location.toString())));
		timeslots.forEach(timeslot -> this.timeslots.add(timeslot.toString()));
		this.creatorIsPreferred = creatorIsPreferred;
		this.pollEndDate = pollEndDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCreatorUsername() {
		return creatorUsername;
	}

	public void setCreatorUsername(String creatorUsername) {
		this.creatorUsername = creatorUsername;
	}

	public List<String> getParticipants() {
		return participants;
	}

	public void setParticipants(List<String> participants) {
		this.participants = participants;
	}

	public List<Integer> getLocations() {
		return locations;
	}

	public void setLocations(List<Integer> locations) {
		this.locations = locations;
	}

	public List<String> getTimeslots() {
		return timeslots;
	}

	public void setTimeslots(List<String> timeslots) {
		this.timeslots = timeslots;
	}

	public Boolean getCreatorIsPreferred() {
		return creatorIsPreferred;
	}

	public void setCreatorIsPreferred(Boolean creatorIsPreferred) {
		this.creatorIsPreferred = creatorIsPreferred;
	}

	public String getPollEndDate() {
		return pollEndDate;
	}

	public void setPollEndDate(String pollEndDate) {
		this.pollEndDate = pollEndDate;
	}
}
