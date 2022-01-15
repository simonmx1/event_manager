package at.qe.event_manager.model;

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
    private Integer pollId;

    @ManyToMany
    private Set<Location> locations;

    @ManyToMany
    private Set<Timeslot> timeslots;

    @ManyToOne
    @JoinColumn(name = "user_username")
    private User user;

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

    @Override
    public int compareTo(@NotNull Poll poll) {
        return this.pollId.compareTo(poll.pollId);
    }

    @Override
    public Integer getId() {
        return null;
    }

    @Override
    public boolean isNew() {
        return createDate == null;
    }
}
