package at.qe.event_manager.services;

import at.qe.event_manager.model.PollLocations;

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
 * Comparator for PollLocation, which compares only by points.
 */
public class PollLocationsComparator implements Comparator<PollLocations> {
    @Override
    public int compare(PollLocations pl1, PollLocations pl2) {
            return pl1.getPoints().compareTo(pl2.getPoints()) * (-1);
    }
}
