package at.qe.event_manager.ui.controllers;

import at.qe.event_manager.model.Poll;
import at.qe.event_manager.model.PollLocations;
import at.qe.event_manager.payload.request.PollLocationsRequest;
import at.qe.event_manager.payload.response.MessageResponse;
import java.io.Serializable;
import java.util.Collection;

import at.qe.event_manager.services.PollLocationsService;
import at.qe.event_manager.services.PollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * This class is part of the event manager project which was programmed during the
 * "PS Software Architecture" course in the winter semester 2021/2022 at the University of Innsbruck.
 *
 * @author Matthias Komar
 * @author Manuel Reichegger
 * @author Simon Muscatello
 * @author Stefan Wagner
 *
 * Controller which controls the pollLocation management between backend and frontend.
 */
@RestController
@RequestMapping("/api/pollLocations")
public class PollLocationsController implements Serializable {

    private static final long serialVersionUID = 1L;

    @Autowired
    private PollService pollService;

    @Autowired
    private PollLocationsService pollLocationsService;

    @GetMapping("/getAll")
    public Collection<PollLocations> getAll(Poll poll) {
        return pollService.loadPollByPollId(poll.getId()).getPollLocations();
    }

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
