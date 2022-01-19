package dao.friendRequest;

public class FriendRequestRepo {
	private String id;
	private String senderID;
	private String recieverID;
	private String dateOfRequest;
	private String state;
	public FriendRequestRepo(String id, String senderID, String recieverID, String dateOfRequest, String state) {
		super();
		this.id = id;
		this.senderID = senderID;
		this.recieverID = recieverID;
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
	public String getDateOfRequest() {
		return dateOfRequest;
	}
	public void setDateOfRequest(String dateOfRequest) {
		this.dateOfRequest = dateOfRequest;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	
}
