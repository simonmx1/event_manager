package at.qe.event_manager.model;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.domain.Persistable;


@Entity
@OnDelete(action = OnDeleteAction.CASCADE)
public class OpeningTime implements Persistable<Integer>, Serializable, Comparable<OpeningTime> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer openingTimeId;

    private Time start;
    private Time end;

    @ManyToOne
    @JoinColumn(name = "locationId")
    @JsonIgnore
    private Location location;

    private int weekday; //monday=0 to sunday=6

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Time getStart() {
        return start;
    }

    public void setStart(Time start) {
        this.start = start;
    }

    public Time getEnd() {
        return end;
    }

    public void setEnd(Time end) {
        this.end = end;
    }

    public void setOpeningTimeId(Integer openingTimeId) {
        this.openingTimeId = openingTimeId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public int getWeekday() {
        return weekday;
    }

    public void setWeekday(int weekday) {
        this.weekday = weekday;
    }

    @Override
    public int compareTo(OpeningTime o) {
        return this.openingTimeId.compareTo(o.getId());
    }

    @Override
    public boolean equals(Object openingTime) {
        if (!(openingTime instanceof OpeningTime)) return false;
        return this.openingTimeId.compareTo(((OpeningTime) openingTime).getId()) == 0;
    }

    @Override
    public Integer getId() {
        return openingTimeId;
    }

    @Override
    public boolean isNew() {
        return (null == createDate);
    }

}