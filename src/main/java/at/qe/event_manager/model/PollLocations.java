package at.qe.event_manager.model;

import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pollLocations")
@IdClass(PollLocationsId.class)
public class PollLocations {

    @Id
    @ManyToOne
    private Poll poll;

    @Id
    @ManyToOne
    private Location location;

    private Integer points;

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
    
    public void addPoints(PollLocations poll_location) {
        this.points += poll_location.getPoints();
    }
    
    @Override
    public boolean equals(Object o) {
    	if(o == null || !(o instanceof PollLocations)) {
    		return false;
    	}
    	PollLocations pl = (PollLocations) o;
    	return this.location.getId() == pl.getLocation().getId();
    }
}

