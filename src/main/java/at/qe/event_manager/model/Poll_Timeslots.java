package at.qe.event_manager.model;

import javax.persistence.*;
import java.util.Comparator;

@Entity
@Table(name = "poll_timeslots")
@IdClass(Poll_Timeslots_Id.class)
public class Poll_Timeslots {

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
    
    public void addPoints(Poll_Timeslots poll_timeslot) {
        this.points += poll_timeslot.getPoints();
    }
    
    @Override
    public boolean equals(Object o) {
    	if(o == null || !(o instanceof Poll_Timeslots)) {
    		return false;
    	}
    	Poll_Timeslots pl = (Poll_Timeslots) o;
    	return this.poll.getId() == pl.getPoll().getId() && this.timeslot.getId() == pl.getTimeslot().getId();
    }
}

class SortPollTimeslotsByPoints implements Comparator<Poll_Timeslots> {

    @Override
    public int compare(Poll_Timeslots pt1, Poll_Timeslots pt2) {
        return pt1.getPoints().compareTo(pt2.getPoints());
    }
}