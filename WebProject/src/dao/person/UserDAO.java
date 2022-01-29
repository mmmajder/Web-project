package dao.person;

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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import beans.Post;
import beans.User;
import enums.Gender;
import services.profile.EditProfileData;
import services.search.SearchData;
import services.search.UserSearchData;

public class UserDAO {
	static final String CSV_FILE = "users.csv";
	private Map<String, User> users = new HashMap<>();
	private String path;

	public UserDAO(String contextPath) {
		this.path = contextPath;
		readFile();
	}

	public static void main(String[] args) {
		UserDAO dao = new UserDAO("src");
		/*
		 * dao.add(new User(dao.generateId(),
		 * dao.generateId(),"password","email","name","surname",LocalDate.now(),Gender.
		 * MALE,"profilePicture","biography",new ArrayList<>(),new ArrayList<>(),new
		 * ArrayList<>(),new ArrayList<>(),false,false,false)); dao.add(new
		 * User(dao.generateId(),
		 * dao.generateId(),"password","email","name","surname",LocalDate.now(),Gender.
		 * MALE,"profilePicture","biography",new ArrayList<>(),new ArrayList<>(),new
		 * ArrayList<>(),new ArrayList<>(),false,false,false)); dao.add(new
		 * User(dao.generateId(),
		 * dao.generateId(),"password","email","name","surname",LocalDate.now(),Gender.
		 * MALE,"profilePicture","biography",new ArrayList<>(),new ArrayList<>(),new
		 * ArrayList<>(),new ArrayList<>(),false,false,false));
		 */

		EditProfileData data = new EditProfileData();
		data.setBiography("biography");
		data.setDateOfBirth("2020-11-11");
		data.setGender("MALE");
		data.setName("name");
		data.setPassword("password");
		data.setPrivacy("privacy");
		data.setSurname("surname");
		dao.editUser("U00001", data);
	}

	public Collection<User> findAll() {
		return users.values();
	}

	public User findById(String id) {
		for (User user : findAll()) {
			System.out.println(id + " " + user.getId());
			if (user.getId().equals(id)) {
				return user;
			}
		}
		return null;
	}

	public void add(User user) {
		users.put(user.getUsername(), user);
		writeFile();
	}

	public User editUser(String id, EditProfileData data) {
		User user = findById(id);
		System.out.println(data.getPrivacy());
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
		for (String user : users.keySet()) {
			System.out.println(user);
		}
		if (!users.containsKey(username)) {
			return null;
		}
		User user = users.get(username);
		if (!user.getPassword().equals(password)) {
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
			OutputStream os = new FileOutputStream(this.path + "/resources/" + CSV_FILE);
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
			}
			writer.writeAll(data);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void readFile() {
		try {
			CSVReader csvr = new CSVReader(
					new InputStreamReader(new FileInputStream(this.path + "/resources/" + CSV_FILE), "UTF-8"), ';',
					'\'', 1);

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
							user.getProfilePicture(), user.getDateOfBirth().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")).toString());
					list.add(newUser);
				}
			}
		}
		return list;
	}

	public int getNumberOfMutualFriends(User loggedUser, User otherUser) {
		System.out.println(loggedUser);
		return loggedUser.getFriends().stream().filter(otherUser.getFriends()::contains).collect(Collectors.toList())
				.size();
	}
	
	public ArrayList<User> getMutualFriends(User loggedUser, User otherUser) {
		System.out.println(loggedUser);
		List<String> ids = loggedUser.getFriends().stream().filter(otherUser.getFriends()::contains).collect(Collectors.toList());
		ArrayList<User> users = new ArrayList<User>();
		for (String id : ids) {
			users.add(findById(id));
		}
		return users;
	}

	public ArrayList<User> getFriends(User user) {
		ArrayList<User> users = new ArrayList<User>();
		System.out.println("stigao " + user);
		for (String u : user.getFriends()) {
			System.out.println(u);
			try {
				users.add(findById(u));
				System.out.println(findById(u));
			} catch (Exception e) {
			}
		}
		// TODO Auto-generated method stub
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

}