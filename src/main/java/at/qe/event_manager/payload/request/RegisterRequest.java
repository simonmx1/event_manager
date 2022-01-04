package at.qe.event_manager.payload.request;

import at.qe.event_manager.model.UserRole;

public class RegisterRequest extends LoginRequest{
	private String firstName;
    private String lastName;
    private String email;
    private boolean enabled;
    private UserRole role;
	
	public RegisterRequest(String username, String password, String firstName, String lastName, String email, boolean enabled, UserRole role) {
		super(username, password);
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.enabled = enabled;
		this.role = role;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}
}
