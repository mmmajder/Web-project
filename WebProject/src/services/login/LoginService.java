package services.login;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.User;
import dao.comment.CommentDAO;
import dao.person.UserDAO;

@Path("/login")
public class LoginService {
	
	@Context
	ServletContext ctx;
	
	@PostConstruct
	public void init() {
		if (ctx.getAttribute("userDAO") == null) {
	    	String contextPath = ctx.getRealPath("");
	    	System.out.println();
			ctx.setAttribute("userDAO", new UserDAO(contextPath));
		}
		if (ctx.getAttribute("commentDAO") == null) {
	    	String contextPath = ctx.getRealPath("");
	    	System.out.println();
			ctx.setAttribute("commentDAO", new CommentDAO(contextPath));
		}
	}
	
	@POST 
	@Path("/login") 
	@Produces(MediaType.APPLICATION_JSON)
	public User login(@Context HttpServletRequest request, LoginUser temp) {
		UserDAO dao = (UserDAO) ctx.getAttribute("userDAO");
		User retUser = dao.find(temp.getUsername(), temp.getPassword());
		if (retUser!=null) {
			request.getSession().setAttribute("logged", retUser);
			/*if (temp.getRemember()!=null) {
				
			}*/
		}
		return retUser;
	}
	
	@GET
	@Path("/testlogin")
	@Produces(MediaType.APPLICATION_JSON)
	public User testLogin(@Context HttpServletRequest request) {
		return (User) request.getSession().getAttribute("logged");
	}
}
