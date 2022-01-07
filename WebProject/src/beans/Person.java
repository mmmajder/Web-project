package beans;

import java.time.LocalDate;
import java.util.ArrayList;

import enums.Gender;

public class Person {
	private String id;
	private String username;
	private String password;
	private String email;
	private String name;
	private String surname;
	private LocalDate dateOfBirth;
	private Gender gender;
	private String profilePicture;
	private String biography;
	private ArrayList<String> friendIDs;
	private ArrayList<String> friendRequestIDs;
	private ArrayList<String> postIDs;
	private ArrayList<String> pictureIDs;
	private boolean isPrivate;
	private ArrayList<String> chats;
	
	
	public Person(String id, String username, String password, String email, String name, String surname,
			LocalDate dateOfBirth, Gender gender, String profilePicture, String biography, ArrayList<String> friendIDs,
			ArrayList<String> friendRequestIDs, ArrayList<String> postIDs, ArrayList<String> pictureIDs,
			boolean isPrivate, ArrayList<String> chats) {
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
		this.isPrivate = isPrivate;
		this.chats = chats;
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


	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}


	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}


	public Gender getGender() {
		return gender;
	}


	public void setGender(Gender gender) {
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


	public ArrayList<String> getFriendIDs() {
		return friendIDs;
	}


	public void setFriendIDs(ArrayList<String> friendIDs) {
		this.friendIDs = friendIDs;
	}


	public ArrayList<String> getFriendRequestIDs() {
		return friendRequestIDs;
	}


	public void setFriendRequestIDs(ArrayList<String> friendRequestIDs) {
		this.friendRequestIDs = friendRequestIDs;
	}


	public ArrayList<String> getPostIDs() {
		return postIDs;
	}


	public void setPostIDs(ArrayList<String> postIDs) {
		this.postIDs = postIDs;
	}


	public ArrayList<String> getPictureIDs() {
		return pictureIDs;
	}


	public void setPictureIDs(ArrayList<String> pictureIDs) {
		this.pictureIDs = pictureIDs;
	}


	public boolean isPrivate() {
		return isPrivate;
	}


	public void setPrivate(boolean isPrivate) {
		this.isPrivate = isPrivate;
	}


	public ArrayList<String> getChats() {
		return chats;
	}


	public void setChats(ArrayList<String> chats) {
		this.chats = chats;
	}
	
	
}
