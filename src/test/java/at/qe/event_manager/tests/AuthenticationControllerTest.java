package at.qe.event_manager.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.web.WebAppConfiguration;
import at.qe.event_manager.exceptions.AuthenticationException;
import at.qe.event_manager.model.User;
import at.qe.event_manager.model.UserRole;
import at.qe.event_manager.payload.request.LoggedInRequest;
import at.qe.event_manager.payload.request.LoginRequest;
import at.qe.event_manager.payload.response.MessageResponse;
import at.qe.event_manager.services.MailService;
import at.qe.event_manager.services.SchedulerEventService;
import at.qe.event_manager.ui.controllers.AuthenticationController;
import at.qe.event_manager.ui.controllers.UserManagementController;

/**
 * This class is part of the event manager project which was programmed during the
 * "PS Software Architecture" course in the winter semester 2021/2022 at the University of Innsbruck.
 * 
 * @author Matthias Komar
 * @author Manuel Reichegger
 * @author Simon Muscatello
 * @author Stefan Wagner
 * 
 * Some very basic tests for {@link AuthenticationController}.
 */
@SpringBootTest
@WebAppConfiguration
public class AuthenticationControllerTest {
	
	@Autowired
	private AuthenticationController authenticationController;
	
	@Autowired
	private UserManagementController userManagementController;
	
	@BeforeEach
    public void disableMailServiceAndSchedulerEventService() {
        MailService.disable();
        SchedulerEventService.disable();
    }
	
	@Test
	public void loginTestAndLoggedInTest() {
		LoginRequest loginRequest = new LoginRequest("user1", "passwd");
		ResponseEntity<MessageResponse> response = authenticationController.loginUser(loginRequest);
		assertEquals(HttpStatus.OK, response.getStatusCode(), "Login was not successful");
		String jsonWebToken = response.getBody().getMsg();
		LoggedInRequest loggedInRequest = new LoggedInRequest(jsonWebToken);
		assertEquals(HttpStatus.OK, authenticationController.loggedIn(loggedInRequest).getStatusCode(),
				"call to authenticationController.loginUser did not return a correct JSONWebToken");
	}
	
	@Test
	public void loginWithWrongUsernameTest() {
		LoginRequest loginRequest = new LoginRequest("blabla", "passwd");
		assertThrows(AuthenticationException.class, () ->  authenticationController.loginUser(loginRequest),
				"Login with wrong username should not work");
	}
	
	@Test
	public void loginWithWrongPasswordTest() {
		LoginRequest loginRequest = new LoginRequest("user1", "123");
		assertThrows(AuthenticationException.class, () ->  authenticationController.loginUser(loginRequest),
				"Login with wrong password should not work");
	}
	
	@Test
	public void registerUserTest() {
		int userSize = userManagementController.getUsers().size();
		User user = new User();
		user.setUsername("admin2");
		user.setPassword("passwd");
		user.setFirstName("Test");
		user.setLastName("test");
		user.setEmail("test@test.test");
		user.setEnabled(true);
		user.setRole(UserRole.USER);
		assertEquals(HttpStatus.CREATED, authenticationController.registerUser(user).getStatusCode(),
				"Call to authenticationController.registerUser did not correctly regiser a new user");
		assertEquals(userSize+1, userManagementController.getUsers().size(),
				"Call to authenticationController.registerUser did not correctly regiser a new user");
	}
}
