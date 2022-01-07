package repo.friendRequest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.fasterxml.jackson.databind.ObjectMapper;

import beans.FriendRequest;
import enums.FriendRequestState;
import repo.DateManager;

public class FriendRequestRepo {
	
	public static ArrayList<FriendRequest> requests;
	
	public static void main(String[] args) {
		requests = new ArrayList<FriendRequest>();
		readFile();
		for (FriendRequest string : requests) {
			System.out.println(string);
		}
		updateFile();
	}
	
	
	
	@SuppressWarnings("unchecked")
	public static void readFile() {
		JSONParser jsonParser = new JSONParser();
        
        try (FileReader reader = new FileReader("resources/friendRequests.json"))
        {
            //Read JSON file
            Object obj = jsonParser.parse(reader);
 
            JSONArray requestList = (JSONArray) obj;
             
            //Iterate over employee array
            requestList.forEach( request -> parse( (JSONObject) request ) );
 
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e) {
			e.printStackTrace();
		}
	}
	
	public static void updateFile() {
		ObjectMapper mapper = new ObjectMapper();
		try {  

	        // Writing to a file   
	        mapper.writeValue(new File("resources/friendRequests.json"), requests );
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }  
	}
	
	private static void parse(JSONObject request) 
    {
        String id = (String) request.get("id");
        String senderID = (String) request.get("senderID");    
        String recieverID = (String) request.get("recieverID"); 
        LocalDateTime dateOfRequest = DateManager.getDateTime((JSONObject) request.get("dateOfRequest"));
        FriendRequestState state = getFriendRequestState((String) request.get("state"));
        requests.add(new FriendRequest(id, senderID, recieverID, dateOfRequest, state));
    }
	
	private static FriendRequestState getFriendRequestState(String state) {
		if (state.equals("ACCEPTED")) {
        	return FriendRequestState.ACCEPTED;        	
        } else if (state.equals("DENIED")) {
        	return FriendRequestState.DENIED; 
        } else {
        	return FriendRequestState.PENDING;
        }
	}


}
