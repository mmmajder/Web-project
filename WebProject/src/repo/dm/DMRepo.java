package repo.dm;

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

import beans.DM;
import repo.DateManager;

public class DMRepo {
	public static ArrayList<DM> dms;
	
	public static void main(String[] args) {
		dms = new ArrayList<DM>();
		readFile();
		for (DM string : dms) {
			System.out.println(string);
		}
		updateFile();
	}
	
	
	
	@SuppressWarnings("unchecked")
	public static void readFile() {
		JSONParser jsonParser = new JSONParser();
        
        try (FileReader reader = new FileReader("resources/dms.json"))
        {
            //Read JSON file
            Object obj = jsonParser.parse(reader);
 
            JSONArray dmList = (JSONArray) obj;
             
            //Iterate over employee array
            dmList.forEach( dm -> parse( (JSONObject) dm ) );
 
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
	        mapper.writeValue(new File("resources/dms.json"), dms );
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }  
	}
	
	private static void parse(JSONObject dm) 
    {
        String id = (String) dm.get("id");
        String content = (String) dm.get("content");
        LocalDateTime dateTime = DateManager.getDateTime((JSONObject) dm.get("dateTime"));
        String senderID = (String) dm.get("senderID");
        String recieverID = (String) dm.get("recieverID");
        dms.add(new DM(id, content, dateTime, senderID, recieverID));
    }

}
