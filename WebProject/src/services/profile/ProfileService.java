package services.profile;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.sound.midi.Soundbank;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.Post;
import beans.User;
import dao.person.UserDAO;
import dao.post.PostDAO;

@Path("/profile")
public class ProfileService {
	@Context
	ServletContext ctx;

	@PostConstruct
	public void init() {
		String contextPath = ctx.getRealPath("");
		if (ctx.getAttribute("userDAO") == null) {
			ctx.setAttribute("userDAO", new UserDAO(contextPath));
		}
		if (ctx.getAttribute("postDAO") == null) {
			ctx.setAttribute("postDAO", new PostDAO(contextPath));
		}
	}

	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public User getUser(@Context HttpServletRequest request) {
		return (User) request.getSession().getAttribute("logged");
	}
	
	@GET
	@Path("/photos")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Post> getUserPhotos(@Context HttpServletRequest request) {
		return ((PostDAO) ctx.getAttribute("postDAO")).getUserPhotos((User) request.getSession().getAttribute("logged"));
	}
	
	@GET
	@Path("/posts")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Post> getUserPosts(@Context HttpServletRequest request) {
		return ((PostDAO) ctx.getAttribute("postDAO")).getUserPosts((User) request.getSession().getAttribute("logged"));
	}
	
	@POST
	@Path("/editProfile")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public User editUser(@Context HttpServletRequest request, EditProfileData data) {
		UserDAO dao = (UserDAO) ctx.getAttribute("userDAO");
		User user = (User) request.getSession().getAttribute("logged");
		return dao.editUser(user.getId(), data);
	}
	
	@POST
	@Path("/photoDetails")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public PhotoDetailsData editUser(@Context HttpServletRequest request, String photoID) {
		PostDAO postDAO = (PostDAO) ctx.getAttribute("postDAO");
		// {"id":"PO00006"} je photoID
		Post post = postDAO.findById(photoID.split(":")[1].replace("\"", "").replace("}", ""));
		return new PhotoDetailsData(post.getId(), post.getAuthor(), post.getPictureLocation(), post.getDescription(), post.getPosted());
	}
	
	@GET
	@Path("/friends")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<User> getFriends(@Context HttpServletRequest request) {
		return ((UserDAO) ctx.getAttribute("userDAO")).getFriends((User) request.getSession().getAttribute("logged"));
	}
	
	@GET
	@Path("/viewOtherProfile")
	public String getBooks(@QueryParam("num") int num) {
		return "/rest/demo/books received QueryParam 'num': " + num;
	}
	
	@DELETE
	@Path("/delete")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ArrayList<Post> deletePost(@Context HttpServletRequest request, String postID) {
		PostDAO postDAO = (PostDAO) ctx.getAttribute("postDAO");
		String post = postID.split(":")[1].replace("\"", "").replace("}", "");
		Post p = postDAO.findById(post);
		UserDAO dao = (UserDAO) ctx.getAttribute("userDAO");
		User user = dao.findById(p.getAuthor());
		dao.deletePost(postDAO.delete(post), post);
		return postDAO.getUserPhotos(user);
	}
	
	@POST
	@Path("/setProfilePhoto")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String setProfilePhoto(@Context HttpServletRequest request, String photoID) {
		PostDAO postDAO = (PostDAO) ctx.getAttribute("postDAO");
		// {"id":"PO00006"} je photoID
		Post post = postDAO.findById(photoID.split(":")[1].replace("\"", "").replace("}", ""));
		UserDAO dao = (UserDAO) ctx.getAttribute("userDAO");
		return dao.changeProfilePicture(post);
	}
	
}
