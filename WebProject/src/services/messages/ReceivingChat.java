package services.messages;

public class ReceivingChat {
	public String sender;
	public String receiver;

	public ReceivingChat(String sender, String receiver) {
		super();
		this.sender = sender;
		this.receiver = receiver;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

}
