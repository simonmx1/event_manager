package at.qe.event_manager.services;

import java.io.Serializable;
import java.util.*;
import at.qe.event_manager.model.*;
import at.qe.event_manager.repositories.EventRepository;
import at.qe.event_manager.repositories.PollRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * Service for accessing and manipulating Event data.
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
	 * Loads all events from the database
	 *
	 * @return a Collection of all events
	 */
	public Collection<Event> getAllEvents() {
		return eventRepository.findAll();
	}

	/**
	 * Loads all events from the database, in which the given user is participant
	 *
	 * @param user the participant from the events returned
	 * @return a Collection of events
	 */
	public Collection<Event> getAllEventFromUser(User user) {
		return eventRepository.findAllByParticipants(user);
	}

	/**
	 * Loads a single event identified by its id.
	 *
	 * @param id the eventId to search for
	 * @return the event with the given id
	 */
	public Event loadEventByEventId(Integer id) {
		return eventRepository.findFirstByEventId(id);
	}

	/**
	 * Saves the given event. This method will also set the event createDate for new
	 * entities.
	 *
	 * @param event the event to save
	 * @return the saved event
	 */
	public Event saveEvent(Event event) {
		if (event.isNew()) {
			event.setCreateDate(new Date());
		}
		return eventRepository.save(event);
	}

	/**
	 * Deletes the event.
	 *
	 * @param event the event to delete
	 */
	@PreAuthorize("hasAuthority('ROLE_ADMIN') or principal.username eq #event.getCreator.getUsername")
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

	/**
	 * Delete policy for the Events, where the given user is participant.
	 * If the given user is creator of the event, the event will be deleted and an email will be sent to all participants.
	 * If the given user is just participant, the user will be deleted from the participants list and it will be checked,
	 * if the participants are greater than 1, otherwise the event will be canceled and the remaining user will be informed
	 * by email.
	 *
	 * @param user the user to delete from participants
	 */
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

	/**
	 * Delete policy for the Events, where the given Location is part of it.
	 * If the event is evaluated, it is in the future and the location in which the event will be held is gets deleted,
	 * the event will be canceled, otherwise if the event wasn't evaluated, the PollLocations with the given location
	 * will be deleted.
	 *
	 * @param location the location to delete from events.
	 */
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

	/**
	 * Delete policy PollLocations, in which the given location is part of it.
	 * The given location, will be deleted from all PollLocation and after it, it will be checked, if there is any location
	 * left for the poll, otherwise all the participants will be informed, that the event can't ve held.
	 *
	 * @param location the location to delete from PollLocations.
	 */
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

	/**
	 * This method evaluates all Polls from the given event.
	 * If there is no Timeslot, in which the event can be held, all users will be informed, that the event can't be held.
	 * Then there are 2 options to evaluate the Polls:
	 * 	- randomize, when at tie: the winner will be evaluated random, either from Locations or Timeslots.
	 * 	- the creator chooses, when at tie: the winner will be evaluated with the Poll from the creator.
	 *
	 * @param event the event to evaluate.
	 */
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
					ArrayList<PollLocations> locationsChosenByCreator = new ArrayList<>(
							pollRepository.findFirstByEventAndUser(event, event.getCreator()).getPollLocations());
					for (PollLocations pl : locationsWithSameMaxPoints) {
						for (PollLocations plCreator : locationsChosenByCreator) {
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
					ArrayList<PollTimeslots> timeslotsChosenByCreator = new ArrayList<>(
							pollRepository.findFirstByEventAndUser(event, event.getCreator()).getPollTimeslots());
					for (PollTimeslots pt : timeslotsWithSameMaxPoints) {
						for (PollTimeslots ptCreator : timeslotsChosenByCreator) {
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

	/**
	 * This method will add the sum of each Location and Timeslot to a List of PollTimeslots with the sum up points
	 * or PollLocations with the sum up points.
	 *
	 * @param event the event to evaluate the Polls.
	 *        locationsWithComputedPoints List of the PollLocations
	 *        timeslotsWithComputedPoints List of the PollTimeslots
	 *
	 */
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
