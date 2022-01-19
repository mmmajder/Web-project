package beans;

import java.time.LocalDateTime;

import enums.FriendRequestState;

public class FriendRequest {
	private String id;
	private String senderID;
	private String recieverID;
	private LocalDateTime dateOfRequest;
	private FriendRequestState state;

	public FriendRequest(String id, String sender, String reciever, LocalDateTime dateOfRequest, FriendRequestState state) {
		super();
		this.id = id;
		this.senderID = sender;
		this.recieverID = reciever;
		this.dateOfRequest = dateOfRequest;
		this.state = state;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSenderID() {
		return senderID;
	}

	public void setSenderID(String senderID) {
		this.senderID = senderID;
	}

	public String getRecieverID() {
		return recieverID;
	}

	public void setRecieverID(String recieverID) {
		this.recieverID = recieverID;
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
	
	
}
