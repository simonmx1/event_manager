package at.qe.event_manager.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.domain.Persistable;
import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;

@Entity
public class Timeslot implements Persistable<Integer>, Serializable, Comparable<Timeslot> {
	
	private static final long serialVersionUID = 1L;

	@Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer timeslotId;

    private Timestamp start;
    private Timestamp end;

   /* @OneToMany (mappedBy = "timeslot")
    @JsonIgnore
    private Set<PollTimeslots> pollTimeslots;*/

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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public int compareTo(Timeslot timeslot) {
        return this.timeslotId.compareTo(timeslot.getId());
    }
    
    @Override
    public boolean equals(Object timeslot) {
    	if(!(timeslot instanceof Timeslot)) return false;
    	return this.timeslotId.compareTo(((Timeslot)timeslot).getId()) == 0;
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
