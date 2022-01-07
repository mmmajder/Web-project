package beans;

import java.time.LocalDate;
import java.util.ArrayList;

import enums.Gender;

public class User extends Person{
	private boolean isBlocked;

	

	public User(String id, String username, String password, String email, String name, String surname,
			LocalDate dateOfBirth, Gender gender, String profilePicture, String biography, ArrayList<String> friendIDs,
			ArrayList<String> friendRequestIDs, ArrayList<String> postIDs, ArrayList<String> pictureIDs,
			boolean isPrivate, ArrayList<String> chats, boolean isBlocked) {
		super(id, username, password, email, name, surname, dateOfBirth, gender, profilePicture, biography, friendIDs,
				friendRequestIDs, postIDs, pictureIDs, isPrivate, chats);
		this.isBlocked = isBlocked;
	}

	public boolean isBlocked() {
		return isBlocked;
	}

	public void setBlocked(boolean isBlocked) {
		this.isBlocked = isBlocked;
	}
}
