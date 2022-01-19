package dao.post;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;
import au.com.bytecode.opencsv.bean.CsvToBean;
import beans.Post;

public class PostDAO {
	static final String CSV_FILE = "posts.csv";
	private Map<String, Post> posts = new HashMap<>();
	private String path;
	
	public PostDAO(String contextPath) {
		this.path = contextPath;
		readFile();
	}
	
	public Collection<Post> findAll() {
		return posts.values();
	}

	public Post findById(String id) {
		for (Post post : findAll()) {
			if (post.getId().equals(id)) {
				return post;
			}
		}
		return null;
	}
	
	void readFile() {
		try(CSVReader csvr = new CSVReader(new FileReader(this.path + "/resources/" + CSV_FILE), ';', CSVWriter.NO_QUOTE_CHARACTER, 1)) {
			ColumnPositionMappingStrategy<PostRepo> strategy = new ColumnPositionMappingStrategy<>();
			strategy.setType(PostRepo.class);
			String[] columns = new String[] {"id","authorID","picture","description","posted","commentIDs","deleted"};
			strategy.setColumnMapping(columns);
			
			CsvToBean<PostRepo> csv = new CsvToBean<>();
			List<PostRepo> tempPosts = csv.parse(strategy, csvr);
			
			for (PostRepo temp : tempPosts) {
				LocalDateTime posted = getDateTime(temp.getPosted());
				ArrayList<String> commentIDs = getList(temp.getCommentIDs());
				Post post = new Post(temp.getId(), temp.getAuthorID(), temp.getPicture(), temp.getDescription(), posted, commentIDs, temp.isDeleted());
				posts.put(temp.getId(), post);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	private ArrayList<String> getList(String s) {
		ArrayList<String> elems = new ArrayList<String>();
		for (String elem : s.split("\\|")) {
			elems.add(elem);
		}
		return elems;
	}
	private LocalDateTime getDateTime(String s) {
		String date = s.split(" ")[0];
		String time = s.split(" ")[1];
		return LocalDateTime.of(Integer.parseInt(date.split("-")[0]), 
								Integer.parseInt(date.split("-")[1]), 
								Integer.parseInt(date.split("-")[2]), 
								Integer.parseInt(time.split(":")[0]), 
								Integer.parseInt(time.split(":")[1]), 
								Integer.parseInt(time.split(":")[2]));
		
	}

}
