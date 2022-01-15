package at.qe.event_manager.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;
@Entity
public class Poll implements Persistable<Integer>, Serializable, Comparable<Poll> {

	private static final long serialVersionUID = 1L;

	@Id
    @Column(name = "poll_id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pollId;


    @OneToMany (mappedBy = "poll")
    private Set<Poll_Timeslots> poll_timeslots;

    @OneToMany (mappedBy = "poll")
    private Set<Poll_Locations> poll_locations;

    @ManyToOne
    @JoinColumn(name = "user_username")
    private User user;

    @ManyToOne
    @JoinColumn(name = "event_event_id")
    @JsonIgnore
    private Event event;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    public Poll() {

    }

    public Integer getPollId() {
        return pollId;
    }

    public void setPollId(Integer pollId) {
        this.pollId = pollId;
    }

    public Set<Poll_Timeslots> getPoll_timeslots() {
        return poll_timeslots;
    }

    public void setPoll_timeslots(Set<Poll_Timeslots> poll_timeslots) {
        this.poll_timeslots = poll_timeslots;
    }

    public Set<Poll_Locations> getPoll_locations() {
        return poll_locations;
    }

    public void setPoll_locations(Set<Poll_Locations> poll_locations) {
        this.poll_locations = poll_locations;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public int compareTo(@NotNull Poll poll) {
        return this.pollId.compareTo(poll.pollId);
    }

    @Override
    public Integer getId() {
        return pollId;
    }

    @Override
    public boolean isNew() {
        return createDate == null;
    }
}
