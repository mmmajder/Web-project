package services.search;

public class UserSearchData {
	private String id;
	private String name;
	private String surname;
	private String profilePicture;
	private String dateOfBirth;
	private int numberOfMutualFriends;

	public UserSearchData(String id, String name, String surname, String profilePicture, String dateOfBirth) {
		super();
		this.id = id;
		this.name = name;
		this.surname = surname;
		this.profilePicture = profilePicture;
		this.dateOfBirth = dateOfBirth;
	}

	public UserSearchData(String id, String name, String surname, String profilePicture, int numberOfMutualFriends) {
		super();
		this.id = id;
		this.name = name;
		this.surname = surname;
		this.profilePicture = profilePicture;
		this.numberOfMutualFriends = numberOfMutualFriends;
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

	public String getProfilePicture() {
		return profilePicture;
	}

	public void setProfilePicture(String profilePicture) {
		this.profilePicture = profilePicture;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public int getNumberOfMutualFriends() {
		return numberOfMutualFriends;
	}

	public void setNumberOfMutualFriends(int numberOfMutualFriends) {
		this.numberOfMutualFriends = numberOfMutualFriends;
	}

}
