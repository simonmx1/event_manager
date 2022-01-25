package at.qe.event_manager.tests;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import at.qe.event_manager.model.User;
import at.qe.event_manager.model.UserRole;
import at.qe.event_manager.services.UserService;

/**
 * Some very basic tests for {@link UserService}.
 *
 * This class is part of the event_manager project.
 */
@SpringBootTest
@WebAppConfiguration
public class UserServiceTest {
	
	@Autowired
	UserService userService;
	
	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    public void testInitUserData() {
		assertTrue(userService.getAllUsers().size() >= 4, "Insufficient amount of users initialized for test data source");
		for(User user : userService.getAllUsers()) {
			if ("admin".equals(user.getUsername())) {
				assertEquals(UserRole.ADMIN, user.getRole(), "User \"" + user + "\" does not have role ADMIN");
				assertNotNull(user.getUsername(), "User \"" + user + "\" does not have a username defined");
				assertNotNull(user.getPassword(), "User \"" + user + "\" does not have a password defined");
				assertNotNull(user.getCreateDate(), "User \"" + user + "\" does not have a createDate defined");
				assertNotNull(user.getFirstName(), "User \"" + user + "\" does not have a firstname defined");
				assertNotNull(user.getLastName(), "User \"" + user + "\" does not have a lastname defined");
				assertNotNull(user.getEmail(), "User \"" + user + "\" does not have a email defined");
            } else if ("user1".equals(user.getUsername())) {
            	assertEquals( UserRole.USER, user.getRole(), "User \"" + user + "\" does not have role USER");
            	assertNotNull(user.getUsername(), "User \"" + user + "\" does not have a username defined");
				assertNotNull(user.getPassword(), "User \"" + user + "\" does not have a password defined");
				assertNotNull(user.getCreateDate(), "User \"" + user + "\" does not have a createDate defined");
				assertNotNull(user.getFirstName(), "User \"" + user + "\" does not have a firstname defined");
				assertNotNull(user.getLastName(), "User \"" + user + "\" does not have a lastname defined");
				assertNotNull(user.getEmail(), "User \"" + user + "\" does not have a email defined");
            } else if ("user2".equals(user.getUsername())) {
            	assertEquals(UserRole.LOCATION_MANAGER, user.getRole(), "User \"" + user + "\" does not have role LOCATION_MANAGER");
            	assertNotNull(user.getUsername(), "User \"" + user + "\" does not have a username defined");
				assertNotNull(user.getPassword(), "User \"" + user + "\" does not have a password defined");
				assertNotNull(user.getCreateDate(), "User \"" + user + "\" does not have a createDate defined");
				assertNotNull(user.getFirstName(), "User \"" + user + "\" does not have a firstname defined");
				assertNotNull(user.getLastName(), "User \"" + user + "\" does not have a lastname defined");
				assertNotNull(user.getEmail(), "User \"" + user + "\" does not have a email defined");
            } else  if ("elvis".equals(user.getUsername())) {
            	assertEquals(UserRole.USER, user.getRole(), "User \"" + user + "\" does not have role USER");
            	assertNotNull(user.getUsername(), "User \"" + user + "\" does not have a username defined");
				assertNotNull(user.getPassword(), "User \"" + user + "\" does not have a password defined");
				assertNotNull(user.getCreateDate(), "User \"" + user + "\" does not have a createDate defined");
				assertNotNull(user.getFirstName(), "User \"" + user + "\" does not have a firstname defined");
				assertNotNull(user.getLastName(), "User \"" + user + "\" does not have a lastname defined");
				assertNotNull(user.getEmail(), "User \"" + user + "\" does not have a email defined");
            }
		}
	}
	
	@DirtiesContext
    @Test
    @Transactional
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    public void testDeleteUser() {
		String username = "user2";
        User adminUser = userService.loadUserByUsername("admin");
        assertNotNull(adminUser, "Admin user could not be loaded from test data source");
        int userSize = userService.getAllUsers().size();
        User toBeDeletedUser = userService.loadUserByUsername(username);
        assertNotNull(toBeDeletedUser, "User \"" + username + "\" could not be loaded from test data source");
        
        userService.deleteUser(toBeDeletedUser);
        
        assertEquals(userSize-1, userService.getAllUsers().size(), "No user has been deleted after calling UserService.deleteUser");
        User deletedUser = userService.loadUserByUsername(username);
        assertNull(deletedUser, "Deleted User \"" + username + "\" could still be loaded from test data source via UserService.loadUserByUsername");

        for (User remainingUser : userService.getAllUsers()) {
            assertNotEquals(toBeDeletedUser.getUsername(), remainingUser.getUsername(), "Deleted User \"" + username + "\" could still be loaded from test data source via UserService.getAllUsers");
        }
	}
	
	@DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    public void testUpdateUser() {
        String username = "user1";
        User adminUser = userService.loadUserByUsername("admin");
        assertNotNull(adminUser, "Admin user could not be loaded from test data source");
        int userSize = userService.getAllUsers().size();
        User toBeUpdatedUser = userService.loadUserByUsername(username);
        assertNotNull(toBeUpdatedUser, "User \"" + username + "\" could not be loaded from test data source");

        toBeUpdatedUser.setEmail("changed-email@whatever.wherever");
        userService.saveUser(toBeUpdatedUser);

        User freshlyLoadedUser = userService.loadUserByUsername("user1");
        assertEquals(userSize, userService.getAllUsers().size(), "Size of users did change in database. User was inserted, not updated");
        assertNotNull(freshlyLoadedUser, "User \"" + username + "\" could not be loaded from test data source after being saved");
        assertEquals("changed-email@whatever.wherever", freshlyLoadedUser.getEmail(), "User \"" + username + "\" does not have a the correct email attribute stored being saved");
    }
	
	@DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    public void testCreateUser() {
        User adminUser = userService.loadUserByUsername("admin");
        assertNotNull(adminUser, "Admin user could not be loaded from test data source");
        int userSize = userService.getAllUsers().size();

        String username = "newuser";
        String password = "passwd";
        String fName = "New";
        String lName = "User";
        String email = "new-email@whatever.wherever";
        User toBeCreatedUser = new User();
        toBeCreatedUser.setUsername(username);
        toBeCreatedUser.setPassword(password);
        toBeCreatedUser.setEnabled(true);
        toBeCreatedUser.setFirstName(fName);
        toBeCreatedUser.setLastName(lName);
        toBeCreatedUser.setEmail(email);
        toBeCreatedUser.setRole(UserRole.USER);
        userService.saveUser(toBeCreatedUser);

        User freshlyCreatedUser = userService.loadUserByUsername(username);
        assertEquals(userSize+1, userService.getAllUsers().size(), "No user has been added after calling UserService.saveUser");
        assertNotNull(freshlyCreatedUser, "New user could not be loaded from test data source after being saved");
        assertEquals(username, freshlyCreatedUser.getUsername(), "New user could not be loaded from test data source after being saved");
        assertEquals(password, freshlyCreatedUser.getPassword(), "User \"" + username + "\" does not have a the correct password attribute stored being saved");
        assertEquals(fName, freshlyCreatedUser.getFirstName(), "User \"" + username + "\" does not have a the correct firstName attribute stored being saved");
        assertEquals(lName, freshlyCreatedUser.getLastName(), "User \"" + username + "\" does not have a the correct lastName attribute stored being saved");
        assertEquals(email, freshlyCreatedUser.getEmail(), "User \"" + username + "\" does not have a the correct email attribute stored being saved");
        assertEquals(UserRole.USER,freshlyCreatedUser.getRole(), "User \"" + username + "\" does not have role USER");
        assertNotNull(freshlyCreatedUser.getCreateDate(), "User \"" + username + "\" does not have a createDate defined after being saved");
    }
	
	@Test
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    public void testExceptionForEmptyUsername() {
		User adminUser = userService.loadUserByUsername("admin");
        assertNotNull(adminUser, "Admin user could not be loaded from test data source");
        User toBeCreatedUser = new User();
        assertThrows(JpaSystemException.class, () -> userService.saveUser(toBeCreatedUser));
    }
	
	@Test
	@WithMockUser(username = "user1", authorities = {"ROLE_LOCATION_MANAGER"})
	public void testUnauthorizedSaveUser() {
		User toNotBeSavedUser = userService.loadUserByUsername("user1");
		assertThrows(AccessDeniedException.class, () -> userService.saveUser(toNotBeSavedUser),
			 "Call to userService.saveUser should not work without proper authorization");
	}
	
	@Test
    @WithMockUser(username = "user1", authorities = {"ROLE_LOCATION_MANAGER"})
    public void testUnauthorizedDeleteUser() {
		User toNotBeDeletedUser = userService.loadUserByUsername("admin");
		assertThrows(AccessDeniedException.class, () -> userService.deleteUser(toNotBeDeletedUser),
				 "Call to userService.deleteUser should not work without proper authorization");
    }
}
