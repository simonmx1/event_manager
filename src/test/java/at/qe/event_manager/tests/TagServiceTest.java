package at.qe.event_manager.tests;

import at.qe.event_manager.model.Tag;
import at.qe.event_manager.services.TagService;
import org.apache.jasper.tagplugins.jstl.core.If;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@WebAppConfiguration
public class TagServiceTest {

    @Autowired
    TagService tagService;

    @Test
    @WithMockUser(username = "user2", authorities = {"ROLE_LOCATION_MANAGER"})
    public void testInitTagData() {
        assertTrue(tagService.getAllTags().size() >= 4, "Insufficient amount of tags initialized for test data source");
        boolean chinese = false, fastFood = false, pizza = false, delivery = false;
        for (Tag tag : tagService.getAllTags()) {
            if (tag.getText().equals("Chinese")) {
                if (chinese) {
                    fail("The tag Chinese is saved more than one time");
                }
                chinese = true;
            }
            if (tag.getText().equals("Fast Food")) {
                if (fastFood) {
                    fail("The tag Fast Food is saved more than one time");
                }
                fastFood = true;
            }
            if (tag.getText().equals("Pizza")) {
                if (pizza) {
                    fail("The tag Pizza is saved more than one time");
                }
                pizza = true;
            }
            if (tag.getText().equals("Delivery")) {
                if (delivery) {
                    fail("The tag Delivery is saved more than one time");
                }
                delivery = true;
            }
        }
        if (!chinese){
            fail("The tag Chinese is missing");
        }
        if (!fastFood){
            fail("The tag Fast Food is missing");
        }
        if (!pizza){
            fail("The tag Pizza is missing");
        }
        if (!delivery){
            fail("The tag Delivery is missing");
        }
    }


    @DirtiesContext
    @Test
    @Transactional
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    public void testDeleteTag() {
        String text = "Pizza";
        Tag tag = tagService.loadTag(text);
        assertNotNull(tag, "Tag Pizza could not be loaded from test data");
        tagService.deleteTag(tag);
        assertNull(tagService.loadTag(text), "Deleted Tag Pizza could still be loaded from test data");
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    public void testCreateTag() {
        int tagSize = tagService.getAllTags().size();

        String text = "Coffee to Go";

        Tag tag = new Tag();
        tag.setText(text);

        tagService.saveTag(tag);

        Tag freshlyCreatedTag = tagService.loadTag(text);
        assertNotNull(freshlyCreatedTag, "New Tag could be loaded from test data");
        assertEquals(tagSize+1, tagService.getAllTags().size(), "No Tag has been added after calling TagService.saveTag");

    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    public void testExeptionForEmtyTag(){
        Tag tag = new Tag();
        assertThrows(JpaSystemException.class, () -> tagService.saveTag(tag));
    }


}
