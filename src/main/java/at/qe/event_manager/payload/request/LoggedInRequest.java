package at.qe.event_manager.payload.request;

public class LoggedInRequest {
	private String jwtToken;
	
	public LoggedInRequest(String jwtToken) {
		this.jwtToken = jwtToken;
	}

	public String getJwtToken() {
		return jwtToken;
	}

	public void setJwtToken(String jwtToken) {
		this.jwtToken = jwtToken;
	}
}
