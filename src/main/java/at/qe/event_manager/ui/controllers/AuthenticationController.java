package at.qe.event_manager.ui.controllers;

import at.qe.event_manager.exceptions.AuthenticationException;
import at.qe.event_manager.model.User;
import at.qe.event_manager.payload.request.LoggedInRequest;
import at.qe.event_manager.payload.request.LoginRequest;
import at.qe.event_manager.payload.response.MessageResponse;
import at.qe.event_manager.services.MailService;
import at.qe.event_manager.services.UserService;
import at.qe.event_manager.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
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

    @PostMapping("/loggedIn")
    public ResponseEntity<String[]> loggedIn(@RequestBody LoggedInRequest loggedInRequest) {
        try {
            if (loggedInRequest.getJwt() != null) {
                String username = jwtTokenUtil.extractUsername(loggedInRequest.getJwt());
                String role = userService.loadUserByUsername(username).getRole().toString();
                return new ResponseEntity<>(new String[]{username, role}, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new String[]{"authentication failed"}, HttpStatus.EXPECTATION_FAILED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new String[]{"token is expired"}, HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<MessageResponse> loginUser(@RequestBody LoginRequest authenticationRequest) throws AuthenticationException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())).getCredentials();
        } catch (Exception e) {
            throw new AuthenticationException("Incorrect username or password was entered in /login");
        }
        final User user = userService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtTokenUtil.generateToken(user);
        return ResponseEntity.ok(new MessageResponse(jwt));
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        if (userService.createUser(user) == null) {
            return new ResponseEntity<>("Error: Username is already taken!", HttpStatus.OK);
        } else {
        	MailService.sendUserRegisterMessage(user);
            return new ResponseEntity<>("User registered successfully!", HttpStatus.CREATED);
        }
    }


}
