package repo.admin;

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

import beans.Admin;
import enums.Gender;
import repo.DateManager;

public class AdminRepo {
	public static ArrayList<Admin> admins;

	
	public static void main(String[] args) {
		admins = new ArrayList<Admin>();
		readFile();
		for (Admin string : admins) {
			System.out.println(string);
		}
		updateFile();
	}
	
	
	@SuppressWarnings("unchecked")
	public static void readFile() {
		JSONParser jsonParser = new JSONParser();
        
        try (FileReader reader = new FileReader("resources/admins.json"))
        {
            //Read JSON file
            Object obj = jsonParser.parse(reader);
 
            JSONArray adminList = (JSONArray) obj;
             
            //Iterate over employee array
            adminList.forEach( admin -> parse( (JSONObject) admin ) );
 
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
	        mapper.writeValue(new File("resources/admins.json"), admins );
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }  
	}
	
	private static void parse(JSONObject admin) 
    {
		String id = (String) admin.get("id");
        String name = (String) admin.get("name");    
        String surname = (String) admin.get("surname");  
        LocalDate dateOfBirth = DateManager.getDate((JSONObject) admin.get("dateOfBirth"));
        Gender gender = getGender((String) admin.get("gender"));
        String username = (String) admin.get("username");
        String password = (String) admin.get("password");
        String email = (String) admin.get("email");  
        String profilePicture = (String) admin.get("profilePicture");  
        
        admins.add(new Admin(id, name, surname, dateOfBirth, gender, username, password, email, profilePicture));  
    }
	private static Gender getGender(String gender) {
		if (gender.equals("muski")) {
        	return Gender.muski;        	
        } else {
        	return Gender.zenski; 
        }
	}
}
