package repo.comment;

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

import beans.Comment;
import repo.DateManager;

public class CommentRepo {
	public static ArrayList<Comment> comments;

	
	public static void main(String[] args) {
		comments = new ArrayList<Comment>();
		readFile();
		for (Comment string : comments) {
			System.out.println(string);
		}
		updateFile();
	}
	
	@SuppressWarnings("unchecked")
	public static void readFile() {
		JSONParser jsonParser = new JSONParser();
        
        try (FileReader reader = new FileReader("resources/comments.json"))
        {
            //Read JSON file
            Object obj = jsonParser.parse(reader);
 
            JSONArray commentList = (JSONArray) obj;
             
            //Iterate over employee array
            commentList.forEach( comment -> parse( (JSONObject) comment ) );
 
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
	        mapper.writeValue(new File("resources/comments.json"), comments );
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }  
	}
	
	private static void parse(JSONObject comment) 
    {
		String id = (String) comment.get("id");
		String text = (String) comment.get("text");
		String authorID = (String) comment.get("authorID");
		LocalDateTime lastEdited = DateManager.getDateTime((JSONObject) comment.get("lastEdited"));
		LocalDateTime created = DateManager.getDateTime((JSONObject) comment.get("created"));
		boolean isDeleted = (boolean) comment.get("deleted"); 
		
		comments.add(new Comment(id, text, authorID, created, lastEdited, isDeleted));
    }
}
