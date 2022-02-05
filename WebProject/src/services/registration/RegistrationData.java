package services.registration;

public class RegistrationData {
	public static boolean emptyFieldRegistration(RegisterUser registerUser) {
		if (registerUser.getDateOfBirth() == "") {
			return false;
		} else if (registerUser.getEmail() == "") {
			return false;
		} else if (registerUser.getGender() == "") {
			return false;
		} else if (registerUser.getName() == "") {
			return false;
		} else if (registerUser.getPassword() == "") {
			return false;
		} else if (registerUser.getSurname() == "") {
			return false;
		} else if (registerUser.getUsername() == "") {
			return false;
		}
		return true;
	}

}
