package at.qe.event_manager.model;

import java.io.Serializable;

public class Poll_Timeslots_Id implements Serializable {

	private static final long serialVersionUID = 1L;
	
	Integer poll;
    Integer timeslot;

    public Integer getPoll() {
        return poll;
    }

    public void setPoll(int poll) {
        this.poll = poll;
    }

    public Integer getTimeslot() {
        return timeslot;
    }

    public void setTimeslot(int timeslot) {
        this.timeslot = timeslot;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
