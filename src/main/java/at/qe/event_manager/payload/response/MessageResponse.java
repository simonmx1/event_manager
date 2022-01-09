package at.qe.event_manager.payload.response;

public class MessageResponse {
	private final String msg;

    public MessageResponse(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
