package at.qe.event_manager.services;

import at.qe.event_manager.model.Tag;
import at.qe.event_manager.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;

@Component
@Scope("application")
public class TagService {

    @Autowired
    private TagRepository tagRepository;
    
    @Autowired
    private LocationService locationService;

    /**
     * Returns a collection of all users.
     *
     * @return Collection of tags
     */
    public Collection<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    /**
     * @param tag
     * @return
     */
    public Tag saveTag(Tag tag) {
        if (tag.isNew()) {
            tag.setCreateDate(new Date());
        }
        return tagRepository.save(tag);
    }

    /**
     * @param tag
     * @return
     */
    public Tag createTag(Tag tag) {
        return saveTag(tag);
    }

    /**
     * @param tag
     */
    public void deleteTag(Tag tag) {
    	locationService.cleanUpForTagDeletion(tag);
        tagRepository.delete(tag);
    }

    /**
     * @param tag
     * @return
     */
    public Tag loadTag(String text) {
        return tagRepository.findFirstTagByText(text);
    }
}
