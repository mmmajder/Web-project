package dao.person;

import java.time.LocalDate;

public class PersonRepo {
	private String id;
	private String username;
	private String password;
	private String email;
	private String name;
	private String surname;
	private String dateOfBirth;
	private String gender;
	private String profilePicture;
	private String biography;
	private String friendIDs;
	private String friendRequestIDs;
	private String postIDs;
	private String pictureIDs;
	private String chats;
	private boolean isPrivate;
	private boolean isBlocked;
	private boolean isAdmin;


	

	public PersonRepo(String id, String username, String password, String email, String name, String surname,
			String dateOfBirth, String gender, String profilePicture, String biography, String friendIDs,
			String friendRequestIDs, String postIDs, String pictureIDs, String chats, boolean isPrivate,
			boolean isBlocked, boolean isAdmin) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.name = name;
		this.surname = surname;
		this.dateOfBirth = dateOfBirth;
		this.gender = gender;
		this.profilePicture = profilePicture;
		this.biography = biography;
		this.friendIDs = friendIDs;
		this.friendRequestIDs = friendRequestIDs;
		this.postIDs = postIDs;
		this.pictureIDs = pictureIDs;
		this.chats = chats;
		this.isPrivate = isPrivate;
		this.isBlocked = isBlocked;
		this.isAdmin = isAdmin;
	}


	@Override
	public String toString() {
		return "UserRepo [id=" + id + ", username=" + username + ", password=" + password + ", email=" + email
				+ ", name=" + name + ", surname=" + surname + ", dateOfBirth=" + dateOfBirth + ", gender=" + gender
				+ ", profilePicture=" + profilePicture + ", biography=" + biography + ", friendIDs=" + friendIDs
				+ ", friendRequestIDs=" + friendRequestIDs + ", postIDs=" + postIDs + ", pictureIDs=" + pictureIDs
				+ ", chats=" + chats + ", isPrivate=" + isPrivate + ", isBlocked=" + isBlocked + "]";
	}


	public boolean isAdmin() {
		return isAdmin;
	}


	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}


	public void setPrivate(boolean isPrivate) {
		this.isPrivate = isPrivate;
	}


	public void setBlocked(boolean isBlocked) {
		this.isBlocked = isBlocked;
	}


	public PersonRepo() {
		super();
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
	public String getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getProfilePicture() {
		return profilePicture;
	}
	public void setProfilePicture(String profilePicture) {
		this.profilePicture = profilePicture;
	}
	public String getBiography() {
		return biography;
	}
	public void setBiography(String biography) {
		this.biography = biography;
	}
	public String getFriendIDs() {
		return friendIDs;
	}
	public void setFriendIDs(String friendIDs) {
		this.friendIDs = friendIDs;
	}
	public String getFriendRequestIDs() {
		return friendRequestIDs;
	}
	public void setFriendRequestIDs(String friendRequestIDs) {
		this.friendRequestIDs = friendRequestIDs;
	}
	public String getPostIDs() {
		return postIDs;
	}
	public void setPostIDs(String postIDs) {
		this.postIDs = postIDs;
	}
	public String getPictureIDs() {
		return pictureIDs;
	}
	public void setPictureIDs(String pictureIDs) {
		this.pictureIDs = pictureIDs;
	}
	public String getChats() {
		return chats;
	}
	public void setChats(String chats) {
		this.chats = chats;
	}
	public boolean getIsPrivate() {
		return isPrivate;
	}
	public void setIsPrivate(boolean isPrivate) {
		this.isPrivate = isPrivate;
	}
	public boolean getIsBlocked() {
		return isBlocked;
	}
	public void setIsBlocked(boolean isBlocked) {
		this.isBlocked = isBlocked;
	}
	
	
	

	
}
