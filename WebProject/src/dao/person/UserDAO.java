package dao.person;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.sound.midi.Soundbank;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.IconifyAction;
import javax.validation.constraints.Size;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;
import au.com.bytecode.opencsv.bean.CsvToBean;
import beans.DM;
import beans.User;
import enums.Gender;
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

	public void add(User user) {
		users.put(user.getUsername(), user);
		writeFile();
	}

	public User findByUsername(String username) {
		System.out.println("stigao");
		if (!users.containsKey(username)) {
			return null;
		}
		return users.get(username);
	}

	public User find(String username, String password) {
		for (String user : users.keySet() ) {
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
		return sb.toString();

	}

	public void writeFile() {
		try {
			CSVWriter writer = new CSVWriter(new FileWriter(this.path + "/resources/" + CSV_FILE), ';',
					CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);
			List<String[]> data = new ArrayList<String[]>();
			data.add(new String[] { "id", "username", "password", "email", "name", "surname", "dateOfBirth", "gender",
					"profilePicture", "biography", "friendIDs", "friendRequestIDs", "postIDs", "pictureIDs", "chats",
					"isPrivate", "isBlocked", "isAdmin" });
			for (User u : findAll()) {
				data.add(new String[] { u.getId(), u.getUsername(), u.getPassword(), u.getEmail(), u.getName(),
						u.getSurname(), u.getDateOfBirth().toString(), u.getGender().toString(), u.getProfilePicture(),
						u.getBiography(), printList(u.getFriends()), printList(u.getFriendRequests()),
						printList(u.getPosts()), printList(u.getChats()), new Boolean(u.isPrivate()).toString(),
						new Boolean(u.isBlocked()).toString(), new Boolean(u.isAdmin()).toString() });
			}
			writer.writeAll(data);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	void readFile() {
        try (CSVReader csvr = new CSVReader(new FileReader(this.path + "/resources/" + CSV_FILE), ';',
                CSVWriter.NO_QUOTE_CHARACTER, 1)) {
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

        } catch (FileNotFoundException e) {
            e.printStackTrace();
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
		if (stringGender == "MALE") {
			return Gender.MALE;
		}
		return Gender.FEMALE;
	}

	private ArrayList<String> getList(String s) {
		ArrayList<String> elems = new ArrayList<String>();
		for (String elem : s.split("\\|")) {
			elems.add(elem);
		}
		return elems;
	}

	public ArrayList<UserSearchData> searchUsers(SearchData data, User loggedUser) {
		ArrayList<UserSearchData> list = new ArrayList<UserSearchData>();
		for (User user : findAll()) {
			if (user.getName().toLowerCase().contains(data.getName().toLowerCase()) && user.getSurname().toLowerCase().contains(data.getLastName().toLowerCase())) {
				LocalDate startDate = LocalDate.MIN;
				if(!data.getStart().isEmpty())
					startDate = LocalDate.parse(data.getStart());
				LocalDate endDate = LocalDate.MAX;
				if(!data.getEnd().isEmpty())
					endDate = LocalDate.parse(data.getEnd());
				if (user.getDateOfBirth().isAfter(startDate) && user.getDateOfBirth().isBefore(endDate)) {
					UserSearchData newUser = new UserSearchData(user.getId(), user.getName(), user.getSurname(), user.getProfilePicture(), getNumberOfMutualFriends(loggedUser, user));
					list.add(newUser);
				}
			}
		}
		return list;
	}
	
	public int getNumberOfMutualFriends(User loggedUser, User otherUser) {
		System.out.println(loggedUser);
		return loggedUser.getFriends().stream()
			    .filter(otherUser.getFriends()::contains)
			    .collect(Collectors
			    .toList()).size();
	}

}