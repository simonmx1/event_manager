package at.qe.event_manager.ui.controllers;

import at.qe.event_manager.model.User;
import at.qe.event_manager.payload.response.MessageResponse;
import at.qe.event_manager.services.UserService;

import java.io.Serializable;
import java.util.Collection;

import org.primefaces.shaded.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for the user list view.
 *
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
    public Collection<User> getUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/get")
    @ResponseBody
    public User get(@RequestParam(name = "username") String username) {
        return userService.loadUserByUsername(username);
    }

    @PostMapping("/edit")
    public ResponseEntity<?> edit(@RequestBody User user) {
        if(userService.saveUser(user) == null) {
            return ResponseEntity.ok(new MessageResponse("Error: User does not exist!"));
        } else {
            return ResponseEntity.ok(new MessageResponse("User edited successfully!"));
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody String username) {
        userService.deleteUser(userService.loadUserByUsername(new JSONObject(username).getString("username")));
        return ResponseEntity.ok(new MessageResponse("User deleted successfully!"));
    }
}
