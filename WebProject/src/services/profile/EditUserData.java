package services.profile;

public class EditUserData {
	private String id;
	private String name;
	private String surname;
	private String password;
	private String gender;
	private String dateOfBirth;
	private String biography;
	private String newProfilePhoto;

	public EditUserData(String id, String name, String surname, String password, String gender, String dateOfBirth,
			String biography, String newProfilePhoto) {
		super();
		this.id = id;
		this.name = name;
		this.surname = surname;
		this.password = password;
		this.gender = gender;
		this.dateOfBirth = dateOfBirth;
		this.biography = biography;
		this.newProfilePhoto = newProfilePhoto;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
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

	public String getNewProfilePhoto() {
		return newProfilePhoto;
	}

	public void setNewProfilePhoto(String newProfilePhoto) {
		this.newProfilePhoto = newProfilePhoto;
	}

}
