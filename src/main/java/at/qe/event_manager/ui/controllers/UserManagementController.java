package at.qe.event_manager.ui.controllers;

import at.qe.event_manager.model.User;
import at.qe.event_manager.services.MailService;
import at.qe.event_manager.services.UserService;

import java.io.Serializable;
import java.util.Collection;

import org.primefaces.shaded.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for the user list view.
 * <p>
 * This class is part of the skeleton project provided for students of the
 * courses "Software Architecture" and "Software Engineering" offered by the
 * University of Innsbruck.
 */
@RestController
@RequestMapping("/api/users")
public class UserManagementController implements Serializable {

    private static final long serialVersionUID = 1L;

    @Autowired
    private UserService userService;

    /**
     * Returns a list of all users.
     *
     * @return
     */
    @GetMapping("/getAll")
    @PreAuthorize("hasRole('ADMIN')")
    public Collection<User> getUsers() {
        return userService.getAllUsers();
    }

    private boolean isAuthorized(String username) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName().equals(username) || auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    @GetMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get(@RequestParam(name = "username") String username) {
        if (isAuthorized(username)) {
            return new ResponseEntity<>(userService.loadUserByUsername(username), HttpStatus.OK);
        }
        return new ResponseEntity<>("You shall not pass!", HttpStatus.FORBIDDEN);
    }

    @PostMapping("/edit")
    public ResponseEntity<?> edit(@RequestBody User user) {
        if (isAuthorized(user.getUsername())) {
            if (userService.saveUser(user) == null) {
                return new ResponseEntity<>("User does not exist!", HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity<>("User edited successfully", HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>("You shall not pass!", HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody String username) {
        if (isAuthorized(new JSONObject(username).getString("username"))) {
        	User user = userService.loadUserByUsername(new JSONObject(username).getString("username"));
            userService.deleteUser(user);
            MailService.sendUserDeleteMessage(user);
            return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("You shall not pass!", HttpStatus.FORBIDDEN);
        }
    }
}
