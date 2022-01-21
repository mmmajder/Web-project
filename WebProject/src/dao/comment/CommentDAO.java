package dao.comment;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import beans.Comment;

public class CommentDAO {
	static final String CSV_FILE = "comments.csv";
	private Map<String, Comment> comments = new HashMap<>();
	private String path;
	
	public CommentDAO(String contextPath) {
		this.path = contextPath;
		readFile();
	}
	public static void main(String[] args) {
		CommentDAO dao = new CommentDAO("src");
		dao.addComment(new Comment("COM-00000004", "Cao%20lepa", "U00001", LocalDateTime.of(2022, 01, 20, 23, 44, 15),LocalDateTime.of(2022, 01, 20, 23, 44, 15), false));
	}
	
	public Collection<Comment> findAll() {
		readFile();
		return comments.values();
	}
	
	public void addComment(Comment comment) {
		System.out.println(comment);
		comments.put(comment.getId(), comment);
		System.out.println(findAll().size());
		writeFile();
	}
	

	
	public Comment findById(String id) {
		for (Comment comment : findAll()) {
			if (comment.getId().equals(id)) {
				return comment;
			}
		}
		return null;
	}
	
	public String generateId() {
		StringBuilder sb = new StringBuilder();
		String number = String.format("%08d", findAll().size()+1);
		sb.append("COM-");
		sb.append(number);
		return sb.toString();
	}
	
	void writeFile() {
		try {
			CSVWriter writer = new CSVWriter(new FileWriter(this.path + "/resources/" + CSV_FILE), ';',
			        CSVWriter.NO_QUOTE_CHARACTER,
			        CSVWriter.DEFAULT_ESCAPE_CHARACTER,
			        CSVWriter.DEFAULT_LINE_END);
			List<String[]> data = new ArrayList<String[]>();
			data.add(new String[] {"id","text","authorID","created","lastEdited","isDeleted"});
			for (Comment c : findAll()) {
				data.add(new String[] { c.getId(), c.getText(), c.getAuthor(), c.getCreated().toString().replace('T', ' '), c.getLastEdited().toString().replace('T', ' '), new Boolean(c.isDeleted()).toString()});
			}
			writer.writeAll(data);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	void readFile() {
		System.out.println(this.path + "/resources/" + CSV_FILE);
		try(CSVReader csvr = new CSVReader(new FileReader(this.path + "/resources/" + CSV_FILE), ';', CSVWriter.NO_QUOTE_CHARACTER, 1)) {
			String[] nextLine;
			//csvr.readNext();	// skip first line
			while ((nextLine = csvr.readNext()) != null) {
				System.out.println(nextLine[0]);
				System.out.println(nextLine[1]);
				System.out.println(nextLine[2]);
				System.out.println(nextLine[3]);
				System.out.println(nextLine[4]);
				System.out.println(nextLine[5]);
				
				LocalDateTime created = getDateTime(nextLine[3]);
				LocalDateTime lastEdited = getDateTime(nextLine[4]);
				Comment comment = new Comment(nextLine[0], nextLine[1], nextLine[2], created, lastEdited, getBool(nextLine[5]));
				System.out.println(comment);
				comments.put(comment.getId(), comment);
			}
			csvr.close();
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
								(int) Double.parseDouble(time.split(":")[2]));
		
	}
	private boolean getBool(String b) {
		if (b.equals("false")) {
			return false;
		}
		return true;
	}
	
}
