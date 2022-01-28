package at.qe.event_manager.services;

import at.qe.event_manager.model.PollLocations;

import java.util.Comparator;

/**
 * Comparator for PollLocation, which compares only by points.
 */
public class PollLocationsComparator implements Comparator<PollLocations> {
    @Override
    public int compare(PollLocations pl1, PollLocations pl2) {
            return pl1.getPoints().compareTo(pl2.getPoints()) * (-1);
    }
}
