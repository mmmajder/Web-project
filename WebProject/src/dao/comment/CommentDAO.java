package dao.comment;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;
import au.com.bytecode.opencsv.bean.CsvToBean;
import beans.Comment;

public class CommentDAO {
	static final String CSV_FILE = "comments.csv";
	private Map<String, Comment> comments = new HashMap<>();
	private String path;
	
	public CommentDAO(String contextPath) {
		this.path = contextPath;
		readFile();
	}
	
	public Collection<Comment> findAll() {
		return comments.values();
	}

	public Comment findById(String id) {
		for (Comment comment : findAll()) {
			if (comment.getId().equals(id)) {
				return comment;
			}
		}
		return null;
	}
	
	void readFile() {
		try(CSVReader csvr = new CSVReader(new FileReader(this.path + "/resources/" + CSV_FILE), ';', CSVWriter.NO_QUOTE_CHARACTER, 1)) {
			ColumnPositionMappingStrategy<CommentRepo> strategy = new ColumnPositionMappingStrategy<>();
			strategy.setType(CommentRepo.class);
			String[] columns = new String[] {"id","text","authorID","created","lastEdited","isDeleted"};
			strategy.setColumnMapping(columns);
			CsvToBean<CommentRepo> csv = new CsvToBean<>();
			List<CommentRepo> tempComments = csv.parse(strategy, csvr);
			
			for (CommentRepo tempComment : tempComments) {
				LocalDateTime created = getDateTime(tempComment.getCreated());
				LocalDateTime lastEdited = getDateTime(tempComment.getCreated());
				Comment comment = new Comment(tempComment.getId(), tempComment.getText(), tempComment.getAuthorID(), created, lastEdited, tempComment.isDeleted());
				comments.put(comment.getId(), comment);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

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
