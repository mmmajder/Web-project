package services.otherProfile;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

import beans.Post;
import beans.User;
import dao.UserDAO;
import dao.FriendRequestDAO;
import dao.PostDAO;

@Path("/otherProfile")
public class OtherProfileService {

	@Context
	ServletContext ctx;

	@PostConstruct
	public void init() {
		String contextPath = ctx.getRealPath("");
		if (ctx.getAttribute("userDAO") == null) {
			ctx.setAttribute("userDAO", new UserDAO(contextPath));
		}
		if (ctx.getAttribute("friendRequestDAO") == null) {
			ctx.setAttribute("friendRequestDAO", new FriendRequestDAO(contextPath));
		}
	}

	@GET
	@Path("/otherUser")
	@Produces(MediaType.APPLICATION_JSON)
	public User getOpenedProfile(@Context HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("otherProfile");
		return user;
	}

	@GET
	@Path("/photos")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Post> getUserPhotos(@Context HttpServletRequest request) {
		return ((PostDAO) ctx.getAttribute("postDAO"))
				.getUserPhotos((User) request.getSession().getAttribute("otherProfile"));
	}

	@GET
	@Path("/posts")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Post> getUserPosts(@Context HttpServletRequest request) {
		return ((PostDAO) ctx.getAttribute("postDAO"))
				.getUserPosts((User) request.getSession().getAttribute("otherProfile"));
	}

	@GET
	@Path("/friends")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<User> getFriends(@Context HttpServletRequest request) {
		return ((UserDAO) ctx.getAttribute("userDAO")).getMutualFriends(
				(User) request.getSession().getAttribute("otherProfile"),
				(User) request.getSession().getAttribute("logged"));
	}

	@GET
	@Path("/arePostsPrivate")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean arePostsPrivate(@Context HttpServletRequest request) {
		User otherUser = (User) request.getSession().getAttribute("otherProfile");
		User logged = (User) request.getSession().getAttribute("logged");
		if (logged.isAdmin())
			return false;
		if (logged.getFriends().contains(otherUser.getId()))
			return false;
		return otherUser.isPrivate();
	}
	
	@GET
	@Path("/getRelationStatus")
	@Produces(MediaType.APPLICATION_JSON)
	public Relation getRelationStatus(@Context HttpServletRequest request) {
		User otherUser = (User) request.getSession().getAttribute("otherProfile");
		User logged = (User) request.getSession().getAttribute("logged");
		FriendRequestDAO dao = (FriendRequestDAO) ctx.getAttribute("friendRequestDAO");
		
		Relation relation = new Relation();
		if (logged == null) {
			relation.setFriendStatus("");
			relation.setSendMessage("");
		} else if (logged.isAdmin()) {
			if(otherUser.isAdmin()) {
				relation.setFriendStatus("");
				relation.setSendMessage("Send a message");
			} else if(otherUser.isBlocked()) {
				relation.setFriendStatus("Unblock user");
				relation.setSendMessage("Send a message");
			} else {
				relation.setFriendStatus("Block user");
				relation.setSendMessage("Send a message");
			}
		} else if(logged.getFriends().contains(otherUser.getId())) {
			relation.setFriendStatus("Remove friend");
			relation.setSendMessage("Send a message");
		} else if(dao.isPending(logged, otherUser)) {
			relation.setFriendStatus("Unsend request");
			relation.setSendMessage("");
		} else if(dao.isPending(otherUser, logged)) {
			relation.setFriendStatus("Accept");
			relation.setSendMessage("Decline");
		} else if(otherUser.isAdmin()) {
			relation.setFriendStatus("");
			relation.setSendMessage("");
		} else {
			relation.setFriendStatus("Add friend");
			relation.setSendMessage("");
		}
		return relation;
	}

}
