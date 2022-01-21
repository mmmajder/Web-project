package dao.person;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
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

	public User find(String username, String password) {
		if (!users.containsKey(username)) {
			return null;
		}
		User user = users.get(username);
		if (!user.getPassword().equals(password)) {
			return null;
		}
		return user;
	}

	void readFile() {
		try (CSVReader csvr = new CSVReader(new FileReader(this.path + "/resources/" + CSV_FILE), ';',
				CSVWriter.NO_QUOTE_CHARACTER, 1)) {
			ColumnPositionMappingStrategy<PersonRepo> strategy = new ColumnPositionMappingStrategy<>();
			strategy.setType(PersonRepo.class);
			String[] columns = new String[] { "id", "username", "password", "email", "name", "surname", "dateOfBirth",
					"gender", "profilePicture", "biography", "friendIDs", "friendRequestIDs", "postIDs", "pictureIDs",
					"chats", "isPrivate", "isBlocked", "isAdmin" };
			strategy.setColumnMapping(columns);

			CsvToBean<PersonRepo> csv = new CsvToBean<>();
			List<PersonRepo> tempUsers = csv.parse(strategy, csvr);

			for (PersonRepo tempUser : tempUsers) {
				LocalDate date = LocalDate.of(Integer.parseInt(tempUser.getDateOfBirth().split("-")[0]),
						Integer.parseInt(tempUser.getDateOfBirth().split("-")[1]),
						Integer.parseInt(tempUser.getDateOfBirth().split("-")[2]));
				Gender gender = getGender(tempUser.getGender());
				ArrayList<String> friendIDs = getList(tempUser.getFriendIDs());
				ArrayList<String> friendRequestIDs = getList(tempUser.getFriendRequestIDs());
				ArrayList<String> postIDs = getList(tempUser.getPostIDs());
				ArrayList<String> chats = getList(tempUser.getChats());

				User user = new User(tempUser.getId(), tempUser.getUsername(), tempUser.getPassword(),
						tempUser.getEmail(), tempUser.getName(), tempUser.getSurname(), date, gender,
						tempUser.getProfilePicture(), tempUser.getBiography(), friendIDs, friendRequestIDs, postIDs,
						chats, tempUser.getIsPrivate(), tempUser.getIsBlocked(), tempUser.isAdmin());
				users.put(tempUser.getUsername(), user);
				System.out.println(user);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private Gender getGender(String stringGender) {
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
		if(data == null)
			System.out.println("null je data");
		if(data.getName() == "")
			System.out.println("'' je data");
		if(data.getName() == null)
			System.out.println("null je data.name");
		System.out.println(data);
		for (User user : findAll()) {
			if (user.getName().toLowerCase().contains(data.getName().toLowerCase()) && user.getSurname().toLowerCase().contains(data.getLastName().toLowerCase())) {
				LocalDate startDate = LocalDate.MIN;
				if(!data.getStart().isEmpty())
					startDate = LocalDate.parse(data.getStart());
				System.out.println(startDate);
				LocalDate endDate = LocalDate.MAX;
				if(!data.getEnd().isEmpty())
					endDate = LocalDate.parse(data.getEnd());
				System.out.println(endDate);
				if (user.getDateOfBirth().isAfter(startDate) && user.getDateOfBirth().isBefore(endDate)) {
					UserSearchData newUser = new UserSearchData(user.getId(), user.getName(), user.getSurname(), user.getProfilePicture(), getNumberOfMutualFriends(loggedUser, user));
					list.add(newUser);
				}
			}
		}
		for (UserSearchData userSearchData : list) {
			System.out.println(userSearchData);			
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
