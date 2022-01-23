package at.qe.event_manager.model;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "pollLocations")
@IdClass(PollLocationsId.class)
public class PollLocations implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @ManyToOne
    @JsonIgnore
    private Poll poll;

    @Id
    @ManyToOne
    private Location location;

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
}

