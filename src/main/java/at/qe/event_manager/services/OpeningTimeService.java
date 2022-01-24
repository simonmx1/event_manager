package at.qe.event_manager.services;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import at.qe.event_manager.model.OpeningTime;
import at.qe.event_manager.repositories.OpeningTimeRepository;

@Component
@Scope("application")
public class OpeningTimeService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private OpeningTimeRepository openingTimeRepository;
	
	public OpeningTime loadOpeningTimeByOpeningTimeId(Integer openingTimeId) {
		return openingTimeRepository.findFirstByOpeningTimeId(openingTimeId);
	}
	
	public void deleteOpeningTime(OpeningTime openingTime) {
		openingTimeRepository.delete(openingTime);
	}
	
}
