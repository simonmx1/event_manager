package at.qe.event_manager.model;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;

@Entity
public class Timeslot implements Persistable<Integer>, Serializable, Comparable<Timeslot> {
    @Id
    @Column(name = "timeslot_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer timeslotId;

    private Timestamp start;
    private Timestamp end;

    @OneToMany (mappedBy = "timeslot")
    private Set<Poll_Timeslots> poll_timeslots;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    public Timestamp getStart() {
        return start;
    }

    public void setStart(Timestamp start) {
        this.start = start;
    }

    public Timestamp getEnd() {
        return end;
    }

    public void setEnd(Timestamp end) {
        this.end = end;
    }

    public Integer getTimeslotId() {
        return timeslotId;
    }

    public void setTimeslotId(Integer timeslotId) {
        this.timeslotId = timeslotId;
    }

    @Override
    public int compareTo(@NotNull Timeslot timeslot) {
        return this.timeslotId.compareTo(timeslot.timeslotId);
    }

    @Override
    public Integer getId() {
        return timeslotId;
    }

    @Override
    public boolean isNew() {
        return createDate == null;
    }
}
