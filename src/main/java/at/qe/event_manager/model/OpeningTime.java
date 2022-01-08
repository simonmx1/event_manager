package at.qe.event_manager.model;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.bytebuddy.build.ToStringPlugin;
import org.springframework.data.domain.Persistable;

import static org.eclipse.jdt.internal.compiler.codegen.ConstantPool.ToString;

/**
 * Entity representing users.
 * <p>
 * This class is part of the skeleton project provided for students of the
 * courses "Software Architecture" and "Software Engineering" offered by the
 * University of Innsbruck.
 */

@Entity
public class OpeningTime implements Persistable<Integer>, Serializable, Comparable<OpeningTime> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(length = 100)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer openingTimeId;

    private Time start;
    private Time end;


    private int weekday; //monday=0 to sunday=6

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @ManyToOne
    @JoinColumn(name = "location_id")
    @JsonIgnore
    private Location location;

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

    public Integer getOpeningTimeId() {
        return openingTimeId;
    }

    public void setOpeningTimeId(Integer openingTimeId) {
        openingTimeId = openingTimeId;
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
        return this.openingTimeId.compareTo(o.getOpeningTimeId());
    }

    @Override
    public Integer getId() {
        return getOpeningTimeId();
    }

    @Override
    public boolean isNew() {
        return (null == createDate);
    }

}