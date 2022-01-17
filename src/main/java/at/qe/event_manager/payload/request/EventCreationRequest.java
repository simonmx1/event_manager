package at.qe.event_manager.payload.request;

import at.qe.event_manager.model.Location;
import at.qe.event_manager.model.Timeslot;
import at.qe.event_manager.model.User;
import org.primefaces.shaded.json.JSONArray;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

public class EventCreationRequest {

    private String name;
    private String creatorUsername;
    private List<String> participants = new ArrayList<>();
    private List<Integer> locations = new ArrayList<>();
    private Set<Date> timeslots = new HashSet<>();
    private Boolean creatorIsPreferred;
    private Date pollEndDate;

    public EventCreationRequest(String name, String creatorUsername, JSONArray participants, JSONArray locations,
                                JSONArray timeslots, Boolean creatorIsPreferred, String pollEndDate) {
            this.name = name;
            this.creatorUsername = creatorUsername;
            participants.forEach(System.out::println);
            locations.forEach(System.out::println);
            timeslots.forEach(System.out::println);
            participants.forEach(user -> this.participants.add(user.toString()));
            locations.forEach(location -> this.locations.add(Integer.valueOf(location.toString())));
            timeslots.forEach(timeslot -> this.timeslots.add(convertStringDateToDate(timeslot.toString())));
            this.creatorIsPreferred = creatorIsPreferred;
            this.pollEndDate = convertStringDateToDate(pollEndDate);
    }

    private Date convertStringDateToDate(String date) {
        return(Timestamp.valueOf(LocalDateTime.parse(date.substring(0, 19))));
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

    public Set<Date> getTimeslots() {
        return timeslots;
    }

    public void setTimeslots(Set<Date> timeslots) {
        this.timeslots = timeslots;
    }

    public Boolean getCreatorIsPreferred() {
        return creatorIsPreferred;
    }

    public void setCreatorIsPreferred(Boolean creatorIsPreferred) {
        this.creatorIsPreferred = creatorIsPreferred;
    }

    public Date getPollEndDate() {
        return pollEndDate;
    }

    public void setPollEndDate(Date pollEndDate) {
        this.pollEndDate = pollEndDate;
    }

    @Override
    public String toString() {
        return "EventCreationRequest{" +
                "name='" + name + '\'' +
                ", creatorUsername='" + creatorUsername + '\'' +
                ", participants=" + participants +
                ", locations=" + locations +
                ", timeslots=" + timeslots +
                ", creatorIsPreferred=" + creatorIsPreferred +
                ", pollEndDate='" + pollEndDate + '\'' +
                '}';
    }
}
