/*package dao.person;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;
import au.com.bytecode.opencsv.bean.CsvToBean;
import beans.Admin;
import beans.User;
import enums.Gender;

public class AdminDAO {
	static final String CSV_FILE = "admins.csv";
	private Map<String, Admin> admins = new HashMap<>();
	private String path;
	
	public static void main(String[] args) {
		AdminDAO usersDAO = new AdminDAO("");
	}
	
	
	
	public AdminDAO(String contextPath) {
		this.path = contextPath;
		readFile();
	}
	
	public Collection<Admin> findAll() {
		return admins.values();
	}

	public Admin findById(String id) {
		for (Admin admin: findAll()) {
			if (admin.getId().equals(id)) {
				return admin;
			}
		}
		return null;
	}

	public Admin find(String username, String password) {
		if (!admins.containsKey(username)) {
			return null;
		}
		Admin admin = admins.get(username);
		if (!admin.getPassword().equals(password)) {
			return null;
		}
		return admin;
	}
	
	void readFile() {
		try(CSVReader csvr = new CSVReader(new FileReader(this.path + "/resources/" + CSV_FILE), ';', CSVWriter.NO_QUOTE_CHARACTER, 1)) {
			ColumnPositionMappingStrategy<PersonRepo> strategy = new ColumnPositionMappingStrategy<>();
			strategy.setType(PersonRepo.class);
			String[] columns = new String[] {"id", "username", "password", "email", "name", "surname","dateOfBirth", "gender", "profilePicture", "biography", "friendIDs", "friendRequestIDs","postIDs", "pictureIDs", "chats", "isPrivate" };
			strategy.setColumnMapping(columns);
			
			CsvToBean<PersonRepo> csv = new CsvToBean<>();
			List<PersonRepo> tempAdmins = csv.parse(strategy, csvr);
			
			for (PersonRepo tempAdmin : tempAdmins) {
				LocalDate date = LocalDate.of(Integer.parseInt(tempAdmin.getDateOfBirth().split("-")[0]), Integer.parseInt(tempAdmin.getDateOfBirth().split("-")[1]), Integer.parseInt(tempAdmin.getDateOfBirth().split("-")[2]));
				Gender gender = getGender(tempAdmin.getGender());
				ArrayList<String> friendIDs = getList(tempAdmin.getFriendIDs());
				ArrayList<String> friendRequestIDs = getList(tempAdmin.getFriendRequestIDs());
				ArrayList<String> postIDs = getList(tempAdmin.getPostIDs());
				ArrayList<String> pictureIDs = getList(tempAdmin.getPictureIDs());
				ArrayList<String> chats = getList(tempAdmin.getChats());
				Admin admin = new Admin(tempAdmin.getId(), tempAdmin.getUsername(), tempAdmin.getPassword(), tempAdmin.getEmail(), tempAdmin.getName(), tempAdmin.getSurname(), date, gender, tempAdmin.getProfilePicture(), tempAdmin.getBiography(), friendIDs, friendRequestIDs, postIDs, pictureIDs, chats, tempAdmin.getIsPrivate());
				admins.put(tempAdmin.getUsername(), admin);
				System.out.println(admin);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	private Gender getGender(String stringGender) {
		if (stringGender=="MALE") {
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

}*/
