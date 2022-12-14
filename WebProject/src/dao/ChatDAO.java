package dao;

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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import beans.Chat;
import beans.DM;
import beans.Post;
import beans.User;
import dao.DmDAO;
import dao.UserDAO;
import services.messages.ChatHeadData;
import services.messages.DmData;

public class ChatDAO {
	static final String CSV_FILE = "chats.csv";
	private static Map<String, Chat> chats = new HashMap<>();
	private String path;
	private RepositoryDAO repository = new RepositoryDAO();

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

	public void add(Chat chat) {
		chats.put(chat.getId(), chat);
		writeFile();
	}

	public void addDM(DM dm, Chat chat) {
		Chat selectedChat = findById(chat.getId());
		selectedChat.getDms().add(dm.getId());
		selectedChat.setSeen(false);
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

	public DmData getDmData(String chatId, DmDAO dmDao, UserDAO userDAO, User user) {
		Chat chat = findById(chatId);
		ArrayList<DM> dms = new ArrayList<DM>();
		User otherParticipant = userDAO.findById(getOtherParticipant(user, chat));
		for (String dmId : chat.getDms()) {
			DM dm = dmDao.findById(dmId);
			dms.add(dmDao.findById(dmId));
		}
		return new DmData(chat, dms, user, otherParticipant);
	}

	public ArrayList<ChatHeadData> getChatsForUser(User user, UserDAO userDAO, DmDAO dmDAO) {
		ArrayList<ChatHeadData> chatsUser = new ArrayList<ChatHeadData>();

		for (String chatId : user.getChats()) {
			Chat chat = findById(chatId);
			User otherParticipant = userDAO.findById(getOtherParticipant(user, chat));
			DM dm = getLastDM(chat, dmDAO);
			if (dm == null) {
				chatsUser.add(new ChatHeadData(chat, otherParticipant, "", null, null));
			} else {
				chatsUser.add(
						new ChatHeadData(chat, otherParticipant, dm.getContent(), dm.getDateTime(), dm.getSender()));
			}

		}
		return sortChatHeads(chatsUser);
	}

	public DM getLastDM(Chat chat, DmDAO dmDAO) {
		if (chat.getDms().size() == 0) {
			return null;
		}
		return dmDAO.findById(chat.getDms().get(chat.getDms().size() - 1));
	}

	public ArrayList<ChatHeadData> sortChatHeads(ArrayList<ChatHeadData> chats) {
		Collections.sort(chats);
		return chats;
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
			OutputStream os = new FileOutputStream(repository.getPath() + "/resources/" + CSV_FILE);
			CSVWriter writer = new CSVWriter(new PrintWriter(new OutputStreamWriter(os, "UTF-8")), ';',
					CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);
			List<String[]> data = new ArrayList<String[]>();
			data.add(new String[] { "id", "participants", "dmIDs", "seen" });
			for (Chat c : findAll()) {
				data.add(new String[] { c.getId(), printList(c.getParticipants()), printList(c.getDms()),
						new Boolean(c.isSeen()).toString() });
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
				new InputStreamReader(new FileInputStream(repository.getPath() + "/resources/" + CSV_FILE), "UTF-8"), ';', '\'',
				1);) {
			String[] nextLine;
			while ((nextLine = csvr.readNext()) != null) {
				ArrayList<String> chatDms = getList(nextLine[2]);
				ArrayList<String> participants = getList(nextLine[1]);

				Chat chat = new Chat(nextLine[0], chatDms, participants, new Boolean(nextLine[3]));
				chats.put(chat.getId(), chat);
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

	public void seenMessage(DmData chatData, User user) {
		if (chatData.getDms().size() != 0) {
			if (!chatData.getDms().get(chatData.getDms().size() - 1).getSender().equals(user.getId())) {
				findById(chatData.getChat().getId()).setSeen(true);
				writeFile();
			}
		}
	}

	public Chat getChatForUsers(User user1, User user2) {
		for (String chat1Ids : user1.getChats()) {
			for (String chat2Ids : user2.getChats()) {
				if (chat1Ids.equals(chat2Ids)) {
					return findById(chat1Ids);
				}
			}
		}
		return null;
	}

	public Chat createChat(User user1, User user2) {
		ArrayList<String> participants = new ArrayList<String>();
		participants.add(user1.getId());
		participants.add(user2.getId());
		Chat chat = new Chat(generateId(), new ArrayList<String>(), participants, true);
		add(chat);
		return chat;
	}
}