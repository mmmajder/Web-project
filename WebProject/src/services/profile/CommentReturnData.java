package services.profile;

import java.time.LocalDateTime;

public class CommentReturnData {
	private String id;
	private String text;
	private String authorId;
	private String name;
	private String lastname;
	private LocalDateTime created;
	private LocalDateTime lastEdited;
	private String profilePicture;

	public CommentReturnData(String id, String text, String authorId, String name, String lastname,
			LocalDateTime created, LocalDateTime lastEdited, String profilePicture) {
		super();
		this.id = id;
		this.text = text;
		this.authorId = authorId;
		this.name = name;
		this.lastname = lastname;
		this.created = created;
		this.lastEdited = lastEdited;
		this.profilePicture = profilePicture;
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

	public String getAuthorId() {
		return authorId;
	}

	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public void setCreated(LocalDateTime created) {
		this.created = created;
	}

	public LocalDateTime getLastEdited() {
		return lastEdited;
	}

	public void setLastEdited(LocalDateTime lastEdited) {
		this.lastEdited = lastEdited;
	}

	public String getProfilePicture() {
		return profilePicture;
	}

	public void setProfilePicture(String profilePicture) {
		this.profilePicture = profilePicture;
	}

}
