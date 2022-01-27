package services.messages;

import beans.Chat;
import beans.User;

public class DmAddition {
	private Chat chat;
	private User sender;
	private User reciever;
	private String content;

	public DmAddition(Chat chat, User sender, User reciever, String content) {
		super();
		this.chat = chat;
		this.sender = sender;
		this.reciever = reciever;
		this.content = content;
	}

	public Chat getChat() {
		return chat;
	}

	public void setChat(Chat chat) {
		this.chat = chat;
	}

	public DmAddition() {
		super();
	}

	public User getReciever() {
		return reciever;
	}

	public void setReciever(User reciever) {
		this.reciever = reciever;
	}

	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
