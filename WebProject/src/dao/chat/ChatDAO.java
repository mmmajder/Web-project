package dao.chat;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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

public class ChatDAO {
	static final String CSV_FILE = "chats.csv";
	private Map<String, Chat> chats = new HashMap<>();
	private String path;

	public ChatDAO() {
		this.path = "";
		readFile();
	}

	public ChatDAO(String contextPath) {
		this.path = contextPath;
		readFile();
	}

	public Collection<Chat> findAll() {
		return chats.values();
	}

	public Chat findById(String id) {
		for (Chat chat : findAll()) {
			if (chat.getId().equals(id)) {
				return chat;
			}
		}
		return null;
	}

	void readFile() {
		try (CSVReader csvr = new CSVReader(new FileReader(this.path + "/resources/" + CSV_FILE), ';',
				CSVWriter.NO_QUOTE_CHARACTER, 1)) {
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
