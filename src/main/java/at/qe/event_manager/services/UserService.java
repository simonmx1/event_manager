package at.qe.event_manager.services;

import at.qe.event_manager.model.User;
import at.qe.event_manager.repositories.UserRepository;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * Service for accessing and manipulating user data.
 */
@Component
@Scope("application")
public class UserService implements Serializable, UserDetailsService {

	private static final long serialVersionUID = 1L;
	
	@Autowired
    private UserRepository userRepository;
	
	@Autowired
	private EventService eventService;

    /**
     * Loads all users from the database
     *
     * @return a Collection of all users
     */
    public Collection<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Loads a single user identified by its username.
     *
     * @param username the username to search for
     * @return the user with the given username
     */
    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findFirstByUsername(username);
    }

    /**
     * Saves the given user. This method will also set the event createDate for new
     * entities.
     *
     * @param user the user to save
     * @return the saved user
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or principal.username eq #user.getUsername")
    public User saveUser(User user) {
        if (user.isNew()) {
            user.setCreateDate(new Date());
        }
        if(user.getPassword() != null && user.getPassword().length() == 0) {
        	user.setPassword(loadUserByUsername(user.getUsername()).getPassword());
        }
        return userRepository.save(user);
    }

    /**
     * Creates the user, if the user is not already created.
     *
     * @param user the user to create
     * @return the updated create
     */
    public User createUser(User user) {
        if (!this.getAllUsers().contains(user)) {
            return saveUser(user);
        } else {
            return null;
        }
    }

    /**
     * Deletes the user.
     *
     * @param user the user to delete
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or principal.username eq #user.getUsername")
    public void deleteUser(User user) {
    	eventService.cleanUpForParticipantDeletion(user);
        userRepository.delete(user);
    }
}
