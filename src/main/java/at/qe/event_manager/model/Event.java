package at.qe.event_manager.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
public class Event implements Persistable<Integer>, Serializable, Comparable<Event> {

	private static final long serialVersionUID = 1L;

	@Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer eventId;

    private String name;
    @ManyToOne
    @JoinColumn(name = "timeslotTimeslotId")
    private Timeslot timeslot;
    @ManyToOne
    @JoinColumn(name = "locationLocationId")
    private Location location;

    @ManyToOne
    @JoinColumn(name = "creatorUsername")
    private User creator;

    @ManyToMany
    private Set<User> participants;

    @OneToMany(mappedBy = "event", fetch=FetchType.EAGER)
    @JsonIgnore
    private Set<Poll> polls;

    private Date pollEndDate;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    
    boolean creatorIsPreferred = false;
    boolean isEvaluated = false;

    public Location getLocation() {
        return location;
    }

    public Timeslot getTimeslot() {
        return timeslot;
    }

    public Integer getId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public Integer getEventId() {
        return eventId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTimeslot(Timeslot timeslot) {
        this.timeslot = timeslot;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public User getCreator() {
        return creator;
    }

    public Set<User> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<User> participants) {
        this.participants = participants;
    }

    public Set<Poll> getPolls() {
        return polls;
    }

    public Date getPollEndDate() {
        return pollEndDate;
    }

    public void setPollEndDate(Date pollEndDate) {
        this.pollEndDate = pollEndDate;
    }

    public void setPolls(Set<Poll> polls) {
        this.polls = polls;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public boolean isCreatorIsPreferred() {
        return creatorIsPreferred;
    }

    public void setCreatorIsPreferred(boolean creatorIsPreferred) {
        this.creatorIsPreferred = creatorIsPreferred;
    }

    public boolean isEvaluated() {
        return isEvaluated;
    }

    public void setEvaluated(boolean evaluated) {
        isEvaluated = evaluated;
    }

    @Override
    public int compareTo(Event event) {
        return this.eventId.compareTo(event.eventId);
    }

    @Override
    public boolean isNew() {
        return (createDate == null);
    }

}
