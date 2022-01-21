package dao.friendRequest;

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
import beans.FriendRequest;
import enums.FriendRequestState;

public class FriendRequestDAO {
	static final String CSV_FILE = "friendRequests.csv";
	private Map<String, FriendRequest> friendRequests = new HashMap<>();
	private String path;

	public FriendRequestDAO(String contextPath) {
		this.path = contextPath;
		readFile();
	}

	public static void main(String[] args) {
		FriendRequestDAO dao = new FriendRequestDAO("src");
		dao.add(new FriendRequest(dao.generateId(), "send", "reciever", LocalDateTime.now(), FriendRequestState.ACCEPTED));
		dao.add(new FriendRequest(dao.generateId(), "send", "reciever", LocalDateTime.now(), FriendRequestState.ACCEPTED));
	}

	public Collection<FriendRequest> findAll() {
		return friendRequests.values();
	}

	public void add(FriendRequest fr) {
		friendRequests.put(fr.getId(), fr);
		writeFile();
	}
	//FR000002
	public String generateId() {
		StringBuilder sb = new StringBuilder();
		String number = String.format("%06d", findAll().size() + 1);
		sb.append("FR");
		sb.append(number);
		return sb.toString();
	}

	public FriendRequest findById(String id) {
		for (FriendRequest friendRequest : findAll()) {
			if (friendRequest.getId().equals(id)) {
				return friendRequest;
			}
		}
		return null;
	}

	void writeFile() {
		try {
			CSVWriter writer = new CSVWriter(new FileWriter(this.path + "/resources/" + CSV_FILE), ';',
					CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);
			List<String[]> data = new ArrayList<String[]>();
			data.add(new String[] { "id", "senderID", "recieverID", "dateOfRequest", "state" });
			for (FriendRequest fr : findAll()) {
				data.add(new String[] { fr.getId(), fr.getSender(), fr.getReciever(),
						fr.getDateOfRequest().toString().replace('T', ' '), fr.getState().toString() });
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
			String[] nextLine;
			// String[] columns = new String[]
			// {"id","senderID","recieverID","dateOfRequest","state"};
			while ((nextLine = csvr.readNext()) != null) {
				LocalDateTime dateOfRequest = getDateTime(nextLine[3]);
				FriendRequest friendRequest = new FriendRequest(nextLine[0], nextLine[1], nextLine[2], dateOfRequest,
						getState(nextLine[4]));
				friendRequests.put(friendRequest.getId(), friendRequest);
			}
			csvr.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private FriendRequestState getState(String state) {
		if (state.equals("PENDING")) {
			return FriendRequestState.PENDING;
		} else if (state.equals("ACCEPTED")) {
			return FriendRequestState.ACCEPTED;
		} else {
			return FriendRequestState.DENIED;
		}
	}

	private LocalDateTime getDateTime(String s) {
		String date = s.split(" ")[0];
		String time = s.split(" ")[1];
		return LocalDateTime.of(Integer.parseInt(date.split("-")[0]), Integer.parseInt(date.split("-")[1]),
				Integer.parseInt(date.split("-")[2]), Integer.parseInt(time.split(":")[0]),
				Integer.parseInt(time.split(":")[1]), (int) Double.parseDouble(time.split(":")[2]));

	}
}
