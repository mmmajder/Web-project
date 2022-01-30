package beans;

import java.time.LocalDateTime;

import enums.FriendRequestState;

public class FriendRequest {
	private String id;
	private String sender;
	private String reciever;
	private LocalDateTime dateOfRequest;
	private FriendRequestState state;

	public FriendRequest(String id, String sender, String reciever, LocalDateTime dateOfRequest,
			FriendRequestState state) {
		super();
		this.id = id;
		this.sender = sender;
		this.reciever = reciever;
		this.dateOfRequest = dateOfRequest;
		this.state = state;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReciever() {
		return reciever;
	}

	public void setReciever(String reciever) {
		this.reciever = reciever;
	}

	public LocalDateTime getDateOfRequest() {
		return dateOfRequest;
	}

	public void setDateOfRequest(LocalDateTime dateOfRequest) {
		this.dateOfRequest = dateOfRequest;
	}

	public FriendRequestState getState() {
		return state;
	}

	public void setState(FriendRequestState state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "FriendRequest [id=" + id + ", sender=" + sender + ", reciever=" + reciever + ", dateOfRequest="
				+ dateOfRequest + ", state=" + state + "]";
	}

}
