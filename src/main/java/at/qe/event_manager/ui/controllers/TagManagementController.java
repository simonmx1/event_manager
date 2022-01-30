package at.qe.event_manager.ui.controllers;

import at.qe.event_manager.model.Tag;
import at.qe.event_manager.payload.response.MessageResponse;
import at.qe.event_manager.services.TagService;
import org.primefaces.shaded.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * This class is part of the event manager project which was programmed during the
 * "PS Software Architecture" course in the winter semester 2021/2022 at the University of Innsbruck.
 * 
 * @author Matthias Komar
 * @author Manuel Reichegger
 * @author Simon Muscatello
 * @author Stefan Wagner
 * 
 * Controller which controls the tag management between backend and frontend.
 */
@RestController
@RequestMapping("/api/tag")
public class TagManagementController {
	
    @Autowired
    private TagService tagService;
    
    @GetMapping("/getAll")
    public Collection<Tag> getAll() {
        return tagService.getAllTags();
    }

    @GetMapping("/get")
    @ResponseBody
    public Tag get(@RequestParam(name = "tag") String tag) {
        return tagService.loadTag(tag);
    }

    @PostMapping("/delete")
    public ResponseEntity<MessageResponse> delete(@RequestBody String tag) {
        tagService.deleteTag(tagService.loadTag(new JSONObject(tag).getString("text")));
        return ResponseEntity.ok(new MessageResponse("Tag deleted successfully!"));
    }

    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody Tag tag) {
        if (tagService.createTag(tag) == null) {
            return new ResponseEntity<>("Error While creating the Tag!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Tag created successfully!", HttpStatus.CREATED);
        }
    }
}
