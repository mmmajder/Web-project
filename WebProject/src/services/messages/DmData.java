package services.messages;

import beans.DM;
import beans.User;

public class DmData {
	private DM dm;
	private User loggedUser;
	private User otherUser;

	public DmData(DM dm, User loggedUser, User otherUser) {
		super();
		this.dm = dm;
		this.loggedUser = loggedUser;
		this.otherUser = otherUser;
	}

	public DM getDm() {
		return dm;
	}

	public void setDm(DM dm) {
		this.dm = dm;
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
