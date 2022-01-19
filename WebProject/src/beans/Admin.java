package beans;

import java.time.LocalDate;
import java.util.ArrayList;

import enums.Gender;

public class Admin extends Person {

	

	public Admin() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Admin(String id, String username, String password, String email, String name, String surname,
			LocalDate dateOfBirth, Gender gender, String profilePicture, String biography, ArrayList<String> friendIDs,
			ArrayList<String> friendRequestIDs, ArrayList<String> postIDs, ArrayList<String> pictureIDs,
			ArrayList<String> chats, boolean isPrivate) {
		super(id, username, password, email, name, surname, dateOfBirth, gender, profilePicture, biography, friendIDs,
				friendRequestIDs, postIDs, pictureIDs, chats, isPrivate);
		// TODO Auto-generated constructor stub
	}

	public Admin(String username, String password) {
		super(username, password);
		// TODO Auto-generated constructor stub
	}

	

	


}
