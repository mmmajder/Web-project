package repo.user;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.fasterxml.jackson.databind.ObjectMapper;

import beans.User;
import enums.Gender;
import repo.DateManager;

public class UserRepo {
	public static ArrayList<User> users;
	public static void main(String[] args) {
		users = new ArrayList<User>();
		readFile();
		for (User string : users) {
			System.out.println(string);
		}
		updateFile();
	}
	
	
	
	@SuppressWarnings("unchecked")
	public static void readFile() {
		JSONParser jsonParser = new JSONParser();
        
        try (FileReader reader = new FileReader("resources/users.json"))
        {
            //Read JSON file
            Object obj = jsonParser.parse(reader);
 
            JSONArray userList = (JSONArray) obj;
             
            //Iterate over employee array
            userList.forEach( user -> parse( (JSONObject) user ) );
 
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void updateFile() {
		ObjectMapper mapper = new ObjectMapper();
		try {  

	        // Writing to a file   
	        mapper.writeValue(new File("resources/users.json"), users );
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }  
	}
	
	private static void parse(JSONObject user) 
    {
        String id = (String) user.get("id");
        String name = (String) user.get("name");    
        String surname = (String) user.get("surname");  
        LocalDate dateOfBirth = DateManager.getDate((JSONObject) user.get("dateOfBirth"));
        Gender gender = getGender((String) user.get("gender"));
        String username = (String) user.get("username");
        String password = (String) user.get("password");
        String email = (String) user.get("email");  
        String profilePicture = (String) user.get("profilePicture");  
        String biography = (String) user.get("biography");  
        ArrayList<String> friendRequests = getFriendRequests((JSONArray) user.get("friendRequests"));
        ArrayList<String> friends = getFriends((JSONArray) user.get("friendIDs"));
        ArrayList<String> posts = getPosts((JSONArray) user.get("postsIDs"));
        boolean isPrivate = (boolean) user.get("isPrivate"); 
        boolean isBlocked = (boolean) user.get("isBlocked"); 
        boolean isDeleted = (boolean) user.get("isDeleted"); 
        users.add(new User(id, name, surname, dateOfBirth, gender, username, password, email, profilePicture, biography, friendRequests, friends, posts, isPrivate, isBlocked, isDeleted));
    }
	
	private static ArrayList<String> getPosts(JSONArray postsObject) {
		ArrayList<String> posts = new ArrayList<String>();
        for (Object post : postsObject) {
        	posts.add((String) post);
		}
        return posts;
	}



	private static ArrayList<String> getFriends(JSONArray friendsObject) {
		ArrayList<String> friends = new ArrayList<String>();
        for (Object friend : friendsObject) {
        	friends.add((String) friend);
		}
        return friends;
	}



	private static ArrayList<String> getFriendRequests(JSONArray friendRequestsObject) {
		ArrayList<String> friendRequests = new ArrayList<String>();
		for (Object request : friendRequestsObject) {
        	friendRequests.add((String) request);
		}
		return friendRequests;
	}



	private static Gender getGender(String gender) {
		if (gender.equals("muski")) {
        	return Gender.muski;        	
        } else {
        	return Gender.zenski; 
        }
	}
}
