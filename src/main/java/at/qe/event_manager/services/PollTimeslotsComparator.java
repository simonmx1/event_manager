package at.qe.event_manager.services;

import at.qe.event_manager.model.PollTimeslots;

import java.util.Comparator;

/**
 * This class is part of the event manager project which was programmed during the
 * "PS Software Architecture" course in the winter semester 2021/2022 at the University of Innsbruck.
 * 
 * @author Matthias Komar
 * @author Manuel Reichegger
 * @author Simon Muscatello
 * @author Stefan Wagner
 * 
 * Comparator for PollTimeslots, which compares only by points.
 */
public class PollTimeslotsComparator implements Comparator<PollTimeslots> {
    @Override
    public int compare(PollTimeslots pt1, PollTimeslots pt2) {
            return pt1.getPoints().compareTo(pt2.getPoints()) * (-1);
    }
}
