package at.qe.event_manager.exceptions;

public class LocationNotFoundException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public LocationNotFoundException(String msg) {
        super(msg);
    }
}