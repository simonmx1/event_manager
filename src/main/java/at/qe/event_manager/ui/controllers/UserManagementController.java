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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * This class is part of the event manager project which was programmed during the
 * "PS Software Architecture" course in the winter semester 2021/2022 at the University of Innsbruck.
 * 
 * @author Matthias Komar
 * @author Manuel Reichegger
 * @author Simon Muscatello
 * @author Stefan Wagner
 * 
 * Controller which controls the user management between backend and frontend.
 */
@RestController
@RequestMapping("/api/users")
public class UserManagementController implements Serializable {

    private static final long serialVersionUID = 1L;

    @Autowired
    private UserService userService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    private static final String FORBIDDEN = "You shall not pass!";

    @GetMapping("/getAll")
    public Collection<User> getUsers() {
    	Collection<User> u = userService.getAllUsers();
        u.forEach(x -> x.setPassword(""));
        return u;
    }

    /**
     * Check if a user is authorized to perform an action (by username or admin role)
     * @param username of the user
     * @return true, if authorized, else false
     */
    private boolean isAuthorized(String username) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName().equals(username) || auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    @GetMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get(@RequestParam(name = "username") String username) {
        if (isAuthorized(username)) {
        	User user = userService.loadUserByUsername(username);
        	user.setPassword("");
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(FORBIDDEN, HttpStatus.FORBIDDEN);
    }

    @PostMapping("/edit")
    public ResponseEntity<String> edit(@RequestBody User user) {
        if (isAuthorized(user.getUsername())) {
            if (userService.saveUser(user) == null) {
                return new ResponseEntity<>("User does not exist!", HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity<>("User edited successfully", HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>(FORBIDDEN, HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<String> delete(@RequestBody String username) {
        if (isAuthorized(new JSONObject(username).getString("username"))) {
        	User user = userService.loadUserByUsername(new JSONObject(username).getString("username"));
        	userService.deleteUser(user);
            MailService.sendUserDeleteMessage(user);
            return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>(FORBIDDEN, HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/password")
    public ResponseEntity<String> password(@RequestBody String body) {
        User user = userService.loadUserByUsername(new JSONObject(body).getString("username"));
        user.setPassword(passwordEncoder.encode(new JSONObject(body).getString("password")));
        userService.saveUser(user);
        return new ResponseEntity<>("Password updated successfully", HttpStatus.OK);
    }
}
