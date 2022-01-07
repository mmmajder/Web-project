package beans;

import java.time.LocalDate;
import java.util.ArrayList;

import enums.Gender;

public class User extends Person {
	
	public User(String id, String name, String surname, LocalDate dateOfBirth, Gender gender, String username,
			String password, String email, String profilePicture, String biography, ArrayList<String> friendRequestIDs,
			ArrayList<String> friendIDs, ArrayList<String> postsIDs, Boolean isPrivate, Boolean isBlocked,
			Boolean isDeleted) {
		super(id, name, surname, dateOfBirth, gender, username, password, email, profilePicture);
		this.biography = biography;
		this.friendRequestIDs = friendRequestIDs;
		this.friendIDs = friendIDs;
		this.postsIDs = postsIDs;
		this.isPrivate = isPrivate;
		this.isBlocked = isBlocked;
		this.isDeleted = isDeleted;
	}

	private String biography;
	private ArrayList<String> friendRequestIDs;
	private ArrayList<String> friendIDs;
	private ArrayList<String> postsIDs;
	private Boolean isPrivate;
	private Boolean isBlocked;
	private Boolean isDeleted;
	
	

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public ArrayList<String> getFriendRequests() {
		return friendRequestIDs;
	}

	public void setFriendRequests(ArrayList<String> friendRequests) {
		this.friendRequestIDs = friendRequests;
	}

	public ArrayList<String> getFriendRequestIDs() {
		return friendRequestIDs;
	}

	public void setFriendRequestIDs(ArrayList<String> friendRequestIDs) {
		this.friendRequestIDs = friendRequestIDs;
	}

	public ArrayList<String> getFriendIDs() {
		return friendIDs;
	}
	public void setFriendIDs(ArrayList<String> friendIDs) {
		this.friendIDs = friendIDs;
	}
	public ArrayList<String> getPostsIDs() {
		return postsIDs;
	}
	public void setPostsIDs(ArrayList<String> postsIDs) {
		this.postsIDs = postsIDs;
	}
	
	public Boolean getIsPrivate() {
		return isPrivate;
	}
	public void setIsPrivate(Boolean isPrivate) {
		this.isPrivate = isPrivate;
	}
	public Boolean getIsBlocked() {
		return isBlocked;
	}
	public void setIsBlocked(Boolean isBlocked) {
		this.isBlocked = isBlocked;
	}
	public String getBiography() {
		return biography;
	}
	public void setBiography(String biography) {
		this.biography = biography;
	}

	@Override
	public String toString() {
		return "User [biography=" + biography + ", friendRequestIDs=" + friendRequestIDs + ", friendIDs=" + friendIDs
				+ ", postsIDs=" + postsIDs + ", isPrivate=" + isPrivate + ", isBlocked=" + isBlocked + ", isDeleted="
				+ isDeleted + ", getProfilePicture()=" + getProfilePicture() + ", getId()=" + getId() + ", getName()="
				+ getName() + ", getSurname()=" + getSurname() + ", getDateOfBirth()=" + getDateOfBirth()
				+ ", getGender()=" + getGender() + ", getUsername()=" + getUsername() + ", getPassword()="
				+ getPassword() + ", getEmail()=" + getEmail() + ", getClass()=" + getClass() + ", hashCode()="
				+ hashCode() + ", toString()=" + super.toString() + "]";
	}

	


	
	
}
