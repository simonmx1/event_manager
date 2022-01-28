package at.qe.event_manager.ui.controllers;

import at.qe.event_manager.model.PollLocations;
import at.qe.event_manager.payload.request.PollLocationsRequest;
import at.qe.event_manager.payload.response.MessageResponse;
import java.io.Serializable;
import at.qe.event_manager.services.PollLocationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for the user list view.
 */
@RestController
@RequestMapping("/api/pollLocations")
public class PollLocationsController implements Serializable {

    private static final long serialVersionUID = 1L;

    @Autowired
    private PollLocationsService pollLocationsService;

    /**
     * Returns a list of all users.
     *
     * @return
     */

    @PostMapping("/edit")
    public ResponseEntity<MessageResponse> edit(@RequestBody PollLocationsRequest pl) {
        PollLocations pollLocations = pollLocationsService.loadPollLocation(pl.getPollId(), pl.getLocationId());
        pollLocations.setPoints(pl.getPoints());
        if(pollLocationsService.savePollLocations(pollLocations) == null) {
            return ResponseEntity.ok(new MessageResponse("Error: Poll does not exist!"));
        } else {
            return ResponseEntity.ok(new MessageResponse("Poll submitted successfully!"));
        }
    }
}
