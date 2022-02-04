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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import beans.FriendRequest;
import beans.User;
import enums.FriendRequestState;
import services.search.UserSearchData;

public class FriendRequestDAO {
	static final String CSV_FILE = "friendRequests.csv";
	private Map<String, FriendRequest> friendRequests = new HashMap<>();
	private RepositoryDAO repository = new RepositoryDAO();
	private String path;

	public FriendRequestDAO(String contextPath) {
		this.path = contextPath;
		readFile();
	}

	public static void main(String[] args) {
		FriendRequestDAO dao = new FriendRequestDAO("src");
		for (FriendRequest x : dao.findAll()) {
			System.out.println(x);
		}
	}

	public Collection<FriendRequest> findAll() {
		return friendRequests.values();
	}

	public FriendRequest getById(String id) {
		return friendRequests.get(id);
	}
	
	public String createFriendRequest(String senderId, String receiverId) {
		String id = generateId();
		add(new FriendRequest(id, senderId, receiverId, LocalDateTime.now(), FriendRequestState.PENDING));
		return id;
	}

	public void add(FriendRequest fr) {
		friendRequests.put(fr.getId(), fr);
		writeFile();
	}

	public ArrayList<FriendRequest> getPending(User user) {
		ArrayList<FriendRequest> pendingFriendRequests = new ArrayList<FriendRequest>();
		for (String r : user.getFriendRequests()) {
			FriendRequest friendRequest = getById(r);
			if (friendRequest != null && !friendRequest.getSender().equals(user.getId())) {
				if (friendRequest.getState() == FriendRequestState.PENDING) {
					pendingFriendRequests.add(friendRequest);
				}
			}
		}
		return pendingFriendRequests;
	}

	public boolean isPending(User one, User other) {
		for (String r : one.getFriendRequests()) {
			FriendRequest friendRequest = getById(r);
			if (friendRequest != null) {
				if (friendRequest.getSender().equals(one.getId()) && friendRequest.getReciever().equals(other.getId())
						&& friendRequest.getState() == FriendRequestState.PENDING) {
					return true;
				}
			}
		}
		return false;
	}

	// FR000002
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
			OutputStream os = new FileOutputStream(repository.getPath() + "/resources/" + CSV_FILE);
			CSVWriter writer = new CSVWriter(new PrintWriter(new OutputStreamWriter(os, "UTF-8")), ';',
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
		try (CSVReader csvr = new CSVReader(
				new InputStreamReader(new FileInputStream(repository.getPath() + "/resources/" + CSV_FILE), "UTF-8"),
				';', '\'', 1);) {
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

	public ArrayList<UserSearchData> getPrintData(User user, UserDAO dao) {
		ArrayList<UserSearchData> data = new ArrayList<UserSearchData>();
		for (FriendRequest friendRequest : getPending(user)) {
			User sender = dao.findById(friendRequest.getSender());
			data.add(new UserSearchData(sender.getId(), sender.getName(), sender.getSurname(),
					sender.getProfilePicture(), dao.getNumberOfMutualFriends(user, sender)));
		}
		return data;
	}

	public void changeStatus(User sender, User receiver, FriendRequestState state) {
		for (String freqID : receiver.getFriendRequests()) {
			FriendRequest request = findById(freqID);
			if (request.getSender().equals(sender.getId())) {
				request.setState(state);
				writeFile();
				return;
			}
		}
	}

	public String getRequestId(String senderId, String id) {
		for (FriendRequest request : findAll()) {
			if (request.getSender().equals(senderId) && request.getReciever().equals(id)) {
				return request.getId();
			}
		}
		return null;
	}

	public void deleteRequest(String requestId) {
		friendRequests.remove(requestId);
		writeFile();
	}
}
