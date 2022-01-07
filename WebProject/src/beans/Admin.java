package beans;

import java.time.LocalDate;

import enums.Gender;

public class Admin extends Person {

	public Admin(String id, String name, String surname, LocalDate dateOfBirth, Gender gender, String username,
			String password, String email, String profilePicture) {
		super(id, name, surname, dateOfBirth, gender, username, password, email, profilePicture);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "Admin [getProfilePicture()=" + getProfilePicture() + ", getId()=" + getId() + ", getName()=" + getName()
				+ ", getSurname()=" + getSurname() + ", getDateOfBirth()=" + getDateOfBirth() + ", getGender()="
				+ getGender() + ", getUsername()=" + getUsername() + ", getPassword()=" + getPassword()
				+ ", getEmail()=" + getEmail() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}
	
	
	
	
}
