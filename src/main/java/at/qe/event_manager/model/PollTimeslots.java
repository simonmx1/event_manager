package at.qe.event_manager.model;

import javax.persistence.*;

@Entity
@Table(name = "pollTimeslots")
@IdClass(PollTimeslotsId.class)
public class PollTimeslots {

    @Id
    @ManyToOne
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
    
    public void addPoints(PollTimeslots poll_timeslot) {
        this.points += poll_timeslot.getPoints();
    }
    
    @Override
    public boolean equals(Object o) {
    	if(o == null || !(o instanceof PollTimeslots)) {
    		return false;
    	}
    	PollTimeslots pl = (PollTimeslots) o;
        return this.timeslot.getId() == pl.getTimeslot().getId();
    }
}