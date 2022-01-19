package beans;

import java.util.ArrayList;

public class Chat {
	private String id;
	private ArrayList<String> dmIDs;
	public Chat(String id, ArrayList<String> dmIDs) {
		super();
		this.id = id;
		this.dmIDs = dmIDs;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ArrayList<String> getDmIDs() {
		return dmIDs;
	}

	public void setDmIDs(ArrayList<String> dmIDs) {
		this.dmIDs = dmIDs;
	}


	@Override
	public String toString() {
		return "Chat [id=" + id + ", dmIDs=" + dmIDs + "]";
	}
	
	
	
	
}
