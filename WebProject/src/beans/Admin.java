package beans;

import java.time.LocalDate;
import java.util.ArrayList;

import enums.Gender;

public class Admin extends Person {

	public Admin(String id, String username, String password, String email, String name, String surname,
			LocalDate dateOfBirth, Gender gender, String profilePicture, String biography, ArrayList<String> friendIDs,
			ArrayList<String> friendRequestIDs, ArrayList<String> postIDs, ArrayList<String> pictureIDs,
			boolean isPrivate, ArrayList<String> chats) {
		super(id, username, password, email, name, surname, dateOfBirth, gender, profilePicture, biography, friendIDs,
				friendRequestIDs, postIDs, pictureIDs, isPrivate, chats);
		// TODO Auto-generated constructor stub
	}



	


}
