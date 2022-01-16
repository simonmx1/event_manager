package at.qe.event_manager.payload.request;

import at.qe.event_manager.model.Location;
import at.qe.event_manager.model.Timeslot;
import at.qe.event_manager.model.User;

import java.util.Set;

public class EventCreationRequest {

    private String name;
    private String creatorUsername;
    private Set<User> participants;
    private Set<Location> locations;
    private Set<Timeslot> timeslots;
    private Boolean creatorIsPreferred;
    private String pollEndDate;

    public EventCreationRequest(String name, String creatorUsername, Set<User> participants, Set<Location> locations, Boolean creatorIsPreferred) {
        this.name = name;
        this.creatorUsername = creatorUsername;
        this.participants = participants;
        this.locations = locations;
        this.creatorIsPreferred = creatorIsPreferred;
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

    public Set<User> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<User> participants) {
        this.participants = participants;
    }

    public Set<Location> getLocations() {
        return locations;
    }

    public void setLocations(Set<Location> locations) {
        this.locations = locations;
    }

    public Set<Timeslot> getTimeslots() {
        return timeslots;
    }

    public void setTimeslots(Set<Timeslot> timeslots) {
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
