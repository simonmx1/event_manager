package at.qe.event_manager.ui.controllers;

import at.qe.event_manager.model.Event;
import at.qe.event_manager.payload.request.EventCreationRequest;
import at.qe.event_manager.payload.response.MessageResponse;
import at.qe.event_manager.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/event")
public class EventManagementController {

    @Autowired
    private EventService eventService;
    

    @GetMapping("/getAll")
    public Collection<Event> getEvents() {
        return eventService.getAllEvents();
    }

    @GetMapping("/get")
    @ResponseBody
    public Event get(@RequestParam(name = "id") Integer id) {
        return eventService.loadEvent(id);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody EventCreationRequest eventCreationRequest) {
        System.out.println(eventCreationRequest.toString());
    }

    @PostMapping("/edit")
    public ResponseEntity<?> edit(@RequestBody Event event) {
        if(eventService.saveEvent(event) == null) {
            return ResponseEntity.ok(new MessageResponse("Error: Event does not exist!"));
        } else {
            return ResponseEntity.ok(new MessageResponse("Event edited successfully!"));
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody Integer id) {
        eventService.deleteEvent(eventService.loadEvent(id));
        return ResponseEntity.ok(new MessageResponse("Event deleted successfully!"));
    }
}
