package at.qe.event_manager.repositories;

import java.io.Serializable;

import at.qe.event_manager.model.Tag;
import org.springframework.data.jpa.repository.Query;

public interface TagRepository extends AbstractRepository<Tag, Integer>, Serializable {

    @Query("SELECT t FROM Tag t WHERE :text = t.text")
    Tag findFirstTagByText(String text);
}
