package at.qe.event_manager.exceptions;

/**
 * This class is part of the event manager project which was programmed during the
 * "PS Software Architecture" course in the winter semester 2021/2022 at the University of Innsbruck.
 * 
 * @author Matthias Komar
 * @author Manuel Reichegger
 * @author Simon Muscatello
 * @author Stefan Wagner
 * 
 * If a user tries to log in with an incorrect username or password, this exception will be thrown.
 */

public class AuthenticationException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;

	public AuthenticationException(String msg) {
        super(msg);
    }
}
