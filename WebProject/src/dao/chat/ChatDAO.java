package dao.chat;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
import beans.Chat;
import beans.DM;
import beans.User;
import dao.dm.DmDAO;
import dao.person.UserDAO;
import services.messages.ChatHeadData;
import services.messages.DmData;

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

		ArrayList<String> participants = new ArrayList<String>();
		participants.add("U000001");
		participants.add("U000002");
		dao.add(new Chat(dao.generateId(), new ArrayList<>(), participants));
		ArrayList<String> dms = new ArrayList<String>();
		dms.add("DM1");
		dms.add("DM2");
		dao.add(new Chat(dao.generateId(), dms, participants));
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
	
	public ArrayList<DmData> getDmData(String chatId, DmDAO dmDao, UserDAO userDAO, User user) {
		Chat chat = findById(chatId);
		System.out.println(chat);
		ArrayList<DmData> list = new ArrayList<DmData>();
		User otherParticipant = userDAO.findById(getOtherParticipant(user, chat));
		for (String dmId : chat.getDms()) {
			System.out.println(dmId);
			DM dm = dmDao.findById(dmId);
			System.out.println(dm);
			list.add(new DmData(dm, user, otherParticipant));
		}
		return list;
	}

	public ArrayList<ChatHeadData> getChatsForUser(User user, UserDAO userDAO) {
		ArrayList<ChatHeadData> chatsUser = new ArrayList<ChatHeadData>();

		for (String chatId : user.getChats()) {
			Chat chat = findById(chatId);
			User otherParticipant = userDAO.findById(getOtherParticipant(user, chat));
			chatsUser.add(new ChatHeadData(chat, otherParticipant));
		}
		return chatsUser;
	}

	private String getOtherParticipant(User user, Chat chat) {
		for (String participant : chat.getParticipants()) {
			if (!user.getId().equals(participant)) {
				return participant;
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
			data.add(new String[] { "id", "participants", "dmIDs" });
			for (Chat c : findAll()) {
				data.add(new String[] { c.getId(), printList(c.getParticipants()), printList(c.getDms()) });
			}
			writer.writeAll(data);
			writer.close();
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

	void readFile() {
		try (CSVReader csvr = new CSVReader(
				new InputStreamReader(new FileInputStream(this.path + "/resources/" + CSV_FILE), "UTF-8"), ';', '\'',
				1);) {
			String[] nextLine;
			while ((nextLine = csvr.readNext()) != null) {
				ArrayList<String> chatDms = getList(nextLine[2]);
				ArrayList<String> participants = getList(nextLine[1]);

				Chat chat = new Chat(nextLine[0], chatDms, participants);
				chats.put(chat.getId(), chat);
				System.out.println(chat);
			}
			csvr.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private ArrayList<String> getList(String s) {
		ArrayList<String> elems = new ArrayList<String>();
		for (String elem : s.split("\\|")) {
			if (!elem.equals("")) {
				elems.add(elem);
			}
		}
		return elems;
	}

	

}
