package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.model.User;
import at.qe.skeleton.services.UserService;

import java.io.Serializable;

import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.faces.application.FacesMessage;

/**
 * Controller for the user detail view.
 * <p>
 * This class is part of the skeleton project provided for students of the
 * courses "Software Architecture" and "Software Engineering" offered by the
 * University of Innsbruck.
 */
@Component
@Scope("view")
public class UserCreateController implements Serializable {

    @Autowired
    private UserService userService;

    /**
     * Attribute to cache the currently displayed user
     */
    private User user = new User();

    /**
     * Returns the currently displayed user.
     *
     * @return
     */
    public User getUser() {
        return user;
    }

    /**
     * Action to save the currently displayed user.
     */
    public void createUser() {
        User response = this.userService.createUser(user);
        if (response == null) {
            dialog("Error", "User already exists!", FacesMessage.SEVERITY_ERROR);
        } else {
            dialog("Success", "User created!", FacesMessage.SEVERITY_INFO);
        }
    }

    private void dialog(String title, String text, FacesMessage.Severity icon) {
        FacesMessage message = new FacesMessage(icon, title, text);
        PrimeFaces.current().dialog().showMessageDynamic(message);

    }

}
