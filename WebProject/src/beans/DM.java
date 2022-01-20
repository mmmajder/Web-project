package beans;

import java.time.LocalDateTime;

public class DM {
	private String id;
	private String content;
	private LocalDateTime dateTime;
	private String sender;
	private String reciever;

	public DM(String id, String content, LocalDateTime dateTime, String sender, String reciever) {
		super();
		this.id = id;
		this.content = content;
		this.dateTime = dateTime;
		this.sender = sender;
		this.reciever = reciever;
	}

	public String toString() {
		return "DM [id=" + id + ", content=" + content + ", dateTime=" + dateTime + ", senderID=" + sender
				+ ", recieverID=" + reciever + "]";
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
