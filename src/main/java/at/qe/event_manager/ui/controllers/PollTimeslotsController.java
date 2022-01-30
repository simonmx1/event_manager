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
 * This class is part of the event manager project which was programmed during the
 * "PS Software Architecture" course in the winter semester 2021/2022 at the University of Innsbruck.
 *
 * @author Matthias Komar
 * @author Manuel Reichegger
 * @author Simon Muscatello
 * @author Stefan Wagner
 *
 * Controller which controls the pollTimeslot management between backend and frontend.
 */
@RestController
@RequestMapping("/api/pollTimeslots")
public class PollTimeslotsController implements Serializable {

    private static final long serialVersionUID = 1L;

    @Autowired
    private PollTimeslotsService pollTimeslotsService;

    @GetMapping("/getAll")
    public Collection<PollTimeslots> getAll() {
        return pollTimeslotsService.getAllPollTimeslots();
    }

    @PostMapping("/edit")
    public ResponseEntity<MessageResponse> edit(@RequestBody PollTimeslotsRequest pt) {
        PollTimeslots pollTimeslots = pollTimeslotsService.loadPollTimeslot(pt.getPollId(), pt.getTimeslotId());
        pollTimeslots.setPoints(pt.getPoints());
        if(pollTimeslotsService.savePollTimeslots(pollTimeslots) == null) {
            return ResponseEntity.ok(new MessageResponse("Error: Poll does not exist!"));
        } else {
            return ResponseEntity.ok(new MessageResponse("Poll submitted successfully!"));
        }
    }
}
