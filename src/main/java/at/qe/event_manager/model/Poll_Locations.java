package at.qe.event_manager.model;

import javax.persistence.*;

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

    private Integer rankWeight;

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

    public Integer getRankWeight() {
        return rankWeight;
    }

    public void setRankWeight(Integer rankWeight) {
        this.rankWeight = rankWeight;
    }
}