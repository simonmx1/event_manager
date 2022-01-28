package at.qe.event_manager.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import at.qe.event_manager.model.User;
import at.qe.event_manager.services.MailService;
import at.qe.event_manager.services.SchedulerEventService;
import at.qe.event_manager.services.UserService;
import at.qe.event_manager.ui.controllers.UserManagementController;

/**
 * Some very basic tests for {@link UserServiceController}.
 *
 * This class is part of the event_manager project.
 */
@SpringBootTest
@WebAppConfiguration
public class UserManagementControllerTest {
	
	@Autowired
	private UserService userService; 
	
	@Autowired
	private UserManagementController userManagementController;
	
	@BeforeEach
	public void disableMailServiceAndSchedulerEventService() {
		MailService.disable();
		SchedulerEventService.disable();
	}
	
	@Test
	public void getUsersTest() {
		for(User user : userManagementController.getUsers()) {
			assertEquals("", user.getPassword(), "User password should not just be passed on to frontend");
		}
	}
	
	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
	public void getUserTest() {
		assertEquals(HttpStatus.OK, userManagementController.get("user2").getStatusCode(),
				"Call to userManagementController.get should work with proper authorization");
	}
	
	@Test
	@WithMockUser(username = "user1", authorities = {"ROLE_USER"})
	public void getUserWithoutAuthorizationTest() {
		assertEquals(HttpStatus.FORBIDDEN, userManagementController.get("user2").getStatusCode(),
				"Call to userManagementController.get should not work without proper authorization");
	}
	
	@DirtiesContext
    @Test
	@WithMockUser(username = "user1", authorities = {"ROLE_USER"})
	public void editUserTest() {
		User toBeEditedUser = userService.loadUserByUsername("user1");
		toBeEditedUser.setFirstName("TEST1");
		userManagementController.edit(toBeEditedUser);
		assertEquals(toBeEditedUser.getFirstName(), userService.loadUserByUsername("user1").getFirstName(), "Firstname should have been edited");
	}
	
	@DirtiesContext
    @Test
    @Transactional
	@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
	public void deleteOtherUserTest() {
		assertEquals(HttpStatus.OK, userManagementController.delete("{username: user1}").getStatusCode(),
				"Call to userManagementController.delete should work with proper authorization");
		assertNull(userService.loadUserByUsername("user1"), "User should have been deleted");
	}
	
	@DirtiesContext
    @Test
    @Transactional
	@WithMockUser(username = "user1", authorities = {"ROLE_USER"})
	public void deleteOwnUserTest() {
		assertEquals(HttpStatus.OK, userManagementController.delete("{username: user1}").getStatusCode(),
				"Call to userManagementController.delete should work with proper authorization");
		assertNull(userService.loadUserByUsername("user1"), "User should have been deleted");
	}
	
	@Test
	@WithMockUser(username = "user1", authorities = {"ROLE_USER"})
	public void deleteUserWithoutAuthorizationTest() {
		assertEquals(HttpStatus.FORBIDDEN, userManagementController.delete("{username: admin}").getStatusCode(),
				"Call to userManagementController.delete should not work without proper authorization");
		assertNotNull(userService.loadUserByUsername("admin"), "User should not have been deleted");
	}
}
