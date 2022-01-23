package services.friendRequest;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.User;
import dao.friendRequest.FriendRequestDAO;
import dao.person.UserDAO;
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
		System.out.println("friendDAO");
		if (ctx.getAttribute("userDAO") == null) {
			ctx.setAttribute("userDAO", new UserDAO(contextPath));
		}
	}
	
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<UserSearchData> getFriendRequests(@Context HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("logged");
		FriendRequestDAO dao = (FriendRequestDAO) ctx.getAttribute("friendRequestDAO");
		UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
		return dao.getPrintData(user, userDAO);
	}
	
}
