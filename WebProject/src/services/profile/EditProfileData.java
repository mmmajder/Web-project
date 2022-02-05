package services.profile;

public class EditProfileData {
	private String name;
	private String surname;
	private String dateOfBirth;
	private String biography;
	private String password;
	private String privacy;
	private String gender;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getBiography() {
		return biography;
	}

	public void setBiography(String biography) {
		this.biography = biography;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPrivacy() {
		return privacy;
	}

	public void setPrivacy(String privacy) {
		this.privacy = privacy;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}
	
	public static boolean isEmpty(EditProfileData editProfileData) {
		if (editProfileData.getBiography().equals("")) {
			return true;
		} 
		if (editProfileData.getDateOfBirth().equals("")) {
			return true;
		} 
		if (editProfileData.getGender().equals("")) {
			return true;
		} 
		if (editProfileData.getName().equals("")) {
			return true;
		} 
		if (editProfileData.getPrivacy().equals("")) {
			return true;
		} 
		if (editProfileData.getSurname().equals("")) {
			return true;
		} 
		return false;
	}

}
