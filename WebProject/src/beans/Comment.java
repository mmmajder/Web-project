package beans;

import java.time.LocalDateTime;

public class Comment {
	private String id;
	private String text;
	private String author;
	private LocalDateTime created;
	private LocalDateTime lastEdited;
	private boolean isDeleted;

	public Comment(String id, String text, String author, LocalDateTime created, LocalDateTime lastEdited,
			boolean isDeleted) {
		super();
		this.id = id;
		this.text = text;
		this.author = author;
		this.lastEdited = lastEdited;
		this.isDeleted = isDeleted;
		this.created = created;
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public void setCreated(LocalDateTime created) {
		this.created = created;
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

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public LocalDateTime getLastEdited() {
		return lastEdited;
	}

	public void setLastEdited(LocalDateTime lastEdited) {
		this.lastEdited = lastEdited;
	}

	@Override
	public String toString() {
		return "Comment [id=" + id + ", text=" + text + ", author=" + author + ", created=" + created + ", lastEdited="
				+ lastEdited + ", isDeleted=" + isDeleted + "]";
	}
	
	
}
