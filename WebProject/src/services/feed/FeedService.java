package services.feed;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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
import javax.xml.bind.DatatypeConverter;

import beans.Post;
import beans.User;
import dao.CommentDAO;
import dao.UserDAO;
import dao.PostDAO;
import dao.RepositoryDAO;

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
		if (ctx.getAttribute("userDAO") == null) {
			ctx.setAttribute("userDAO", new UserDAO(contextPath));
		}
	}

	@GET
	@Path("/getUserPosts")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Post> getUserPosts(@Context HttpServletRequest request) {
		return ((PostDAO) ctx.getAttribute("postDAO")).getUserFeed((User) request.getSession().getAttribute("logged"));
	}

	@GET
	@Path("/getPublicFeed")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Post> getPublicFeed(@Context HttpServletRequest request) {
		UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
		ArrayList<String> postsOfPublicUsers = userDAO.getPostsOfPublicUsers();
		return ((PostDAO) ctx.getAttribute("postDAO")).getPublicFeed(postsOfPublicUsers);
	}

	@POST
	@Path("/createNewPost")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Post createNewPost(PostData postData, @Context HttpServletRequest request) {
		PostDAO postDAO = (PostDAO) ctx.getAttribute("postDAO");
		UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
		User currentlyLogged = (User) request.getSession().getAttribute("logged");
		RepositoryDAO repositoryDAO = new RepositoryDAO();
		String photoName = "";
		System.out.println(postData.getPictureLocation());
		if (!postData.getPictureLocation().equals("")) {
			String base64String = postData.getPictureLocation();
			String[] strings = base64String.split(",");
			String extension;
			switch (strings[0]) { // check image's extension
			case "data:image/jpeg;base64":
				extension = "jpeg";
				break;
			case "data:image/png;base64":
				extension = "png";
				break;
			default:
				extension = "jpg";
				break;
			}
			// convert base64 string to binary data
			byte[] data = DatatypeConverter.parseBase64Binary(strings[1]);
			photoName = "photo" + currentlyLogged.getPosts().size() + "." + extension;
			
			String path1 = repositoryDAO.getPath() + "/images/userPictures/" + currentlyLogged.getId() + "/"
					+ photoName;
			File file1 = new File(path1);
			try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file1))) {
				outputStream.write(data);
				System.out.println("napravio sam u eclipsu");
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("nisam napravio u eclipsu");
			}

			String path2 = repositoryDAO.getTomcatPath() + "/images/userPictures/" + currentlyLogged.getId() + "/"
					+ photoName;
			File file2 = new File(path2);
			try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file2))) {
				outputStream.write(data);
				System.out.println("napravio sam u tomcatu");
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("nisam napravio u tomcatu");
			}
		}
		Post newPost = new Post(postDAO.generateId(), currentlyLogged.getId(), photoName, postData.getDescription(),
				LocalDateTime.now(), new ArrayList<String>(), false, postData.getPicture().equals("true"));
		postDAO.addNewPost(currentlyLogged, newPost);
		userDAO.addNewPost(currentlyLogged, newPost);
		return newPost;
	}
}
