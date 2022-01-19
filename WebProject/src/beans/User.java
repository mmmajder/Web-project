package beans;

import java.time.LocalDate;
import java.util.ArrayList;

import enums.Gender;

public class User extends Person{
	private boolean isBlocked;

	

	public User(String username, String password) {
		super(username, password);
		// TODO Auto-generated constructor stub
	}

	public User(String id, String username, String password, String email, String name, String surname,
			LocalDate dateOfBirth, Gender gender, String profilePicture, String biography, ArrayList<String> friendIDs,
			ArrayList<String> friendRequestIDs, ArrayList<String> postIDs, ArrayList<String> pictureIDs,
			ArrayList<String> chats, boolean isPrivate, boolean isBlocked) {
		super(id, username, password, email, name, surname, dateOfBirth, gender, profilePicture, biography, friendIDs,
				friendRequestIDs, postIDs, pictureIDs, chats, isPrivate);
		this.isBlocked = isBlocked;
	}

	public User(String id, String username, String email, String name, String surname, Gender gender, String password) {
		super(id, username, email, name, surname, gender, password);
	}


	public User() {
		// TODO Auto-generated constructor stub
	}

	public User(String firstName, String lastName, String email, String username, String password) {
		this.setName(firstName); 
		this.setSurname(lastName);
		this.setEmail(email);
		this.setUsername(username);
		this.setPassword(password);
	}

	public boolean isBlocked() {
		return isBlocked;
	}

	public void setBlocked(boolean isBlocked) {
		this.isBlocked = isBlocked;
	}

	@Override
	public String toString() {
		return "User [isBlocked=" + isBlocked + ", getId()=" + getId() + ", getUsername()=" + getUsername()
				+ ", getPassword()=" + getPassword() + ", getEmail()=" + getEmail() + ", getName()=" + getName()
				+ ", getSurname()=" + getSurname() + ", getDateOfBirth()=" + getDateOfBirth() + ", getGender()="
				+ getGender() + ", getProfilePicture()=" + getProfilePicture() + ", getBiography()=" + getBiography()
				+ ", getFriendIDs()=" + getFriendIDs() + ", getFriendRequestIDs()=" + getFriendRequestIDs()
				+ ", getPostIDs()=" + getPostIDs() + ", getPictureIDs()=" + getPictureIDs() + ", isPrivate()="
				+ isPrivate() + ", getChats()=" + getChats() + ", getClass()=" + getClass() + ", hashCode()="
				+ hashCode() + ", toString()=" + super.toString() + "]";
	}
	
	
}
