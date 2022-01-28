package at.qe.event_manager.services;

import at.qe.event_manager.model.Tag;
import at.qe.event_manager.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;

/**
 * Service for accessing and manipulating tag data.
 */
@Component
@Scope("application")
public class TagService {

    @Autowired
    private TagRepository tagRepository;
    
    @Autowired
    private LocationService locationService;

    /**
     * Loads all Tags from the database
     *
     * @return a Collection of all tags
     */
    public Collection<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    /**
     * Loads a single tag identified by its text.
     *
     * @param text the tag text to search for
     * @return the tag with the given text
     */
    public Tag loadTag(String text) {
        return tagRepository.findFirstTagByText(text);
    }

    /**
     * Saves the given tag. This method will also set the tag createDate for new
     * entities.
     *
     * @param tag the tag to save
     * @return the saved tag
     */
    public Tag saveTag(Tag tag) {
        if (tag.isNew()) {
            tag.setCreateDate(new Date());
        }
        return tagRepository.save(tag);
    }

    /**
     * Creates the given tag.
     *
     * @param tag the tag to create
     * @return the created tag
     */
    public Tag createTag(Tag tag) {
        return saveTag(tag);
    }

    /**
     * Deletes the tag.
     *
     * @param tag the tag to delete
     */
    public void deleteTag(Tag tag) {
    	locationService.cleanUpForTagDeletion(tag);
        tagRepository.delete(tag);
    }
}
