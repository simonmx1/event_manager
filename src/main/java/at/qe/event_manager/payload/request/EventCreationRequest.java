package at.qe.event_manager.payload.request;

import at.qe.event_manager.model.Location;
import at.qe.event_manager.model.User;
import at.qe.event_manager.services.LocationService;
import at.qe.event_manager.services.UserService;
import org.primefaces.shaded.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class EventCreationRequest {

    @Autowired
    private UserService userService;

    @Autowired
    private LocationService locationService;

    private String name;
    private User creator;
    private Set<User> participants = new HashSet<>();
    private Set<Location> locations = new HashSet<>();
    private Set<Date> timeslots = new HashSet<>();
    private Boolean creatorIsPreferred;
    private Date pollEndDate;

    public EventCreationRequest(String name, String creatorUsername, JSONArray participants, JSONArray locations,
                                JSONArray timeslots, Boolean creatorIsPreferred, String pollEndDate) {

        this.name = name;
        System.out.println(userService);
        this.creator = userService.loadUserByUsername(creatorUsername);
        System.out.println("Hello1!");
        participants.forEach(username -> this.participants.add(userService.loadUserByUsername(username.toString())));
        System.out.println("Hello2!");
        locations.forEach(location -> this.locations.add(locationService.loadLocationByLocationId(Integer.valueOf(location.toString()))));
        System.out.println("Hello3!");
        timeslots.forEach(timeslot -> this.timeslots.add(convertStringDateToDate(timeslot.toString())));
        System.out.println("Hello4!");
        this.creatorIsPreferred = creatorIsPreferred;
        this.pollEndDate = convertStringDateToDate(pollEndDate);
    }

    private Date convertStringDateToDate(String date) {
        return(Timestamp.valueOf(LocalDateTime.parse(date.substring(0, 19))));
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public LocationService getLocationService() {
        return locationService;
    }

    public void setLocationService(LocationService locationService) {
        this.locationService = locationService;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
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
                ", creatorUsername='" + creator + '\'' +
                ", participants=" + participants +
                ", locations=" + locations +
                ", timeslots=" + timeslots +
                ", creatorIsPreferred=" + creatorIsPreferred +
                ", pollEndDate='" + pollEndDate + '\'' +
                '}';
    }
}
