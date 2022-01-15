package at.qe.event_manager.services;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import at.qe.event_manager.model.Event;

@Component
public class SchedulerEventService {
	
	@Autowired
	EventService eventService;
	
	@Scheduled(fixedRate = 5000*60)
	@Async
	public void cronJobSch() throws Exception {
		for(Event event : eventService.getAllEvents()) {
			LocalDateTime currentDateTime = LocalDateTime.now();
			LocalDateTime pollEndDate = LocalDateTime.ofInstant(event.getPollEndDate().toInstant(), ZoneId.systemDefault());
			if(currentDateTime.compareTo(pollEndDate) > 0) {
				eventService.evaluatePolls(event);
			}
		}
	}
}
