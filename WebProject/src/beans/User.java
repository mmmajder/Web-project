package beans;

import java.time.LocalDate;
import java.util.ArrayList;

import enums.Gender;

public class User {
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
	private ArrayList<String> friends;
	private ArrayList<String> friendRequests;
	private ArrayList<String> posts;
	private ArrayList<String> chats;
	private boolean isPrivate;
	private boolean isBlocked;
	private boolean isAdmin;

	public User(String id, String username, String password, String email, String name, String surname,
			LocalDate dateOfBirth, Gender gender, String profilePicture, String biography, ArrayList<String> friends,
			ArrayList<String> friendRequests, ArrayList<String> posts, ArrayList<String> chats, boolean isPrivate,
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
		this.friends = friends;
		this.friendRequests = friendRequests;
		this.posts = posts;
		this.chats = chats;
		this.isPrivate = isPrivate;
		this.isBlocked = isBlocked;
		this.isAdmin = isAdmin;
	}

	public User() {
		super();
	}

	public User(String id, String username, String email, String name, String surname, Gender gender, String password,
			boolean isAdmin, LocalDate dateOfBirth) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.name = name;
		this.surname = surname;
		this.gender = gender;
		this.isAdmin = isAdmin;
		this.dateOfBirth = dateOfBirth;
		this.profilePicture = "images/default.jpg";
		this.biography = "Click to Edit Profile to add your biography!";
		this.friends = new ArrayList<String>();
		this.friendRequests = new ArrayList<String>();
		this.posts = new ArrayList<String>();
		this.chats = new ArrayList<String>();
		this.isPrivate = true;
		this.isBlocked = false;
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

	public ArrayList<String> getFriends() {
		return friends;
	}

	public void setFriends(ArrayList<String> friends) {
		this.friends = friends;
	}

	public ArrayList<String> getFriendRequests() {
		return friendRequests;
	}

	public void setFriendRequests(ArrayList<String> friendRequests) {
		this.friendRequests = friendRequests;
	}

	public ArrayList<String> getPosts() {
		return posts;
	}

	public void setPosts(ArrayList<String> posts) {
		this.posts = posts;
	}

	public void addPost(String p) {
		this.posts.add(p);
	}
	
	public void deletePost(String p) {
		this.posts.remove(p);
	}

	public ArrayList<String> getChats() {
		return chats;
	}

	public void setChats(ArrayList<String> chats) {
		this.chats = chats;
	}

	public boolean isPrivate() {
		return isPrivate;
	}

	public void setPrivate(boolean isPrivate) {
		this.isPrivate = isPrivate;
	}

	public boolean isBlocked() {
		return isBlocked;
	}

	public void setBlocked(boolean isBlocked) {
		this.isBlocked = isBlocked;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", email=" + email + ", name="
				+ name + ", surname=" + surname + ", dateOfBirth=" + dateOfBirth + ", gender=" + gender
				+ ", profilePicture=" + profilePicture + ", biography=" + biography + ", friends=" + friends
				+ ", friendRequests=" + friendRequests + ", posts=" + posts + ", chats=" + chats + ", isPrivate="
				+ isPrivate + ", isBlocked=" + isBlocked + ", isAdmin=" + isAdmin + "]";
	}

}
