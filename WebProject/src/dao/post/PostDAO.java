package dao.post;

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
import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;
import au.com.bytecode.opencsv.bean.CsvToBean;
import beans.Comment;
import beans.Post;
import beans.User;

public class PostDAO {
	static final String CSV_FILE = "posts.csv";
	private Map<String, Post> posts = new HashMap<>();
	private String path;

	public PostDAO(String contextPath) {
		this.path = contextPath;
		readFile();
	}

	public static void main(String[] args) {
		PostDAO dao = new PostDAO("src");
		dao.add(new Post(dao.generateId(), "author", "picture", "description", LocalDateTime.now(), new ArrayList<>(), false, false));
	}

	public Collection<Post> findAll() {
		return posts.values();
	}

	public void add(Post post) {
		posts.put(post.getId(), post);
		writeFile();
	}
	// PO00001
	public String generateId() {
		StringBuilder sb = new StringBuilder();
		String number = String.format("%05d", findAll().size()+1);
		sb.append("PO");
		sb.append(number);
		return sb.toString();
	}

	public Post findById(String id) {
		for (Post post : findAll()) {
			if (post.getId().equals(id)) {
				return post;
			}
		}
		return null;
	}

	void writeFile() {
		try {
			CSVWriter writer = new CSVWriter(new FileWriter(this.path + "/resources/" + CSV_FILE), ';',
					CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);
			List<String[]> data = new ArrayList<String[]>();
			data.add(new String[] { "id", "authorID", "picture", "description", "posted", "commentIDs", "deleted",
					"isPicture" });
			for (Post p : findAll()) {
				data.add(new String[] { p.getId(), p.getAuthor(), p.getPicture(), p.getDescription(),
						p.getPosted().toString().replace('T', ' '), printList(p.getComments()),
						new Boolean(p.isDeleted()).toString(), new Boolean(p.isPicture()).toString() });
			}
			writer.writeAll(data);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void readFile() {
		try (CSVReader csvr = new CSVReader(new FileReader(this.path + "/resources/" + CSV_FILE), ';',
				CSVWriter.NO_QUOTE_CHARACTER, 1)) {
			// String[] columns = new String[]
			// {"id","authorID","picture","description","posted","commentIDs","deleted",
			// "isPicture"};
			String[] nextLine;
			// csvr.readNext(); // skip first line
			while ((nextLine = csvr.readNext()) != null) {
				LocalDateTime posted = getDateTime(nextLine[4]);
				Post post = new Post(nextLine[0], nextLine[1], nextLine[2], nextLine[3], posted, getList(nextLine[5]),
						new Boolean(nextLine[6]), new Boolean(nextLine[7]));
				posts.put(post.getId(), post);
				System.out.println(post);
			}
			csvr.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private String printList(List<?> elems) {
		StringBuilder sb = new StringBuilder();
		for (Object object : elems) {
			sb.append(object.toString());
			sb.append('|');
		}
		if (sb.length() != 0) {
			sb.setLength(sb.length() - 1);
		}
		return sb.toString();
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
		return LocalDateTime.of(Integer.parseInt(date.split("-")[0]), Integer.parseInt(date.split("-")[1]),
				Integer.parseInt(date.split("-")[2]), Integer.parseInt(time.split(":")[0]),
				Integer.parseInt(time.split(":")[1]));

	}
	
	public ArrayList<Post> getUserPhotos(User u) {
		ArrayList<Post> photos = new ArrayList<Post>();
		for (String post : u.getPosts()) {
			if(posts.get(post).isPicture()) {
				photos.add(posts.get(post));
				System.out.println(posts.get(post));
			}
		}
		return photos;
	}
	
	public ArrayList<Post> getUserPosts(User u) {
		ArrayList<Post> retPosts = new ArrayList<Post>();
		for (String post : u.getPosts()) {
			if(!posts.get(post).isPicture())
				retPosts.add(posts.get(post));
		}
		return retPosts;
	}

}
