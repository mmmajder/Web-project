package services.friendRequest;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.Chat;
import beans.User;
import dao.ChatDAO;
import dao.FriendRequestDAO;
import dao.UserDAO;
import enums.FriendRequestState;
import services.search.UserSearchData;

@Path("/friendRequest")
public class FriendRequestService {
	@Context
	ServletContext ctx;

	@PostConstruct
	public void init() {
		String contextPath = ctx.getRealPath("");
		if (ctx.getAttribute("friendRequestDAO") == null) {
			ctx.setAttribute("friendRequestDAO", new FriendRequestDAO(contextPath));
		}
		if (ctx.getAttribute("userDAO") == null) {
			ctx.setAttribute("userDAO", new UserDAO(contextPath));
		}
		if (ctx.getAttribute("chatDAO") == null) {
			ctx.setAttribute("chatDAO", new ChatDAO(contextPath));
		}
	}

	@GET
	@Path("/getRequests")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<UserSearchData> getFriendRequests(@Context HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("logged");
		FriendRequestDAO dao = (FriendRequestDAO) ctx.getAttribute("friendRequestDAO");
		UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
		return dao.getPrintData(user, userDAO);
	}

	@POST
	@Path("/accept")
	@Consumes(MediaType.APPLICATION_JSON)
	public void acceptRequest(@Context HttpServletRequest request, String senderId) {
		User user = (User) request.getSession().getAttribute("logged");
		UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
		User sender = userDAO.findById(senderId);
		System.out.println("sender " + sender);
		System.out.println("user " + user);
		userDAO.addFriend(user.getId(), senderId);

		ChatDAO chatDAO = (ChatDAO) ctx.getAttribute("chatDAO");
		Chat chat = chatDAO.createChat(user, sender);

		userDAO.addChatToUsers(user, sender, chat);

		FriendRequestDAO friendRequestDAO = (FriendRequestDAO) ctx.getAttribute("friendRequestDAO");
		friendRequestDAO.changeStatus(sender, user, FriendRequestState.ACCEPTED);
	}

	@POST
	@Path("/deny")
	@Consumes(MediaType.APPLICATION_JSON)
	public void declineRequest(@Context HttpServletRequest request, String senderId) {
		User user = (User) request.getSession().getAttribute("logged");
		UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
		User sender = userDAO.findById(senderId);
		FriendRequestDAO friendRequestDAO = (FriendRequestDAO) ctx.getAttribute("friendRequestDAO");
		friendRequestDAO.changeStatus(sender, user, FriendRequestState.DENIED);
	}
	
	@POST
	@Path("/unsendRequest")
	@Consumes(MediaType.APPLICATION_JSON)
	public void unsendRequest(@Context HttpServletRequest request, String senderId) {
		User user = (User) request.getSession().getAttribute("logged");
		UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
		FriendRequestDAO friendRequestDAO = (FriendRequestDAO) ctx.getAttribute("friendRequestDAO");
		String requestId = friendRequestDAO.getRequestId(senderId, user.getId());
		if(requestId == null)
			return;
		userDAO.deleteRequest(senderId, user.getId(), requestId);
		friendRequestDAO.deleteRequest(requestId);
	}
	
	@POST
	@Path("/blockUser")
	@Consumes(MediaType.APPLICATION_JSON)
	public void blockUser(@Context HttpServletRequest request, String userId) {
		User user = (User) request.getSession().getAttribute("logged");
		if(user.isAdmin()) {
			UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
			userDAO.blockUser(userId);
		}
	}
	
	@POST
	@Path("/unblockUser")
	@Consumes(MediaType.APPLICATION_JSON)
	public void unblockUser(@Context HttpServletRequest request, String userId) {
		User user = (User) request.getSession().getAttribute("logged");
		if(user.isAdmin()) {
			UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
			userDAO.unblockUser(userId);
		}
	}
}
