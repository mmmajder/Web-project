package beans;

import java.util.ArrayList;
import java.util.Date;

public class User extends Person {
	private String profilePicture;
	private String biography;
	private ArrayList<FriendRequest> friendRequests;
	private ArrayList<User> friends;
	private ArrayList<Post> posts;
	private Boolean isPrivate;
	private Boolean isBlocked;

	public User(String name, String surname, Date dateOfBirth, Gender gender, String username, String password,
			String email, String profilePicture, String biography, ArrayList<FriendRequest> friendRequests,
			ArrayList<User> friends, ArrayList<Post> posts, Boolean isPrivate, Boolean isBlocked) {
		super(name, surname, dateOfBirth, gender, username, password, email);
		this.profilePicture = profilePicture;
		this.biography = biography;
		this.friendRequests = friendRequests;
		this.friends = friends;
		this.posts = posts;
		this.isPrivate = isPrivate;
		this.isBlocked = isBlocked;
	}
	
	public ArrayList<FriendRequest> getFriendRequests() {
		return friendRequests;
	}
	public void setFriendRequests(ArrayList<FriendRequest> friendRequests) {
		this.friendRequests = friendRequests;
	}
	public void addFriendRequest(FriendRequest friendRequest) {
		this.friendRequests.add(friendRequest);
	}
	public void removeFriendRequest(FriendRequest friendRequest) {
		this.friendRequests.remove(friendRequest);
	}
	public ArrayList<Post> getPosts() {
		return posts;
	}
	public void setPosts(ArrayList<Post> posts) {
		this.posts = posts;
	}	
	public void addPost(Post post) {
		this.posts.add(post);
	}	
	public void removePost(Post post) {
		this.posts.remove(post);
	}	
	public String getProfilePicture() {
		return profilePicture;
	}
	public void setProfilePicture(String profilePicture) {
		this.profilePicture = profilePicture;
	}
	public ArrayList<User> getFriends() {
		return friends;
	}
	public void setFriends(ArrayList<User> friends) {
		this.friends = friends;
	}	
	public void addFriend(User friend) {
		this.friends.add(friend);
	}
	public void setFriends(User friend) {
		this.friends.remove(friend);
	}
	public Boolean getIsPrivate() {
		return isPrivate;
	}
	public void setIsPrivate(Boolean isPrivate) {
		this.isPrivate = isPrivate;
	}
	public Boolean getIsBlocked() {
		return isBlocked;
	}
	public void setIsBlocked(Boolean isBlocked) {
		this.isBlocked = isBlocked;
	}
	public String getBiography() {
		return biography;
	}
	public void setBiography(String biography) {
		this.biography = biography;
	}
}
