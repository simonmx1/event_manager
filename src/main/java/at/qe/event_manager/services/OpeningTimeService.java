package at.qe.event_manager.services;

import java.io.Serializable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import at.qe.event_manager.model.OpeningTime;
import at.qe.event_manager.repositories.OpeningTimeRepository;

/**
 * This class is part of the event manager project which was programmed during the
 * "PS Software Architecture" course in the winter semester 2021/2022 at the University of Innsbruck.
 * 
 * @author Matthias Komar
 * @author Manuel Reichegger
 * @author Simon Muscatello
 * @author Stefan Wagner
 * 
 * Service for accessing and manipulating openingTime data.
 */
@Component
@Scope("application")
public class OpeningTimeService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private OpeningTimeRepository openingTimeRepository;

	/**
	 * Deletes the openingTime.
	 *
	 * @param openingTime the openingTime to delete
	 */
	public void deleteOpeningTime(OpeningTime openingTime) {
		openingTimeRepository.delete(openingTime);
	}
	
}
