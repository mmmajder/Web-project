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

import beans.Post;
import beans.User;
import dao.person.UserDAO;
import dao.post.PostDAO;

@Path("/otherProfile")
public class OtherProfileService {
	
	@Context
	ServletContext ctx;

	@PostConstruct
	public void init() {
		if (ctx.getAttribute("userDAO") == null) {
			String contextPath = ctx.getRealPath("");
			ctx.setAttribute("userDAO", new UserDAO(contextPath));
		}
	}

	@GET
	@Path("/")
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
		return ((PostDAO) ctx.getAttribute("postDAO")).getUserPosts((User) request.getSession().getAttribute("otherProfile"));
	}
	
	@GET
	@Path("/friends")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<User> getFriends(@Context HttpServletRequest request) {
		return ((UserDAO) ctx.getAttribute("userDAO")).getMutualFriends((User) request.getSession().getAttribute("otherProfile"), (User) request.getSession().getAttribute("logged"));
	}
	
	

}
