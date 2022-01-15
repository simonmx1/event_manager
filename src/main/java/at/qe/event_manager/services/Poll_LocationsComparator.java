package at.qe.event_manager.services;

import at.qe.event_manager.model.Poll_Locations;

import java.util.Comparator;

public class Poll_LocationsComparator implements Comparator<Poll_Locations> {
    @Override
    public int compare(Poll_Locations pl1, Poll_Locations pl2) {
            return pl1.getPoints().compareTo(pl2.getPoints()) * (-1);
    }
}
