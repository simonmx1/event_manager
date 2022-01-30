package at.qe.event_manager.payload.request;

/**
 * This class is part of the event manager project which was programmed during the
 * "PS Software Architecture" course in the winter semester 2021/2022 at the University of Innsbruck.
 * 
 * @author Matthias Komar
 * @author Manuel Reichegger
 * @author Simon Muscatello
 * @author Stefan Wagner
 * 
 * This class represents an poll location request from the frontend.
 */
public class PollLocationsRequest {
    private Integer pollId;
    private Integer locationId;
    private Integer points;

    public PollLocationsRequest(Integer pollId, Integer locationId, Integer points) {
        this.pollId = pollId;
        this.locationId = locationId;
        this.points = points;
    }

    public Integer getPollId() {
        return pollId;
    }

    public void setPollId(Integer pollId) {
        this.pollId = pollId;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }
}
