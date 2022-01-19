package dao.comment;

public class CommentRepo {
	private String id;
	private String text;
	private String authorID;
	private String created;
	private String lastEdited;
	private boolean isDeleted;
	public CommentRepo(String id, String text, String authorID, String created, String lastEdited, boolean isDeleted) {
		super();
		this.id = id;
		this.text = text;
		this.authorID = authorID;
		this.created = created;
		this.lastEdited = lastEdited;
		this.isDeleted = isDeleted;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getAuthorID() {
		return authorID;
	}
	public void setAuthorID(String authorID) {
		this.authorID = authorID;
	}
	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
		this.created = created;
	}
	public String getLastEdited() {
		return lastEdited;
	}
	public void setLastEdited(String lastEdited) {
		this.lastEdited = lastEdited;
	}
	public boolean isDeleted() {
		return isDeleted;
	}
	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	
}
