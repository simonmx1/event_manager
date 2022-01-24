package at.qe.event_manager.payload.request;

import at.qe.event_manager.model.OpeningTime;
import at.qe.event_manager.model.Tag;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Time;
import java.time.Instant;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.*;

public class LocationCreationRequest {
    private Integer locationId;
    private String name;
    private String menu;
    private Date createDate;
    private String geolocation;
    private String description;
    boolean enabled;
    private Set<Tag> tags;
    private List<OpeningTime> openingTimes = new ArrayList<>();

    public LocationCreationRequest(Integer locationId, String name, String menu, Date createDate, String geolocation, String description, boolean enabled, Set<Tag> tags, JSONArray openingTimes) {
        this.locationId = locationId;
        this.name = name;
        this.menu = menu;
        this.createDate = createDate;
        this.geolocation = geolocation;
        this.description = description;
        this.enabled = enabled;
        this.tags = tags;
        formatOpeningTimes(openingTimes, this.openingTimes);
    }
    private void formatOpeningTimes (JSONArray openingTimes, List<OpeningTime> list) {
        for (int i = 0; i < openingTimes.length(); i++) {
            JSONObject object = openingTimes.getJSONObject(i);
            if (object.getBoolean("enabled")) {
                OpeningTime ot = new OpeningTime();
                ot.setWeekday(DayOfWeek.valueOf(object.getString("day")).getValue());
                ot.setStart(Time.valueOf(LocalTime.parse(object.getJSONArray("openingTimes").getJSONObject(0).getString("start"))));
                ot.setEnd(Time.valueOf(LocalTime.parse(object.getJSONArray("openingTimes").getJSONObject(0).getString("end"))));
                ot.setCreateDate(new Date());
                list.add(ot);
            }
        }
    }
    public enum DayOfWeek {
        Monday(0), Tuesday(1), Wednesday(2), Thursday(3), Friday(4), Saturday(5), Sunday(6);

        private final int value;

        DayOfWeek(int value) {
            this.value = value;
        }
        public int getValue() {
            return value;
        }
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public List<OpeningTime> getOpeningTimes() {
        return openingTimes;
    }

    public void setOpeningTimes(List<OpeningTime> openingTimes) {
        this.openingTimes = openingTimes;
    }
}
