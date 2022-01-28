package at.qe.event_manager.model;

import java.io.Serializable;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Entity representing pollTimeslots.
 * <p>
 * This class models a part of the poll of a user with points of a timeslot.
 */
@Entity
@Table(name = "pollTimeslots")
@IdClass(PollTimeslotsId.class)
public class PollTimeslots implements Serializable {

    private static final long serialVersionUID = 1L;

    /** The ID of the poll for which the ranking applies */
    @Id
    @ManyToOne
    @JsonIgnore
    private Poll poll;

    /** The timeslot to which the points apply */
    @Id
    @ManyToOne
    private Timeslot timeslot;

    /** The points for the location */
    private Integer points;

    public PollTimeslots() {
    }

    public PollTimeslots(PollTimeslots pollTimeslotToBeCopied) {
        this.poll = pollTimeslotToBeCopied.getPoll();
        this.timeslot = pollTimeslotToBeCopied.getTimeslot();
        this.points = pollTimeslotToBeCopied.getPoints();
    }

    public Poll getPoll() {
        return poll;
    }

    public void setPoll(Poll poll) {
        this.poll = poll;
    }

    public Timeslot getTimeslot() {
        return timeslot;
    }

    public void setTimeslot(Timeslot timeslot) {
        this.timeslot = timeslot;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public void addPoints(PollTimeslots pollTimeslot) {
        this.points += pollTimeslot.getPoints();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PollTimeslots)) {
            return false;
        }
        PollTimeslots pl = (PollTimeslots) o;
        return this.timeslot.getId() == pl.getTimeslot().getId();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}