package dao.chat;

public class ChatRepo {
	private String id;
	private String dmIDs;
	public ChatRepo(String id, String dmIDs) {
		super();
		this.id = id;
		this.dmIDs = dmIDs;
	}

	public ChatRepo() {
		super();
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDmIDs() {
		return dmIDs;
	}
	public void setDmIDs(String dmIDs) {
		this.dmIDs = dmIDs;
	}
	

}
