package at.qe.event_manager.model;

import java.io.Serializable;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "pollTimeslots")
@IdClass(PollTimeslotsId.class)
public class PollTimeslots implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
    @ManyToOne
    @JsonIgnore
    private Poll poll;

    @Id
    @ManyToOne
    private Timeslot timeslot;

    private Integer points;

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
    	if(!(o instanceof PollTimeslots)) {
    		return false;
    	}
    	PollTimeslots pl = (PollTimeslots) o;
        return this.timeslot.getId() == pl.getTimeslot().getId();
    }
}