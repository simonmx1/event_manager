package at.qe.eventManager.ui.controllers;

import at.qe.eventManager.model.User;
import at.qe.eventManager.services.UserService;
import java.io.Serializable;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for the user list view.
 *
 * This class is part of the skeleton project provided for students of the
 * courses "Software Architecture" and "Software Engineering" offered by the
 * University of Innsbruck.
 */
@RestController
@RequestMapping("/api/userlist")
public class UserListController implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Autowired
    private UserService userService;

    /**
     * Returns a list of all users.
     *
     * @return
     */
    @GetMapping("/getUsers")
    public Collection<User> getUsers() {
        return userService.getAllUsers();
    }

}
