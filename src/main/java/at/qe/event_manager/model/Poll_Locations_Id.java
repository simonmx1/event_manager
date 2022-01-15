package at.qe.event_manager.model;

import java.io.Serializable;

public class Poll_Locations_Id implements Serializable {

	private static final long serialVersionUID = 1L;
	
	int poll;
    int location;

    public int getPoll() {
        return poll;
    }

    public void setPoll(int poll) {
        this.poll = poll;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
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
