package at.qe.event_manager.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * Entity representing events.
 * <p>
 * This class models an event with participants and polls
 */
@Entity
public class Event implements Persistable<Integer>, Serializable, Comparable<Event> {

    private static final long serialVersionUID = 1L;

    /** ID of the Event (primary-key)*/
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer eventId;

    /** Name of the Event */
    private String name;

    /** The final voted timeslot */
    @ManyToOne
    private Timeslot timeslot;

    /** The final voted location */
    @ManyToOne
    private Location location;

    /** The user who creates the event */
    @ManyToOne
    @JoinColumn(name = "creatorUsername")
    private User creator;

    /** The participants of the event witch can poll*/
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<User> participants;

    /** The polls of every participant */
    @OneToMany(mappedBy = "event", fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<Poll> polls;

    /** The date from which voting will no longer take place and the result will be evaluated */
    private Date pollEndDate;

    /** The date on which the event is created */
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    /** Decides in case of a tie <br>
     *  true  -> creator <br>
     *  false -> random */
    boolean creatorIsPreferred = false;

    /** Is the event enabled */
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
        return this.eventId.compareTo(event.getId());
    }

    @Override
    public boolean equals(Object event) {
        if (!(event instanceof Event)) return false;
        return this.eventId.compareTo(((Event) event).getId()) == 0;
    }

    @Override
    public boolean isNew() {
        return (createDate == null);
    }

}
