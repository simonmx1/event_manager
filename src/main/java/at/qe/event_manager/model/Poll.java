package at.qe.event_manager.model;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
public class Poll implements Persistable<Integer>, Serializable, Comparable<Poll> {

	private static final long serialVersionUID = 1L;

	@Id
    @Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pollId;


    @OneToMany (mappedBy = "poll", fetch=FetchType.EAGER)
    private Set<PollTimeslots> pollTimeslots;

    @OneToMany (mappedBy = "poll", fetch=FetchType.EAGER)
    private Set<PollLocations> pollLocations;

    @ManyToOne
    private User user;

    @ManyToOne
    private Event event;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    public void setPollId(Integer pollId) {
        this.pollId = pollId;
    }

    public Set<PollTimeslots> getPollTimeslots() {
        return pollTimeslots;
    }

    public void setPollTimeslots(Set<PollTimeslots> pollTimeslots) {
        this.pollTimeslots = pollTimeslots;
    }

    public Set<PollLocations> getPollLocations() {
        return pollLocations;
    }

    public void setPollLocations(Set<PollLocations> pollLocations) {
        this.pollLocations = pollLocations;
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
        return this.pollId.compareTo(poll.getId());
    }
    
    @Override
    public boolean equals(Object poll) {
    	if(!(poll instanceof Poll)) return false;
    	return this.pollId.compareTo(((Poll)poll).getId()) == 0;
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
