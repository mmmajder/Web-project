package beans;

import java.time.LocalDateTime;

public class FriendRequest {
	private User sender;
	private User reciever;
	private LocalDateTime sent;
	private FriendRequestState state;
	
	public FriendRequest(User sender, User reciever, LocalDateTime sent, FriendRequestState state) {
		super();
		this.sender = sender;
		this.reciever = reciever;
		this.sent = sent;
		this.state = state;
	}

	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	public User getReciever() {
		return reciever;
	}

	public void setReciever(User reciever) {
		this.reciever = reciever;
	}

	public LocalDateTime getSent() {
		return sent;
	}

	public void setSent(LocalDateTime sent) {
		this.sent = sent;
	}

	public FriendRequestState getState() {
		return state;
	}

	public void setState(FriendRequestState state) {
		this.state = state;
	}
	
	
}
