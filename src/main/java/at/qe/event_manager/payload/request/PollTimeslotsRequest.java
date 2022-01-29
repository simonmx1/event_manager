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
public class PollTimeslotsRequest {
    private Integer pollId;
    private Integer timeslotId;
    private Integer points;

    public PollTimeslotsRequest(Integer pollId, Integer timeslotId, Integer points) {
        this.pollId = pollId;
        this.timeslotId = timeslotId;
        this.points = points;
    }

    public Integer getPollId() {
        return pollId;
    }

    public void setPollId(Integer pollId) {
        this.pollId = pollId;
    }

    public Integer getTimeslotId() {
        return timeslotId;
    }

    public void setTimeslotId(Integer timeslotId) {
        this.timeslotId = timeslotId;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }
}
