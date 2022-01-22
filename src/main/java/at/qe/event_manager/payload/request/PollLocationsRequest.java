package at.qe.event_manager.payload.request;

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
