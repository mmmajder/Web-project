package repo.picture;

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

import beans.Picture;
import repo.DateManager;

public class PictureRepo {
	public static ArrayList<Picture> pictures;

	
	public static void main(String[] args) {
		pictures = new ArrayList<Picture>();
		readFile();
		for (Picture string : pictures) {
			System.out.println(string);
		}
		updateFile();
	}
	
	@SuppressWarnings("unchecked")
	public static void readFile() {
		JSONParser jsonParser = new JSONParser();
        
        try (FileReader reader = new FileReader("resources/pictures.json"))
        {
            //Read JSON file
            Object obj = jsonParser.parse(reader);
 
            JSONArray postList = (JSONArray) obj;
             
            //Iterate over employee array
            postList.forEach( post -> parse( (JSONObject) post ) );
 
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
	        mapper.writeValue(new File("resources/pictures.json"), pictures );
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }  
	}
	
	private static void parse(JSONObject post) 
    {
		String id = (String) post.get("id");
		String author = (String) post.get("authorID");
		String picture = (String) post.get("picture");
		String description = (String) post.get("description");
		ArrayList<String> comments = getList((JSONArray) post.get("commentIDs"));
		boolean isDeleted = (boolean) post.get("deleted"); 
		LocalDateTime posted = DateManager.getDateTime((JSONObject) post.get("posted"));
		
		pictures.add(new Picture(id, author, picture, description, posted, comments, isDeleted));
    }
	private static ArrayList<String> getList(JSONArray jsonArray) {
		ArrayList<String> list = new ArrayList<String>();
        for (Object elem : jsonArray) {
        	list.add((String) elem);
		}
        return list;
	}

}
