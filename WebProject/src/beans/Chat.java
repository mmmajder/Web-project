package beans;

import java.util.ArrayList;

public class Chat {
	private String id;
	private ArrayList<String> dms;
	
	public Chat(String id, ArrayList<String> dms) {
		super();
		this.id = id;
		this.dms = dms;
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
