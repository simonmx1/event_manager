package at.qe.event_manager.ui.controllers;

import at.qe.event_manager.model.User;
import at.qe.event_manager.payload.request.LoginRequest;
import at.qe.event_manager.payload.request.RegisterRequest;
import at.qe.event_manager.payload.response.MessageResponse;
import at.qe.event_manager.services.UserService;
import at.qe.event_manager.util.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtTokenUtil;
    @Autowired
	PasswordEncoder encoder;

    @GetMapping("/hello")
    public String hello() {
        return "Hello World";
    }

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody LoginRequest authenticationRequest) throws Exception {
    	try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())).getCredentials();
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }
        final User user = userService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtTokenUtil.generateToken(user);
        System.out.println(userService.loadUserByUsername(jwtTokenUtil.extractUsername(jwt)).getRole());
        return ResponseEntity.ok(new MessageResponse(jwt));
    }
    
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
    	User user = new User();
    	user.setUsername(registerRequest.getUsername());
    	user.setPassword(encoder.encode(registerRequest.getPassword()));
    	user.setFirstName(registerRequest.getFirstName());
    	user.setLastName(registerRequest.getLastName());
    	user.setEmail(registerRequest.getEmail());
    	user.setEnabled(registerRequest.isEnabled());
    	user.setRole(registerRequest.getRole());
    	if(userService.createUser(user) == null) {
    		return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
    	}
    	else {
    		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    	}
    }
}
