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

import beans.Admin;
import beans.Person;
import beans.User;
import dao.person.AdminDAO;
import dao.person.UserDAO;

@Path("/demo")
public class LoginService {
	
	@Context
	ServletContext ctx;
	
	@PostConstruct
	public void init() {
		if (ctx.getAttribute("userDAO") == null) {
	    	String contextPath = ctx.getRealPath("");
			ctx.setAttribute("userDAO", new UserDAO(contextPath));
		}
		if (ctx.getAttribute("adminDAO") == null) {
	    	String contextPath = ctx.getRealPath("");
			ctx.setAttribute("adminDAO", new AdminDAO(contextPath));
		} 
	}
	
	@POST 
	@Path("/login") 
	@Produces(MediaType.APPLICATION_JSON)
	public Person login(@Context HttpServletRequest request, LoginUser temp) {
		UserDAO dao = (UserDAO) ctx.getAttribute("userDAO");
		User retUser = dao.find(temp.getUsername(), temp.getPassword());
		if (retUser!=null) {
			if (temp.getRemember()!=null) {
				request.getSession().setAttribute("logged", retUser);
			}
			return retUser;
		}
		AdminDAO adminDAO = (AdminDAO) ctx.getAttribute("adminDAO");
		Admin retAdmin = adminDAO.find(temp.getUsername(), temp.getPassword());
		if (retAdmin!=null) {
			if (temp.getRemember()!=null) {
				request.getSession().setAttribute("logged", retAdmin);
			}
			return retAdmin;
		}
		return null;
	}
	
	@GET
	@Path("/testlogin")
	@Produces(MediaType.APPLICATION_JSON)
	public User testLogin(@Context HttpServletRequest request) {
		return (User) request.getSession().getAttribute("logged");
		}
}
