package at.qe.event_manager.services;

import at.qe.event_manager.model.Poll_Locations;
import at.qe.event_manager.model.Poll_Timeslots;

import java.util.Comparator;

public class Poll_TimsSlotsComparator implements Comparator<Poll_Timeslots> {
    @Override
    public int compare(Poll_Timeslots pt1, Poll_Timeslots pt2) {
            return pt1.getPoints().compareTo(pt2.getPoints()) * (-1);
    }
}
