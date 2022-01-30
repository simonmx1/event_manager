package at.qe.event_manager.repositories;

import java.io.Serializable;

import at.qe.event_manager.model.Tag;
import org.springframework.data.jpa.repository.Query;

/**
 * This interface is part of the event manager project which was programmed during the
 * "PS Software Architecture" course in the winter semester 2021/2022 at the University of Innsbruck.
 * 
 * @author Matthias Komar
 * @author Manuel Reichegger
 * @author Simon Muscatello
 * @author Stefan Wagner
 * 
 * Repository for managing {@link Tag} entities.
 */

public interface TagRepository extends AbstractRepository<Tag, Integer>, Serializable {

    @Query("SELECT t FROM Tag t WHERE :text = t.text")
    Tag findFirstTagByText(String text);
}
