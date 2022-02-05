package services.registration;

public class RegistrationData {
	public static boolean isValid(RegisterUser registerUser) {
		if (!isValid(registerUser.getDateOfBirth())) {
			return false;
		} 
		if (!isValid(registerUser.getEmail())) {
			return false;
		} 
		if (!isValid(registerUser.getGender())) {
			return false;
		} 
		if (!isValid(registerUser.getName())) {
			return false;
		} 
		if (!isValid(registerUser.getPassword())) {
			return false;
		} 
		if (!isValid(registerUser.getSurname())) {
			return false;
		} 
		if (!isValid(registerUser.getUsername())) {
			return false;
		} 
		return true;
	}
	
	private static boolean isValid(String s) {
		return !s.equals("") && !s.contains(";");
	}

}
