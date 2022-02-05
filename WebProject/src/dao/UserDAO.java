package dao;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import beans.Chat;
import beans.DM;
import beans.Post;
import beans.User;
import enums.Gender;
import services.profile.EditProfileData;
import services.registration.RegisterUser;
import services.search.SearchData;
import services.search.UserSearchData;

public class UserDAO {
	static final String CSV_FILE = "users.csv";
	private Map<String, User> users = new HashMap<>();
	private RepositoryDAO repository = new RepositoryDAO();
	private String path;

	public UserDAO(String contextPath) {
		this.path = contextPath;
		readFile();
	}

	public Collection<User> findAll() {
		return users.values();
	}

	public User findById(String id) {
		for (User user : findAll()) {
			if (user.getId().equals(id)) {
				return user;
			}
		}
		return null;
	}

	public ArrayList<User> getAdmins() {
		ArrayList<User> admins = new ArrayList<User>();
		for (User user : findAll()) {
			if (user.isAdmin()) {
				admins.add(user);
			}
		}
		return admins;
	}

	public void add(User user) {
		users.put(user.getUsername(), user);
		writeFile();
	}

	public User editUser(String id, EditProfileData data) {
		if (EditProfileData.isEmpty(data)) {
			return null;
		}
		User user = findById(id);
		user.setName(data.getName());
		user.setSurname(data.getSurname());
		if (!data.getPassword().equals("")) {
			user.setPassword(data.getPassword());
		}
		user.setGender(getGender(data.getGender().toUpperCase()));
		user.setDateOfBirth(getDate(data.getDateOfBirth()));
		user.setBiography(data.getBiography());
		if (data.getPrivacy().equals("private")) {
			user.setPrivate(true);
		} else {
			user.setPrivate(false);
		}
		writeFile();
		return user;
	}

	public User findByUsername(String username) {
		if (!users.containsKey(username)) {
			return null;
		}
		return users.get(username);
	}

	public User find(String username, String password) {
		if (!users.containsKey(username)) {
			return null;
		}
		User user = users.get(username);
		if (!user.getPassword().equals(password) || user.isBlocked()) {
			return null;
		}
		return user;
	}

	// example of id = U00001
	public String generateId() {
		String number = String.format("%05d", findAll().size() + 1);
		StringBuilder sb = new StringBuilder();
		sb.append("U");
		sb.append(number);
		System.out.println(sb);
		return sb.toString();

	}

	public void writeFile() {
		try {
			OutputStream os = new FileOutputStream(repository.getPath() + "/resources/" + CSV_FILE);
			CSVWriter writer = new CSVWriter(new PrintWriter(new OutputStreamWriter(os, "UTF-8")), ';',
					CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);

			List<String[]> data = new ArrayList<String[]>();
			data.add(new String[] { "id", "username", "password", "email", "name", "surname", "dateOfBirth", "gender",
					"profilePicture", "biography", "friendIDs", "friendRequestIDs", "postIDs", "pictureIDs", "chats",
					"isPrivate", "isBlocked", "isAdmin" });
			for (User u : users.values()) {
				data.add(new String[] { u.getId(), u.getUsername(), u.getPassword(), u.getEmail(), u.getName(),
						u.getSurname(), u.getDateOfBirth().toString(), u.getGender().toString(), u.getProfilePicture(),
						u.getBiography(), printList(u.getFriends()), printList(u.getFriendRequests()),
						printList(u.getPosts()), printList(u.getChats()), new Boolean(u.isPrivate()).toString(),
						new Boolean(u.isBlocked()).toString(), new Boolean(u.isAdmin()).toString() });
				System.out.println(u);
			}
			writer.writeAll(data);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void readFile() {
		try {
			CSVReader csvr = new CSVReader(new InputStreamReader(
					new FileInputStream(repository.getPath() + "/resources/" + CSV_FILE), "UTF-8"), ';', '\'', 1);

			String[] nextLine;
			/*
			 * String[] columns = new String[] { "id", "username", "password", "email",
			 * "name", "surname", "dateOfBirth", "gender", "profilePicture", "biography",
			 * "friendIDs", "friendRequestIDs", "postIDs", "pictureIDs", "chats",
			 * "isPrivate", "isBlocked", "isAdmin" };
			 */
			while ((nextLine = csvr.readNext()) != null) {
				LocalDate date = getDate(nextLine[6]);
				Gender gender = getGender(nextLine[7]);
				ArrayList<String> friends = getList(nextLine[10]);
				ArrayList<String> friendRequests = getList(nextLine[11]);
				ArrayList<String> posts = getList(nextLine[12]);
				ArrayList<String> chats = getList(nextLine[13]);
				User user = new User(nextLine[0], nextLine[1], nextLine[2], nextLine[3], nextLine[4], nextLine[5], date,
						gender, nextLine[8], nextLine[9], friends, friendRequests, posts, chats,
						new Boolean(nextLine[14]), new Boolean(nextLine[15]), new Boolean(nextLine[16]));
				System.out.println(user);
				users.put(user.getUsername(), user);
			}
			csvr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String printList(List<?> elems) {
		StringBuilder sb = new StringBuilder();
		for (Object object : elems) {
			sb.append(object.toString());
			sb.append('|');
		}
		if (sb.length() != 0) {
			sb.setLength(sb.length() - 1);
		}
		return sb.toString();
	}

	public LocalDate getDate(String s) {
		return LocalDate.of(Integer.parseInt(s.split("-")[0]), Integer.parseInt(s.split("-")[1]),
				Integer.parseInt(s.split("-")[2]));
	}

	public Gender getGender(String stringGender) {
		if (stringGender.equals("MALE")) {
			return Gender.MALE;
		}
		return Gender.FEMALE;
	}

	private ArrayList<String> getList(String s) {
		ArrayList<String> elems = new ArrayList<String>();
		for (String elem : s.split("\\|")) {
			if (!elem.equals("")) {
				elems.add(elem);
			}
		}
		return elems;
	}

	public ArrayList<UserSearchData> searchUsers(SearchData data, User loggedUser) {
		ArrayList<UserSearchData> list = new ArrayList<UserSearchData>();
		for (User user : findAll()) {
			if (user.getName().toLowerCase().contains(data.getName().toLowerCase())
					&& user.getSurname().toLowerCase().contains(data.getLastName().toLowerCase())) {
				LocalDate startDate = LocalDate.MIN;
				if (!data.getStart().isEmpty())
					startDate = LocalDate.parse(data.getStart());
				LocalDate endDate = LocalDate.MAX;
				if (!data.getEnd().isEmpty())
					endDate = LocalDate.parse(data.getEnd());
				if (user.getDateOfBirth().isAfter(startDate) && user.getDateOfBirth().isBefore(endDate)) {
					UserSearchData newUser = new UserSearchData(user.getId(), user.getName(), user.getSurname(),
							user.getProfilePicture(),
							user.getDateOfBirth().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")).toString());
					list.add(newUser);
				}
			}
		}
		return list;
	}

	public int getNumberOfMutualFriends(User loggedUser, User otherUser) {
		return loggedUser.getFriends().stream().filter(otherUser.getFriends()::contains).collect(Collectors.toList())
				.size();
	}

	public ArrayList<User> getMutualFriends(User loggedUser, User otherUser) {
		List<String> ids = loggedUser.getFriends().stream().filter(otherUser.getFriends()::contains)
				.collect(Collectors.toList());
		ArrayList<User> users = new ArrayList<User>();
		for (String id : ids) {
			users.add(findById(id));
		}
		return users;
	}

	public ArrayList<User> getFriends(User user) {
		ArrayList<User> users = new ArrayList<User>();
		for (String u : user.getFriends()) {
			try {
				users.add(findById(u));
			} catch (Exception e) {
			}
		}
		return users;
	}

	public void deletePost(String userID, String postID) {
		for (User user : findAll()) {
			if (user.getId().equals(userID)) {
				user.deletePost(postID);
				this.writeFile();
				System.out.println("Obrisao sam post.");
			}
		}
	}

	public String changeProfilePicture(Post photo) {
		for (User user : findAll()) {
			if (user.getId().equals(photo.getAuthor())) {
				user.setProfilePicture(photo.getPictureLocation());
				this.writeFile();
				System.out.println("Promenio sam profilnu.");
				return "images/userPictures/" + user.getId() + "/" + photo.getPictureLocation();
			}
		}
		return null;
	}

	public void addNewPost(User currentlyLogged, Post newPost) {
		for (User user : findAll()) {
			if (user.getId().equals(currentlyLogged.getId())) {
				user.addPost(newPost.getId());
				this.writeFile();
				System.out.println("Dodao sam post.");
			}
		}
	}

	public ArrayList<String> getPostsOfPublicUsers() {
		ArrayList<String> posts = new ArrayList<String>();
		for (User user : findAll()) {
			if (!user.isPrivate())
				posts.addAll(user.getPosts());
		}
		return posts;
	}

	private void connectFriends(User user1, User user2) {
		ArrayList<String> user1Friends = user1.getFriends();
		user1Friends.add(user2.getId());
		user1.setFriends(user1Friends);
	}

	public void addFriend(String receiverId, String senderId) {
		User receiver = findById(receiverId);
		User sender = findById(senderId);
		connectFriends(receiver, sender);
		connectFriends(sender, receiver);
		writeFile();
	}

	private void setChats(User user, Chat chat) {
		ArrayList<String> user1Chats = user.getChats();
		user1Chats.add(chat.getId());
		user.setChats(user1Chats);
	}

	public void addChatToUsers(User user1, User user2, Chat chat) {
		setChats(user1, chat);
		setChats(user2, chat);
		writeFile();
	}

	public boolean hasNotSeenMessages(User user, DmDAO dmDAO, ChatDAO chatDao) {
		for (String chatId : user.getChats()) {
			Chat chat = chatDao.findById(chatId);
			if (!chat.isSeen()) {
				DM dm = dmDAO.findById(chat.getDms().get(chat.getDms().size() - 1));
				if (dm.getReciever().equals(user.getId())) {
					return true;
				}
			}
		}
		return false;
	}

	private void removeFromFriendsList(User user1, String user2Id) {
		ArrayList<String> friends = user1.getFriends();
		Iterator<String> itr = friends.iterator();
		while (itr.hasNext()) {
			if (itr.next().equals(user2Id)) {
				itr.remove();
				break;
			}
		}
	}

	public void removeFriend(String loggedUserId, String otherUserId) {
		User loggedUser = findById(loggedUserId);
		User otherUser = findById(otherUserId);
		removeFromFriendsList(loggedUser, otherUser.getId());
		removeFromFriendsList(otherUser, loggedUser.getId());
		writeFile();
	}

	public void addFriendRequest(String senderId, String receiverId, String friendRequestId) {
		System.out.println("\n\nFRIEND REQUEST\n\n");
		System.out.println(senderId);
		System.out.println(findById(senderId));
		findById(senderId).addRequest(friendRequestId);
		System.out.println(findById(senderId));
		System.out.println(receiverId);
		System.out.println(findById(receiverId));
		findById(receiverId).addRequest(friendRequestId);
		System.out.println(findById(receiverId));

		writeFile();
	}

	public void blockUser(String userId) {
		users.get(userId).setBlocked(true);
		writeFile();
	}

	public void unblockUser(String userId) {
		users.get(userId).setBlocked(false);
		writeFile();
	}

	public void deleteRequest(String senderId, String id, String requestId) {
		findById(senderId).deleteRequest(requestId);
		findById(id).deleteRequest(requestId);
		writeFile();
	}

	/*private void removeChat(User user, String charForDelete) {
		user.removeChat(charForDelete);
	}*/
	
	public void removeChat(User user1, User user2, Chat chat) {
		user1.removeChat(chat.getId());
		user2.removeChat(chat.getId());		
	}
	
	

}