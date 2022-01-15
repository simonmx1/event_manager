package at.qe.event_manager.model;

import javax.persistence.*;

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

    private Integer rankWeight;

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

    public Integer getRankWeight() {
        return rankWeight;
    }

    public void setRankWeight(Integer rankWeight) {
        this.rankWeight = rankWeight;
    }
}