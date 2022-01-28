package services.messages;

import java.time.LocalDateTime;

import beans.Chat;
import beans.User;

public class ChatHeadData implements Comparable<ChatHeadData>{
	private Chat chat;
	private User otherParticipant;
	private String content;
	private LocalDateTime lastMessage;

	public ChatHeadData(Chat chat, User otherParticipant, String content, LocalDateTime lastMessage) {
		super();
		this.chat = chat;
		this.otherParticipant = otherParticipant;
		this.content = content;
		this.lastMessage = lastMessage;
	}

	public LocalDateTime getLastMessage() {
		return lastMessage;
	}

	public void setLastMessage(LocalDateTime lastMessage) {
		this.lastMessage = lastMessage;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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

	@Override
	public int compareTo(ChatHeadData o) {
		if (this.getLastMessage().isAfter(o.getLastMessage())) {
			return -1;
		} else {
			return 1;
		}
	}

}
