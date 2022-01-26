package at.qe.event_manager.services;

import java.io.Serializable;
import java.util.*;
import at.qe.event_manager.model.*;
import at.qe.event_manager.repositories.EventRepository;
import at.qe.event_manager.repositories.PollRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Service for accessing and manipulating user data.
 * <p>
 * This class is part of the skeleton project provided for students of the
 * courses "Software Architecture" and "Software Engineering" offered by the
 * University of Innsbruck.
 */
@Component
@Scope("application")
public class EventService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private EventRepository eventRepository;

	@Autowired
	private PollRepository pollRepository;

	@Autowired
	private PollService pollService;
	
	@Autowired
	private PollLocationsService pollLocationsService;

	/**
	 * Returns a collection of all users.
	 *
	 * @return
	 */
	public Collection<Event> getAllEvents() {
		return eventRepository.findAll();
	}

	public Collection<Event> getAllEventFromUser(User user) {
		return eventRepository.findAllByParticipants(user);
	}

	/**
	 * Loads a single user identified by its username.
	 *
	 * @param username the username to search for
	 * @return the user with the given username
	 */
	public Event loadEvent(Integer id) {
		return eventRepository.findFirstByEventId(id);
	}

	public Event saveEvent(Event event) {
		if (event.isNew()) {
			event.setCreateDate(new Date());
		}
		return eventRepository.save(event);
	}

	public Event createEvent(Event event) {
		return saveEvent(event);
	}

	/**
	 * Deletes the user.
	 *
	 * @param user the user to delete
	 */
	public void deleteEvent(Event event) {
		deleteEventWrapper(event, true);
	}
	
	private void deleteEventWrapper(Event event, boolean sendEventDeletionMessage) {
		if (sendEventDeletionMessage && (!event.isEvaluated() || (event.getTimeslot() != null && event.getTimeslot().getStart().compareTo(new Date()) > 0))) {
			// send Mail that event is being deleted
			for(User participant : event.getParticipants()) {
				MailService.sendEventDeletionMessage(participant, event);
			}
		}
		if (!event.getPolls().isEmpty()) {
			event.getPolls().forEach(p -> pollService.deletePoll(p));
		}
		eventRepository.delete(event);
	}

	public void cleanUpForParticipantDeletion(User user) {
		// Delete Policy for User in Events
		for (Event event : getAllEvents()) {
			if (event.getCreator().getUsername().compareTo(user.getUsername()) == 0) {
				if (!event.isEvaluated() || (event.getTimeslot() != null && event.getTimeslot().getStart().compareTo(new Date()) > 0)) {
					for(User participant : event.getParticipants()) {
						if(user.compareTo(participant) != 0) {
							// send Mail that creator has been deleted
							MailService.sendEventCreatorDeletionMessage(participant, event);
						}
					}
				}
				deleteEventWrapper(event, false);
			} else {
				Set<User> participants = event.getParticipants();
				if (participants.contains(user)) {
					participants.remove(user);
					if (participants.size() < 2) {
						if (!event.isEvaluated() || (event.getTimeslot() != null && event.getTimeslot().getStart().compareTo(new Date()) > 0)) {
							// send Mail that participants = 1 -> to last user -> event is being canceld
							for(User participant : participants) {
								MailService.sendEventNotEnoughParticipantsMessage(participant, event, user);
							}
						}
						deleteEventWrapper(event, false);
					}
				}
			}
		}
		pollService.cleanUpForParticipantDeletion(user);
	}

	public void cleanUpForLocationDeletion(Location location) {
    	for(Event event : getAllEvents()) {
    		if(event.isEvaluated() && event.getLocation().compareTo(location) == 0) {
    			if(event.getTimeslot().getStart().compareTo(new Date()) > 0) {
    				// send mail that event can't be held, because Location was deleted
    				for(User participant : event.getParticipants()) {
    					MailService.sendEventLocationDeletionMessage(participant, event, location);
    				}
    			}
    			deleteEventWrapper(event, false);
    		}
    	}
		cleanUpPollsForLocationDeletion(location);
	}
	
	private void cleanUpPollsForLocationDeletion(Location location) {
    	// Delete Policy for Location in Polls
    	for(Poll poll : pollService.getAllPolls()) {
    		Set<PollLocations> pollLocations = poll.getPollLocations();
    		Iterator<PollLocations> iterator = pollLocations.iterator();
    		while(iterator.hasNext()) {
    			PollLocations pollLocation = iterator.next();
    			if(pollLocation.getLocation().compareTo(location) == 0) {
    				iterator.remove();
    				pollLocationsService.deletePollLocations(pollLocation);
    			}
    		}
    		if (pollLocations.isEmpty()) {
    			Event event = poll.getEvent();
    			if (!event.isEvaluated()) {
					// send Mail that Event can't be held because pollLocation size = 0
    				for(User participant : event.getParticipants()) {
    					MailService.sendEventNotEnoughLocationsMessage(participant, event);
    				}
				}
    			deleteEventWrapper(event, false);
    		}
    	}
    }

	public void evaluatePolls(Event event) {
		ArrayList<PollLocations> locationsWithComputedPoints = new ArrayList<>();
		ArrayList<PollTimeslots> timeslotsWithComputedPoints = new ArrayList<>();
		computePointsOfPolls(event, locationsWithComputedPoints, timeslotsWithComputedPoints);
		Comparator<PollLocations> pollLocationsComparator = new PollLocationsComparator();
		Comparator<PollTimeslots> pollTimeslotsComparator = new PollTimeslotsComparator();
		locationsWithComputedPoints.sort(pollLocationsComparator);
		timeslotsWithComputedPoints.sort(pollTimeslotsComparator);
		if (timeslotsWithComputedPoints.get(0).getPoints() == 0) {
			// send email to participants, event is evaluated but will not be held
			for(User participant : event.getParticipants()) {
				MailService.sendEventNoCompatibleTimeslotAvailableMessage(participant, event);
			}
			deleteEventWrapper(event, false);
		} else {
			if (locationsWithComputedPoints.size() > 1 && pollLocationsComparator
					.compare(locationsWithComputedPoints.get(0), locationsWithComputedPoints.get(1)) == 0) {
				ArrayList<PollLocations> locationsWithSameMaxPoints = new ArrayList<>();
				for (PollLocations pollLocation : locationsWithComputedPoints) {
					if (pollLocationsComparator.compare(pollLocation, locationsWithComputedPoints.get(0)) == 0) {
						locationsWithSameMaxPoints.add(pollLocation);
					}
				}
				if (event.isCreatorIsPreferred()) {
					ArrayList<PollLocations> locationsChoosenByCreator = new ArrayList<>(
							pollRepository.findFirstByEventAndUser(event, event.getCreator()).getPollLocations());
					for (PollLocations pl : locationsWithSameMaxPoints) {
						for (PollLocations plCreator : locationsChoosenByCreator) {
							if (pl.equals(plCreator)) {
								locationsWithSameMaxPoints.set(locationsWithSameMaxPoints.indexOf(pl), plCreator);
							}
						}
					}
					locationsWithSameMaxPoints.sort(pollLocationsComparator);
					event.setLocation(locationsWithSameMaxPoints.get(0).getLocation());
				} else {
					Random random = new Random();
					int min = 0;
					int max = locationsWithSameMaxPoints.size() - 1;
					int index = random.nextInt(max + min) + min;
					event.setLocation(locationsWithSameMaxPoints.get(index).getLocation());
				}
			} else {
				event.setLocation(locationsWithComputedPoints.get(0).getLocation());
			}
			if (timeslotsWithComputedPoints.size() > 1 && timeslotsWithComputedPoints.get(0)
					.getPoints().compareTo(timeslotsWithComputedPoints.get(1).getPoints()) == 0) {
				ArrayList<PollTimeslots> timeslotsWithSameMaxPoints = new ArrayList<>();
				for (PollTimeslots pollTimeslot : timeslotsWithComputedPoints) {
					if (pollTimeslotsComparator.compare(pollTimeslot, timeslotsWithComputedPoints.get(0)) == 0) {
						timeslotsWithSameMaxPoints.add(pollTimeslot);
					}
				}
				if (event.isCreatorIsPreferred()) {
					ArrayList<PollTimeslots> timeslotsChoosenByCreator = new ArrayList<>(
							pollRepository.findFirstByEventAndUser(event, event.getCreator()).getPollTimeslots());
					for (PollTimeslots pt : timeslotsWithSameMaxPoints) {
						for (PollTimeslots ptCreator : timeslotsChoosenByCreator) {
							if (pt.equals(ptCreator)) {
								timeslotsWithSameMaxPoints.set(timeslotsWithSameMaxPoints.indexOf(pt), ptCreator);
							}
						}
					}
					timeslotsWithSameMaxPoints.sort(pollTimeslotsComparator);
					event.setTimeslot(timeslotsWithSameMaxPoints.get(0).getTimeslot());
				} else {
					Random random = new Random();
					int min = 0;
					int max = timeslotsWithSameMaxPoints.size() - 1;
					int index = random.nextInt(max + min) + min;
					event.setTimeslot(timeslotsWithSameMaxPoints.get(index).getTimeslot());
				}
			} else {
				event.setTimeslot(timeslotsWithComputedPoints.get(0).getTimeslot());
			}
			event.setEvaluated(true);
			event.getParticipants().forEach(user -> MailService.sendEventEvaluationMessage(user, event));
			eventRepository.save(event);
		}
	}

	private void computePointsOfPolls(Event event, List<PollLocations> locationsWithComputedPoints,
			List<PollTimeslots> timeslotsWithComputedPoints) {
		for (Poll poll : event.getPolls()) {
			for (PollLocations pollLocation : poll.getPollLocations()) {
				if (!locationsWithComputedPoints.contains(pollLocation)) {
					locationsWithComputedPoints.add(new PollLocations(pollLocation));
				} else {
					locationsWithComputedPoints.get(locationsWithComputedPoints.indexOf(pollLocation))
							.addPoints(pollLocation);
				}
			}
			for (PollTimeslots pollTimeslot : poll.getPollTimeslots()) {
				if (!timeslotsWithComputedPoints.contains(pollTimeslot)) {
					timeslotsWithComputedPoints.add(new PollTimeslots(pollTimeslot));
				} else if (timeslotsWithComputedPoints.get(timeslotsWithComputedPoints.indexOf(pollTimeslot))
						.getPoints() == 0 || pollTimeslot.getPoints() == 0) {
					timeslotsWithComputedPoints.get(timeslotsWithComputedPoints.indexOf(pollTimeslot)).setPoints(0);
				} else {
					timeslotsWithComputedPoints.get(timeslotsWithComputedPoints.indexOf(pollTimeslot))
							.addPoints(pollTimeslot);
				}
			}
		}
	}
}
