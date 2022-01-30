package dao.comment;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import beans.Comment;
import beans.Post;
import beans.User;
import services.profile.CommentReturnData;

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
		dao.addComment(
				new Comment(dao.generateId(), "Cao%20lepa", "U00001", LocalDateTime.now(), LocalDateTime.now(), false));
	}

	public Collection<Comment> findAll() {
		readFile();
		return comments.values();
	}

	public void addComment(Comment comment) {
		comments.put(comment.getId(), comment);
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
		String number = String.format("%08d", findAll().size() + 1);
		sb.append("COM-");
		sb.append(number);
		return sb.toString();
	}

	void writeFile() {
		try {
			OutputStream os = new FileOutputStream(this.path + "/resources/" + CSV_FILE);
			CSVWriter writer = new CSVWriter(new PrintWriter(new OutputStreamWriter(os, "UTF-8")), ';',
					CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);
			List<String[]> data = new ArrayList<String[]>();
			data.add(new String[] { "id", "text", "authorID", "created", "lastEdited", "isDeleted" });
			for (Comment c : findAll()) {
				data.add(new String[] { c.getId(), c.getText(), c.getAuthor(),
						c.getCreated().toString().replace('T', ' '), c.getLastEdited().toString().replace('T', ' '),
						new Boolean(c.isDeleted()).toString() });
			}
			writer.writeAll(data);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void readFile() {
		try (CSVReader csvr = new CSVReader(
				new InputStreamReader(new FileInputStream(this.path + "/resources/" + CSV_FILE), "UTF-8"), ';', '\'',
				1);) {
			String[] nextLine;
			// csvr.readNext(); // skip first line
			while ((nextLine = csvr.readNext()) != null) {
				LocalDateTime created = getDateTime(nextLine[3]);
				LocalDateTime lastEdited = getDateTime(nextLine[4]);
				Comment comment = new Comment(nextLine[0], nextLine[1], nextLine[2], created, lastEdited,
						getBool(nextLine[5]));
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
		return LocalDateTime.of(Integer.parseInt(date.split("-")[0]), Integer.parseInt(date.split("-")[1]),
				Integer.parseInt(date.split("-")[2]), Integer.parseInt(time.split(":")[0]),
				Integer.parseInt(time.split(":")[1]), (int) Double.parseDouble(time.split(":")[2]));
	}

	private boolean getBool(String b) {
		return b.equals("true");
	}

	public ArrayList<CommentReturnData> getCommentsOnPost(Post post, User user) {
		ArrayList<CommentReturnData> comments = new ArrayList<CommentReturnData>();
		for (String id : post.getComments()) {
			Comment comment = this.findById(id);
			if (!comment.isDeleted()) {
				comments.add(new CommentReturnData(comment.getId(), comment.getText(), user.getId(), user.getName(),
						user.getSurname(), comment.getCreated(), comment.getLastEdited(), user.getProfilePicture()));
			}
		}
		return comments;
	}

	public void editComment(String commentID, String text) {
		Comment comment = comments.get(commentID);
		comment.setText(text);
		comment.setLastEdited(LocalDateTime.now());
		writeFile();
	}

	public void deleteComment(String commentID) {
		Comment comment = comments.get(commentID);
		comment.setDeleted(true);
		writeFile();
	}

}
