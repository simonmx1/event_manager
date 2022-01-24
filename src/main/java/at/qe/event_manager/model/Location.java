package at.qe.event_manager.model;

import java.io.Serializable;
import java.util.*;
import javax.persistence.*;

import at.qe.event_manager.payload.request.LocationCreationRequest;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.domain.Persistable;

/**
 * Entity representing location.
 * <p>
 * This class models a location with the information about menu, geolocation, description and tags.
 */
@Entity
public class Location implements Persistable<Integer>, Serializable, Comparable<Location> {

    private static final long serialVersionUID = 1L;

    /** The Id of the location (primary-key) */
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer locationId;

    /** The name of the location */
    private String name;

    /** The link to the menu */
    private String menu;

    /** The date on which the location is created */
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    /** The link to the location on GoogleMaps */
    private String geolocation;

    /** The description of the event */
    private String description;

    /** Is the location enabled */
    boolean enabled = true;

    /** Tags which describe the location more detailed */
    @ManyToMany
    @JoinTable(name = "locationTags")
    private Set<Tag> tags;

    /** The openingTimes of the location every weekday */
    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL)
    private List<OpeningTime> openingTimes;

    public Location() {}

    public Location (LocationCreationRequest location) {
        this.name = location.getName();
        this.menu = location.getMenu();
        this.createDate = location.getCreateDate();
        this.geolocation = location.getGeolocation();
        this.description = location.getDescription();
        this.enabled = location.isEnabled();
        this.tags = location.getTags();
        this.openingTimes = location.getOpeningTimes();

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<OpeningTime> getOpeningTimes() {
        return openingTimes;
    }

    public void setOpeningTimes(List<OpeningTime> openingTimes) {
        this.openingTimes = openingTimes;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
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
    public boolean equals(Object location) {
        if (!(location instanceof Location)) return false;
        return this.locationId.compareTo(((Location) location).getId()) == 0;
    }

    @Override
    public Integer getId() {
        return getLocationId();
    }

    @Override
    public boolean isNew() {
        return (null == createDate);
    }
}
