package beans;

import java.time.LocalDateTime;

public class Comment {
	private String text;
	private User author;
	private LocalDateTime lastEdited;
	
	public Comment(String text, User author, LocalDateTime lastEdited) {
		this.author = author;
		this.lastEdited = lastEdited;
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public LocalDateTime getLastEdited() {
		return lastEdited;
	}

	public void setLastEdited(LocalDateTime lastEdited) {
		this.lastEdited = lastEdited;
	}
}
