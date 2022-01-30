package at.qe.event_manager.model;

import java.io.Serializable;

/**
 * Entity representing pollTimeslotId.
 * <p>
 * This class models connect the poll with the timeslot
 */
public class PollTimeslotsId implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The pollID
     */
    private Integer poll;

    /**
     * The timeslotID
     */
    private Integer timeslot;

    public Integer getPoll() {
        return poll;
    }

    public void setPollId(Integer poll) {
        this.poll = poll;
    }

    public Integer getTimeslot() {
        return timeslot;
    }

    public void setTimeslotId(Integer timeslot) {
        this.timeslot = timeslot;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((poll == null) ? 0 : poll.hashCode());
        result = prime * result + ((timeslot == null) ? 0 : timeslot.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof PollTimeslotsId))
            return false;

        PollTimeslotsId other = (PollTimeslotsId) obj;

        if (poll == null) {
            if (other.poll != null)
                return false;
        } else if (!poll.equals(other.poll))
            return false;

        if (timeslot == null) {
            return other.timeslot == null;
        } else return timeslot.equals(other.timeslot);
    }
}
