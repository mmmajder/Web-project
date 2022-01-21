package services.friendRequest;

import java.time.LocalDateTime;
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

import beans.Comment;
import beans.FriendRequest;
import beans.User;
import dao.comment.CommentDAO;
import dao.friendRequest.FriendRequestDAO;
import dao.person.UserDAO;
import services.search.SearchData;
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
	}
	
	@GET
	@Path("/getFriendRequests")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ArrayList<UserSearchData> getFriendRequests(@Context HttpServletRequest request, SearchData data) {
		User user = (User) request.getSession().getAttribute("logged");
		FriendRequestDAO dao = (FriendRequestDAO) ctx.getAttribute("friendRequestDAO");
		ArrayList<FriendRequest> friendRequest = dao.getPending(user);
		return getPrintData(friendRequest, user);
	}
	
	private ArrayList<UserSearchData> getPrintData(ArrayList<FriendRequest> friendRequests, User user) {
		ArrayList<UserSearchData> data = new ArrayList<UserSearchData>();
		for (FriendRequest friendRequest : friendRequests) {
			UserDAO dao = (UserDAO) ctx.getAttribute("userDAO");
			User sender = dao.findById(friendRequest.getSender());
			data.add(new UserSearchData(sender.getId(), sender.getName(), sender.getSurname(), sender.getProfilePicture(), dao.getNumberOfMutualFriends(user, sender)));
		}
		return data;
	}
	
	/*@POST 
	@Path("/changeStatus") 
	@Produces(MediaType.APPLICATION_JSON)
	public Comment addComment(@Context HttpServletRequest request, FriendRequest friendRequest) {
		CommentDAO dao = (CommentDAO) ctx.getAttribute("commentDAO");
		System.out.println("broj kom " + dao.findAll().size());
		User user = (User) request.getSession().getAttribute("logged");
		Comment comment = new Comment(dao.generateId(), text, user.getId(), LocalDateTime.now(), LocalDateTime.now(), false);
		dao.addComment(comment);
		return comment;
	}*/
}
