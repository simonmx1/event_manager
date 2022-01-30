package at.qe.event_manager.model;

import java.io.Serializable;

/**
 * Entity representing pollLocationsId.
 * <p>
 * This class models connect the poll with the location
 */
public class PollLocationsId implements Serializable {

    private static final long serialVersionUID = 1L;

    /** The pollID */
    private Integer poll;

    /** The locationID */
    private Integer location;

    public Integer getPoll() {
        return poll;
    }

    public void setPollId(Integer poll) {
        this.poll = poll;
    }

    public Integer getLocation() {
        return location;
    }

    public void setLocation(Integer location) {
        this.location = location;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((poll == null) ? 0 : poll.hashCode());
        result = prime * result + ((location == null) ? 0 : location.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof PollLocationsId))
            return false;

        PollLocationsId other = (PollLocationsId) obj;

        if (poll == null) {
            if (other.poll != null)
                return false;
        } else if (!poll.equals(other.poll))
            return false;

        if (location == null) {
            return other.location == null;
        } else return location.equals(other.location);
    }
}
