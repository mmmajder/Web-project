package services.feed;

import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
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
import dao.post.PostDAO;

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
		if (ctx.getAttribute("postDAO") == null) {
			ctx.setAttribute("postDAO", new PostDAO(contextPath));
		}
	}
	
	@POST 
	@Path("/addComment") 
	@Produces(MediaType.APPLICATION_JSON)
	public Comment addComment(@Context HttpServletRequest request, String text) {
		CommentDAO dao = (CommentDAO) ctx.getAttribute("commentDAO");
		System.out.println("broj kom " + dao.findAll().size());
		User user = (User) request.getSession().getAttribute("logged");
		Comment comment = new Comment(dao.generateId(), text, user.getId(), LocalDateTime.now(), LocalDateTime.now(), false);
		dao.addComment(comment);
		return comment;
	}
	
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Post> getUserPosts(@Context HttpServletRequest request) {
		return ((PostDAO) ctx.getAttribute("postDAO")).getUserFeed((User) request.getSession().getAttribute("logged"));
	}

	
}
