package at.qe.event_manager.model;

import java.io.Serializable;
import java.util.*;
import javax.persistence.*;

import org.springframework.data.domain.Persistable;

/**
 * Entity representing users.
 * <p>
 * This class is part of the skeleton project provided for students of the
 * courses "Software Architecture" and "Software Engineering" offered by the
 * University of Innsbruck.
 */
@Entity
public class Location implements Persistable<Integer>, Serializable, Comparable<Location> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(length = 100)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer locationId;
    private String name;
    private String menu;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    private String geolocation;

    boolean enabled = true;

    @ManyToMany
    @JoinTable(name = "location_tags")
    private Set<Tag> tags;


    @OneToMany (mappedBy = "location", fetch=FetchType.EAGER)
    private Set<Poll_Locations> poll_locations;

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getGeolocation() {
        return geolocation;
    }

    public void setGeolocation(String geolocation) {
        this.geolocation = geolocation;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public int compareTo(Location o) {
        return this.locationId.compareTo(o.getId());
    }

    @Override
    public Integer getId() {
        return locationId;
    }

    @Override
    public boolean isNew() {
        return (null == createDate);
    }
}
