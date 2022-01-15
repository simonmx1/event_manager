package at.qe.event_manager.model;

import javax.persistence.*;
import java.util.Comparator;

@Entity
@Table(name = "poll_locations")
@IdClass(Poll_Locations_Id.class)
public class Poll_Locations {

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
    
    public void addPoints(Poll_Locations poll_location) {
        this.points += poll_location.getPoints();
    }
    
    @Override
    public boolean equals(Object o) {
    	if(o == null || !(o instanceof Poll_Locations)) {
    		return false;
    	}
    	Poll_Locations pl = (Poll_Locations) o;
    	return this.poll.getId() == pl.getPoll().getId() && this.location.getId() == pl.getLocation().getId();
    }
}