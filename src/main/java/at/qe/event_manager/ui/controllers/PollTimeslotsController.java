package at.qe.event_manager.ui.controllers;

import at.qe.event_manager.model.PollTimeslots;
import at.qe.event_manager.payload.request.PollTimeslotsRequest;
import at.qe.event_manager.payload.response.MessageResponse;
import java.io.Serializable;
import java.util.Collection;

import at.qe.event_manager.services.PollTimeslotsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for the user list view.
 */
@RestController
@RequestMapping("/api/pollTimeslots")
public class PollTimeslotsController implements Serializable {

    private static final long serialVersionUID = 1L;

    @Autowired
    private PollTimeslotsService pollTimeslotsService;

    /**
     * Returns a list of all users.
     *
     * @return
     */

    @GetMapping("/getAll")
    public Collection<PollTimeslots> getAll() {
        return pollTimeslotsService.getAllPollTimeslots();
    }

    @PostMapping("/edit")
    public ResponseEntity<MessageResponse> edit(@RequestBody PollTimeslotsRequest pt) {
        PollTimeslots pollTimeslots = pollTimeslotsService.get(pt.getPollId(), pt.getTimeslotId());
        pollTimeslots.setPoints(pt.getPoints());
        if(pollTimeslotsService.savePollTimeslots(pollTimeslots) == null) {
            return ResponseEntity.ok(new MessageResponse("Error: Poll does not exist!"));
        } else {
            return ResponseEntity.ok(new MessageResponse("Poll submitted successfully!"));
        }
    }
}
