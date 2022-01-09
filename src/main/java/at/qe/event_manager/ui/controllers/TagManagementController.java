package at.qe.event_manager.ui.controllers;

import at.qe.event_manager.model.Tag;
import at.qe.event_manager.model.Tag;
import at.qe.event_manager.payload.response.MessageResponse;
import at.qe.event_manager.services.TagService;
import org.primefaces.shaded.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/tag")
public class TagManagementController {

    @Autowired
    private TagService tagService;

    @GetMapping("/getAll")
    public Collection<Tag> getTags() {
        return tagService.getAllTags();
    }

    @GetMapping("/get")
    @ResponseBody
    public Tag get(@RequestParam(name = "tag") String tag) {
        return tagService.loadTag(tag);
    }

    @PostMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody String tag) {
        tagService.deleteTag(tagService.loadTag(new JSONObject(tag).getString("tag")));
        return ResponseEntity.ok(new MessageResponse("Tag deleted successfully!"));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createTag(@RequestBody Tag tag) {
        if (tagService.createTag(tag) == null) {
            return new ResponseEntity<>("Error While creating the Tag!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Tag created successfully!", HttpStatus.CREATED);
        }
    }
}
