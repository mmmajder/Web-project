package dao.friendRequest;

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
	
	public Collection<FriendRequest> findAll() {
		return friendRequests.values();
	}

	public FriendRequest findById(String id) {
		for (FriendRequest friendRequest : findAll()) {
			if (friendRequest.getId().equals(id)) {
				return friendRequest;
			}
		}
		return null;
	}
	
	void readFile() {
		try(CSVReader csvr = new CSVReader(new FileReader(this.path + "/resources/" + CSV_FILE), ';', CSVWriter.NO_QUOTE_CHARACTER, 1)) {
			ColumnPositionMappingStrategy<FriendRequestRepo> strategy = new ColumnPositionMappingStrategy<>();
			strategy.setType(FriendRequestRepo.class);
			String[] columns = new String[] {"id","senderID","recieverID","dateOfRequest","state"};
			strategy.setColumnMapping(columns);
			CsvToBean<FriendRequestRepo> csv = new CsvToBean<>();
			List<FriendRequestRepo> tempFriendRequests = csv.parse(strategy, csvr);
			
			for (FriendRequestRepo tempFriendRequest : tempFriendRequests) {
				LocalDateTime dateTime = getDateTime(tempFriendRequest.getDateOfRequest());
				FriendRequestState state = getState(tempFriendRequest.getState());
				FriendRequest friendRequest = new FriendRequest(tempFriendRequest.getId(), tempFriendRequest.getSenderID(), tempFriendRequest.getRecieverID(), dateTime, state);
				friendRequests.put(friendRequest.getId(), friendRequest);
			}
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
		return LocalDateTime.of(Integer.parseInt(date.split("-")[0]), 
								Integer.parseInt(date.split("-")[1]), 
								Integer.parseInt(date.split("-")[2]), 
								Integer.parseInt(time.split(":")[0]), 
								Integer.parseInt(time.split(":")[1]), 
								Integer.parseInt(time.split(":")[2]));
		
	}
}
