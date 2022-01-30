package at.qe.event_manager.model;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * This class is part of the event manager project which was programmed during the
 * "PS Software Architecture" course in the winter semester 2021/2022 at the University of Innsbruck.
 * 
 * @author Matthias Komar
 * @author Manuel Reichegger
 * @author Simon Muscatello
 * @author Stefan Wagner
 * 
 * Entity representing pollLocations.
 * <p>
 * This class models a part of the poll of a user with points of a location.
 */
@Entity
@Table(name = "pollLocations")
@IdClass(PollLocationsId.class)
public class PollLocations implements Serializable {

    private static final long serialVersionUID = 1L;

    /** The ID of the poll for which the ranking applies */
    @Id
    @ManyToOne
    @JsonIgnore
    private Poll poll;

    /** The location to which the points apply */
    @Id
    @ManyToOne
    private Location location;

    /** The points for the location */
    private Integer points;

    public PollLocations() {
    }

    public PollLocations(PollLocations pollLocationToBeCopied) {
        this.poll = pollLocationToBeCopied.getPoll();
        this.location = pollLocationToBeCopied.getLocation();
        this.points = pollLocationToBeCopied.getPoints();
    }

    public Poll getPoll() {
        return poll;
    }

    public void setPoll(Poll poll) {
        this.poll = poll;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public void addPoints(PollLocations pollLocation) {
        this.points += pollLocation.getPoints();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PollLocations)) {
            return false;
        }
        PollLocations pl = (PollLocations) o;
        return this.location.getId() == pl.getLocation().getId();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}

