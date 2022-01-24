package services.feed;

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
import beans.Post;
import beans.User;
import dao.comment.CommentDAO;
import dao.person.UserDAO;
import dao.post.PostDAO;
import services.registration.RegisterUser;

@Path("/feed")
public class FeedService {
	@Context
	ServletContext ctx;

	@PostConstruct
	public void init() {
		String contextPath = ctx.getRealPath("");
		if (ctx.getAttribute("commentDAO") == null) {
			ctx.setAttribute("commentDAO", new CommentDAO(contextPath));
		}
		System.out.println("commentDAO");
		if (ctx.getAttribute("postDAO") == null) {
			ctx.setAttribute("postDAO", new PostDAO(contextPath));
		}
		System.out.println("postDAO");
	}

	@POST
	@Path("/addComment")
	@Produces(MediaType.APPLICATION_JSON)
	public Comment addComment(@Context HttpServletRequest request, String text) {
		CommentDAO dao = (CommentDAO) ctx.getAttribute("commentDAO");
		System.out.println("broj kom " + dao.findAll().size());
		User user = (User) request.getSession().getAttribute("logged");
		Comment comment = new Comment(dao.generateId(), text, user.getId(), LocalDateTime.now(), LocalDateTime.now(),
				false);
		dao.addComment(comment);
		return comment;
	}

	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Post> getUserPosts(@Context HttpServletRequest request) {
		return ((PostDAO) ctx.getAttribute("postDAO")).getUserFeed((User) request.getSession().getAttribute("logged"));
	}

	@POST
	@Path("/createNewPost")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Post createNewPost(PostData postData, @Context HttpServletRequest request) {
		System.out.println(postData.getDescription());
		System.out.println(postData.getPictureLocation());
		System.out.println(postData.getPicture());
		PostDAO postDAO = (PostDAO) ctx.getAttribute("postDAO");
		User currentlyLogged = (User) request.getSession().getAttribute("logged");
		Post newPost = new Post(postDAO.generateId(), currentlyLogged.getId(), postData.getPictureLocation(),
				postData.getDescription(), LocalDateTime.now(), new ArrayList<String>(), false, postData.getPicture().equals("true"));
		postDAO.addNewPost(currentlyLogged, newPost);
		System.out.println("DODAO SAM NOVI POST: " + newPost);
		return newPost;
	}

}
