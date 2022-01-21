package beans;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Post {
	private String id;
	private String author;
	private String picture;
	private String description;
	private LocalDateTime posted;
	private ArrayList<String> comments;
	private boolean isDeleted;
	private boolean isPicture;

	public Post(String id, String author, String picture, String description, LocalDateTime posted,
			ArrayList<String> comments, boolean isDeleted, boolean isPicture) {
		super();
		this.id = id;
		this.author = author;
		this.picture = picture;
		this.description = description;
		this.posted = posted;
		this.comments = comments;
		this.isDeleted = isDeleted;
		this.isPicture = isPicture;
	}

	public boolean isPicture() {
		return isPicture;
	}

	public void setPicture(boolean isPicture) {
		this.isPicture = isPicture;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public LocalDateTime getPosted() {
		return posted;
	}

	public void setPosted(LocalDateTime posted) {
		this.posted = posted;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public ArrayList<String> getComments() {
		return comments;
	}

	public void setComments(ArrayList<String> comments) {
		this.comments = comments;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	@Override
	public String toString() {
		return "Post [id=" + id + ", authorID=" + author + ", picture=" + picture + ", description=" + description
				+ ", posted=" + posted + ", commentIDs=" + comments + ", isDeleted=" + isDeleted + "]";
	}

}
