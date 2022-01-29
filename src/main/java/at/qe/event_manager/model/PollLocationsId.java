package at.qe.event_manager.model;

import java.io.Serializable;

/**
 * This class is part of the event manager project which was programmed during the
 * "PS Software Architecture" course in the winter semester 2021/2022 at the University of Innsbruck.
 * 
 * @author Matthias Komar
 * @author Manuel Reichegger
 * @author Simon Muscatello
 * @author Stefan Wagner
 * 
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
        if (!(obj instanceof PollTimeslotsId))
            return false;

        PollLocationsId other = (PollLocationsId) obj;

        if (poll == null) {
            if (other.poll != null)
                return false;
        } else if (!poll.equals(other.poll))
            return false;

        if (location == null) {
            if (other.location != null)
                return false;
        } else if (!location.equals(other.location))
            return false;

        return true;
    }
}
