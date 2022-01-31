package services.profile;

import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.Comment;
import beans.Post;
import beans.User;
import dao.chat.ChatDAO;
import dao.comment.CommentDAO;
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
		if (ctx.getAttribute("commentDAO") == null) {
			ctx.setAttribute("commentDAO", new CommentDAO(contextPath));
		}
		if (ctx.getAttribute("chatDAO") == null) {
			ctx.setAttribute("chatDAO", new ChatDAO(contextPath));
		}
	}

	@GET
	@Path("/getUser")
	@Produces(MediaType.APPLICATION_JSON)
	public User getUser(@Context HttpServletRequest request) {
		return (User) request.getSession().getAttribute("logged");
	}

	@GET
	@Path("/photos")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Post> getUserPhotos(@Context HttpServletRequest request) {
		return ((PostDAO) ctx.getAttribute("postDAO"))
				.getUserPhotos((User) request.getSession().getAttribute("logged"));
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
		return new PhotoDetailsData(post.getId(), post.getAuthor(), post.getPictureLocation(), post.getDescription(),
				post.getPosted());
	}

	@GET
	@Path("/friends")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<User> getFriends(@Context HttpServletRequest request) {
		return ((UserDAO) ctx.getAttribute("userDAO")).getFriends((User) request.getSession().getAttribute("logged"));
	}

	@POST
	@Path("/viewOtherProfile")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public User viewOtherProfile(@Context HttpServletRequest request, String id) {
		System.out.println(id);
		User user = ((UserDAO) ctx.getAttribute("userDAO")).findById(id);
		System.out.println("prvo " + user);
		request.getSession().setAttribute("otherProfile", user);
		return user;
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

	@POST
	@Path("/loadComments")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ArrayList<CommentReturnData> loadComments(@Context HttpServletRequest request, String photoID) {
		PostDAO postDAO = (PostDAO) ctx.getAttribute("postDAO");
		CommentDAO commentDAO = (CommentDAO) ctx.getAttribute("commentDAO");
		Post post;
		if(photoID.contains("{")) {
			post = postDAO.findById(photoID.split(":")[1].replace("\"", "").replace("}", ""));			
		} else {
			post = postDAO.findById(photoID);
		}
		UserDAO dao = (UserDAO) ctx.getAttribute("userDAO");
		return commentDAO.getCommentsOnPost(post, dao);
	}

	@POST
	@Path("/addComment")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public CommentReturnData addComment(@Context HttpServletRequest request, CommentData commentData) {
		PostDAO postDAO = (PostDAO) ctx.getAttribute("postDAO");
		Post post = postDAO.findById(commentData.getPostID());
		User user = (User) request.getSession().getAttribute("logged");
		CommentDAO commentDAO = (CommentDAO) ctx.getAttribute("commentDAO");
		Comment comment = new Comment(commentDAO.generateId(), commentData.getText(), user.getId(), LocalDateTime.now(),
				LocalDateTime.now(), false);
		commentDAO.addComment(comment);
		postDAO.addComment(post, comment);
		System.out.println(post);
		System.out.println(comment);
		return new CommentReturnData(comment.getId(), comment.getText(), user.getId(), user.getName(),
				user.getSurname(), comment.getCreated(), comment.getLastEdited(), user.getProfilePicture());
	}
	
	@POST
	@Path("/removeFriend")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public void removeFriend(@Context HttpServletRequest request, String otherUserId) {
		User loggedUser = (User) request.getSession().getAttribute("logged");
		UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
		userDAO.removeFriend(loggedUser.getId(), otherUserId);
		
	//	ChatDAO chatDAO = (ChatDAO) ctx.getAttribute("chatDAO");
	//	chatDAO.removeChat(loggedUser, userDAO.findById(otherUserId));
		
	}
	
	@POST
	@Path("/editComment")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Comment editComment(@Context HttpServletRequest request, CommentEditData commentData) {
		CommentDAO commentDAO = (CommentDAO) ctx.getAttribute("commentDAO");
		commentDAO.editComment(commentData.getCommentID(), commentData.getText());
		return commentDAO.findById(commentData.getCommentID());
	}
	
	@POST
	@Path("/deleteComment")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Comment deleteComment(@Context HttpServletRequest request, CommentEditData commentData) {
		CommentDAO commentDAO = (CommentDAO) ctx.getAttribute("commentDAO");
		commentDAO.deleteComment(commentData.getCommentID());
		PostDAO postDAO = (PostDAO) ctx.getAttribute("postDAO");
		postDAO.deleteComment(commentData.getPostID(), commentData.getCommentID());
		return commentDAO.findById(commentData.getCommentID());
	}

}
