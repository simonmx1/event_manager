package at.qe.event_manager.ui.controllers;

import at.qe.event_manager.model.Poll;
import at.qe.event_manager.payload.response.MessageResponse;
import java.io.Serializable;
import java.util.Collection;
import at.qe.event_manager.services.EventService;
import at.qe.event_manager.services.PollService;
import at.qe.event_manager.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for the poll list view.
 */
@RestController
@RequestMapping("/api/poll")
public class PollManagementController implements Serializable {

    private static final long serialVersionUID = 1L;

    @Autowired
    private PollService pollService;

    @Autowired
    private EventService eventService;

    @Autowired
    private UserService userService;

    /**
     * Returns a list of all polls.
     *
     * @return
     */

    @GetMapping("/getAll")
    public Collection<Poll> getAll() {
        return pollService.getAllPolls();
    }

    @GetMapping("/get")
    @ResponseBody
    public Poll get(@RequestParam(name = "eventId") Integer eventId, @RequestParam(name = "username") String username) {
        return pollService.loadPollByEventAndUser(eventService.loadEventByEventId(eventId), userService.loadUserByUsername(username));
    }

    @PostMapping("/edit")
    public ResponseEntity<MessageResponse> edit(@RequestBody Poll poll) {
        if (pollService.savePoll(poll) == null) {
            return ResponseEntity.ok(new MessageResponse("Error: Location does not exist!"));
        } else {
            return ResponseEntity.ok(new MessageResponse("Poll submitted successfully!"));
        }
    }
}
