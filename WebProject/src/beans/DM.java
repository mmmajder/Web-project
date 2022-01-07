package beans;

import java.time.LocalDateTime;

public class DM {
	private String id;
	private String content;
	private LocalDateTime dateTime;
	private String senderID;
	private String recieverID;
	
	public DM(String id, String content, LocalDateTime dateTime, String senderID, String recieverID) {
		super();
		this.id = id;
		this.content = content;
		this.dateTime = dateTime;
		this.senderID = senderID;
		this.recieverID = recieverID;
	}


	public String toString() {
		return "DM [id=" + id + ", content=" + content + ", dateTime=" + dateTime + ", senderID=" + senderID
				+ ", recieverID=" + recieverID + "]";
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
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public LocalDateTime getDateTime() {
		return dateTime;
	}
	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}
	
	
}
