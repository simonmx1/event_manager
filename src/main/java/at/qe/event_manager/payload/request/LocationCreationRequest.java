package at.qe.event_manager.payload.request;

import at.qe.event_manager.model.OpeningTime;
import at.qe.event_manager.model.Tag;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;
import java.sql.Time;
import java.time.LocalTime;
import java.util.*;

/**
 * This class is part of the event manager project which was programmed during the
 * "PS Software Architecture" course in the winter semester 2021/2022 at the University of Innsbruck.
 * 
 * @author Matthias Komar
 * @author Manuel Reichegger
 * @author Simon Muscatello
 * @author Stefan Wagner
 * 
 * This class represents an location creation request from the frontend.
 */
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
                JSONArray openingTimesPerDay = object.getJSONArray("openingTimes");
                for(int j = 0; j < openingTimesPerDay.length(); j++) {
                	OpeningTime ot = new OpeningTime();
                    ot.setWeekday(DayOfWeek.valueOf(object.getString("day")).getValue());
                    ot.setStart(Time.valueOf(LocalTime.parse(openingTimesPerDay.getJSONObject(j).getString("start"))));
                    ot.setEnd(Time.valueOf(LocalTime.parse(openingTimesPerDay.getJSONObject(j).getString("end"))));
                    ot.setCreateDate(new Date());
                    list.add(ot);
                }
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

    public String getName() {
        return name;
    }

    public String getMenu() {
        return menu;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public String getGeolocation() {
        return geolocation;
    }

    public String getDescription() {
        return description;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public List<OpeningTime> getOpeningTimes() {
        return openingTimes;
    }
}
