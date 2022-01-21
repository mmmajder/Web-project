package dao.chat;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;
import au.com.bytecode.opencsv.bean.CsvToBean;
import beans.Chat;
import beans.DM;
import beans.User;

public class ChatDAO {
	static final String CSV_FILE = "chats.csv";
	private static Map<String, Chat> chats = new HashMap<>();
	private String path;

	public ChatDAO() {
		this.path = "";
		readFile();
	}
	
	public static void main(String[] args) {
		ChatDAO dao = new ChatDAO("src");
		
		dao.add(new Chat(dao.generateId(), new ArrayList<>()));
		ArrayList<String> dms = new ArrayList<String>();
		dms.add("DM1");
		dms.add("DM2");
		dao.add(new Chat(dao.generateId(), dms));
		dao.writeFile();
	}

	public ChatDAO(String contextPath) {
		this.path = contextPath;
		readFile();
	}

	public Collection<Chat> findAll() {
		return chats.values();
	}
	
	public void add(Chat chat) {
		chats.put(chat.getId(), chat);
		writeFile();
	}
	// C000001
	public String generateId() {
		String number = String.format("%06d", findAll().size() + 1);
		StringBuilder sb = new StringBuilder();
		sb.append("C");
		sb.append(number);
		return sb.toString();

	}

	public Chat findById(String id) {
		for (Chat chat : findAll()) {
			if (chat.getId().equals(id)) {
				return chat;
			}
		}
		return null;
	}
	
	void writeFile() {
		try {
			OutputStream os = new FileOutputStream(this.path + "/resources/" + CSV_FILE);
			CSVWriter writer = new CSVWriter(new PrintWriter(new OutputStreamWriter(os, "UTF-8")), ';',
					CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);
			List<String[]> data = new ArrayList<String[]>();
			data.add(new String[] { "id", "dmIDs" });
			for (Chat c : findAll()) {
				data.add(new String[] { c.getId(), printList(c.getDms()) });
			}
			writer.writeAll(data);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String printList(List<?> elems) {
		StringBuilder sb = new StringBuilder();
		for (Object object : elems) {
			sb.append(object.toString());
			sb.append('|');
		}
		if (sb.length()!=0) { 
			sb.setLength(sb.length() - 1);
		}
		return sb.toString();
	}
	
	void readFile() {
		try (CSVReader csvr = new CSVReader(
				new InputStreamReader(new FileInputStream(this.path + "/resources/" + CSV_FILE), "UTF-8"), ';', '\'',
				1);) {
			ColumnPositionMappingStrategy<ChatRepo> strategy = new ColumnPositionMappingStrategy<>();
			strategy.setType(ChatRepo.class);
			String[] columns = new String[] { "id", "dmIDs" };
			strategy.setColumnMapping(columns);
			CsvToBean<ChatRepo> csv = new CsvToBean<>();
			List<ChatRepo> tempChats = csv.parse(strategy, csvr);

			for (ChatRepo tempChat : tempChats) {
				ArrayList<String> chatDms = getDMs(tempChat.getDmIDs());
				Chat chat = new Chat(tempChat.getId(), chatDms);
				chats.put(tempChat.getId(), chat);
				System.out.println(chat);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private ArrayList<String> getDMs(String s) {
		ArrayList<String> dms = new ArrayList<String>();
		for (String elem : s.split("\\|")) {
			dms.add(elem);
		}
		return dms;
	}

}
