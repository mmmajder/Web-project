package services.messages;

import beans.Chat;
import beans.User;

public class ChatHeadData {
	private Chat chat;
	private User otherParticipant;

	public ChatHeadData(Chat chat, User otherParticipant) {
		super();
		this.chat = chat;
		this.otherParticipant = otherParticipant;
	}

	public Chat getChat() {
		return chat;
	}

	public void setChat(Chat chat) {
		this.chat = chat;
	}

	public User getOtherParticipant() {
		return otherParticipant;
	}

	public void setOtherParticipant(User otherParticipant) {
		this.otherParticipant = otherParticipant;
	}

}
