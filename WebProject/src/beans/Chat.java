package beans;

import java.util.ArrayList;

public class Chat {
	private String id;
	private ArrayList<String> dms;
	private ArrayList<String> participants;

	public Chat(String id, ArrayList<String> dms, ArrayList<String> participants) {
		super();
		this.id = id;
		this.dms = dms;
		this.participants = participants;
	}

	public ArrayList<String> getParticipants() {
		return participants;
	}

	public void setParticipants(ArrayList<String> participants) {
		this.participants = participants;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ArrayList<String> getDms() {
		return dms;
	}

	public void setDms(ArrayList<String> dms) {
		this.dms = dms;
	}

	@Override
	public String toString() {
		return "Chat [id=" + id + ", dms=" + dms + "]";
	}

}
