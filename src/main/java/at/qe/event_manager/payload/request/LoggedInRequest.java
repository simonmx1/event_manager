package at.qe.event_manager.payload.request;

public class LoggedInRequest {
	private String jwt;

	public LoggedInRequest() {
	}
	
	public LoggedInRequest(String jwt) {
		this.jwt = jwt;
	}

	public String getJwt() {
		return jwt;
	}

	public void setJwt(String jwt) {
		this.jwt = jwt;
	}
}
