package at.qe.event_manager.services;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import at.qe.event_manager.model.Event;

/**
 * This class is part of the event manager project which was programmed during the
 * "PS Software Architecture" course in the winter semester 2021/2022 at the University of Innsbruck.
 * 
 * @author Matthias Komar
 * @author Manuel Reichegger
 * @author Simon Muscatello
 * @author Stefan Wagner
 * 
 * The Scheduler looks up every 5 minutes for events which PollEndTime is in the Past and sends them to evaluation.
 */

@Component
public class SchedulerEventService {
	
	@Autowired
	EventService eventService;
	
	private static boolean enabled = true;
	
	public static void disable() {
		enabled = false;
	}
	
	/**
     * Scheduler which decides if an event has to be evaluated
     */
	@Scheduled(fixedRate = 5000*60)
	@Async
	public void scheduleEventEvaluation() {
		if(enabled) {
			for(Event event : eventService.getAllEvents()) {
				if (!event.isEvaluated()) {
					LocalDateTime currentDateTime = LocalDateTime.now();
					LocalDateTime pollEndDate = LocalDateTime.ofInstant(event.getPollEndDate().toInstant(), ZoneId.systemDefault());
					if (currentDateTime.compareTo(pollEndDate) > 0) {
						eventService.evaluatePolls(event);
					}
				}
			}
		}
	}
}
