package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.model.User;
import at.qe.skeleton.services.UserService;
import java.io.Serializable;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
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
