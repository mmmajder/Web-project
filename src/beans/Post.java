package beans;

import java.time.LocalDateTime;
import java.util.ArrayList;


public class Post {
	private User author;
	private String picture;
	private String description;
	private LocalDateTime posted;  
	private ArrayList<Comment> comments;
	
	public Post(User author, String picture, String description, LocalDateTime posted, ArrayList<Comment> comments) {
		this.author = author;
		this.picture = picture;
		this.description = description;
		this.posted = posted;
		this.comments = comments;
	}
	
	public User getAuthor() {
		return author;
	}
	public void setAuthor(User author) {
		this.author = author;
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
	public ArrayList<Comment> getComments() {
		return comments;
	}
	public void setComments(ArrayList<Comment> comments) {
		this.comments = comments;
	}
	public void addComment(Comment comment) {
		this.comments.add(comment);
	}
	public void removeComment(Comment comment) {
		this.comments.remove(comment);
	}
}
