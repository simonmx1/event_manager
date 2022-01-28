package at.qe.event_manager.services;

import at.qe.event_manager.model.PollTimeslots;

import java.util.Comparator;

/**
 * Comparator for PollTimeslots, which compares only by points.
 */
public class PollTimeslotsComparator implements Comparator<PollTimeslots> {
    @Override
    public int compare(PollTimeslots pt1, PollTimeslots pt2) {
            return pt1.getPoints().compareTo(pt2.getPoints()) * (-1);
    }
}
