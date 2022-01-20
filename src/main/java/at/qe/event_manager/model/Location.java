package at.qe.event_manager.model;

import java.io.Serializable;
import java.util.*;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.domain.Persistable;

/**
 * Entity representing users.
 * <p>
 * This class is part of the skeleton project provided for students of the
 * courses "Software Architecture" and "Software Engineering" offered by the
 * University of Innsbruck.
 */
@Entity
@OnDelete(action = OnDeleteAction.CASCADE)
public class Location implements Persistable<Integer>, Serializable, Comparable<Location> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer locationId;

    private String name;
    private String menu;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    private String geolocation;

    private String description;

    boolean enabled = true;

    @ManyToMany
    @JoinTable(name = "locationTags")
    private Set<Tag> tags;

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL)
    private List<OpeningTime> openingTimes;

    /*@OneToMany (mappedBy = "location", fetch=FetchType.EAGER)
    @JsonIgnore
    private Set<PollLocations> pollLocations;*/

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
    	if(!(location instanceof Location)) return false;
    	return this.locationId.compareTo(((Location)location).getId()) == 0;
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
