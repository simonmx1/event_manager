package at.qe.event_manager.model;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.domain.Persistable;

/**
 * This class is part of the event manager project which was programmed during the
 * "PS Software Architecture" course in the winter semester 2021/2022 at the University of Innsbruck.
 * 
 * @author Matthias Komar
 * @author Manuel Reichegger
 * @author Simon Muscatello
 * @author Stefan Wagner
 * 
 * Entity representing openingTimes.
 * <p>
 * This class models a openingTime with the weekday, opening and closing.
 */
@Entity
@OnDelete(action = OnDeleteAction.CASCADE)
public class OpeningTime implements Persistable<Integer>, Serializable, Comparable<OpeningTime> {

    private static final long serialVersionUID = 1L;

    /** The ID of the openingTime (primary-key)*/
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer openingTimeId;

    /** The time it opens */
    private Time start;

    /** The time it close */
    private Time end;

    /** The location for which the openingTime applies*/
    @ManyToOne
    @JoinColumn(name = "locationId")
    @JsonIgnore
    private Location location;

    /** The weekday of the openingTime<br>
     * monday - 0 ... sunday - 6
     */
    private int weekday;

    /** The date on which the openingTime is created */
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

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}