package dao.post;


public class PostRepo {
	private String id;
	private String authorID;
	private String picture;
	private String description;
	private String posted;  
	private String commentIDs;
	private boolean isDeleted;
	public PostRepo(String id, String authorID, String picture, String description, String posted, String commentIDs,
			boolean isDeleted) {
		super();
		this.id = id;
		this.authorID = authorID;
		this.picture = picture;
		this.description = description;
		this.posted = posted;
		this.commentIDs = commentIDs;
		this.isDeleted = isDeleted;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAuthorID() {
		return authorID;
	}
	public void setAuthorID(String authorID) {
		this.authorID = authorID;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPosted() {
		return posted;
	}
	public void setPosted(String posted) {
		this.posted = posted;
	}
	public String getCommentIDs() {
		return commentIDs;
	}
	public void setCommentIDs(String commentIDs) {
		this.commentIDs = commentIDs;
	}
	public boolean isDeleted() {
		return isDeleted;
	}
	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	
	
}
