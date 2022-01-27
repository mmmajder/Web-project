package services.messages;

import java.util.ArrayList;

import beans.Chat;
import beans.DM;
import beans.User;

public class DmData {
	private Chat chat;
	private ArrayList<DM> dms;
	private User loggedUser;
	private User otherUser;

	public DmData(Chat chat, ArrayList<DM> dms, User loggedUser, User otherUser) {
		super();
		this.chat = chat;
		this.dms = dms;
		this.loggedUser = loggedUser;
		this.otherUser = otherUser;
	}

	public DmData() {
		super();
	}

	public Chat getChat() {
		return chat;
	}

	public void setChat(Chat chat) {
		this.chat = chat;
	}

	public ArrayList<DM> getDms() {
		return dms;
	}

	public void setDms(ArrayList<DM> dms) {
		this.dms = dms;
	}

	public User getLoggedUser() {
		return loggedUser;
	}

	public void setLoggedUser(User loggedUser) {
		this.loggedUser = loggedUser;
	}

	public User getOtherUser() {
		return otherUser;
	}

	public void setOtherUser(User otherUser) {
		this.otherUser = otherUser;
	}

}
