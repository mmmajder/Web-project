package dao.dm;


public class DmRepo {
	private String id;
	private String content;
	private String dateTime;
	private String senderID;
	private String recieverID;
	public DmRepo(String id, String content, String dateTime, String senderID, String recieverID) {
		super();
		this.id = id;
		this.content = content;
		this.dateTime = dateTime;
		this.senderID = senderID;
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
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
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
	
	
}
