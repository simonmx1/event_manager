package at.qe.event_manager.exceptions;

public class LocationNotFoundException extends RuntimeException{
    public LocationNotFoundException(String msg) {
        super(msg);
    }
}