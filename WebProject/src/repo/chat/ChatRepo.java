package repo.chat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.fasterxml.jackson.databind.ObjectMapper;

import beans.Chat;

public class ChatRepo {
	public static ArrayList<Chat> chats;
	
	public static void main(String[] args) {
		chats = new ArrayList<Chat>();
		readFile();
		for (Chat string : chats) {
			System.out.println(string);
		}
		updateFile();
	}
	
	
	
	@SuppressWarnings("unchecked")
	public static void readFile() {
		JSONParser jsonParser = new JSONParser();
        
        try (FileReader reader = new FileReader("resources/chats.json"))
        {
            //Read JSON file
            Object obj = jsonParser.parse(reader);
 
            JSONArray chatList = (JSONArray) obj;
             
            //Iterate over employee array
            chatList.forEach( chat -> parse( (JSONObject) chat ) );
 
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
	        mapper.writeValue(new File("resources/chats.json"), chats );
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }  
	}
	
	private static void parse(JSONObject chat) 
    {
        String id = (String) chat.get("id");
        ArrayList<String> dmIDs = getList((JSONArray) chat.get("dmIDs"));
        chats.add(new Chat(id, dmIDs));
    }
	
	private static ArrayList<String> getList(JSONArray jsonArray) {
		ArrayList<String> list = new ArrayList<String>();
        for (Object elem : jsonArray) {
        	list.add((String) elem);
		}
        return list;
	}

	
}
