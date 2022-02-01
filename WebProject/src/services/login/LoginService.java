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
import dao.UserDAO;

@Path("/login")
public class LoginService {

	@Context
	ServletContext ctx;

	@PostConstruct
	public void init() {
		if (ctx.getAttribute("userDAO") == null) {
			String contextPath = ctx.getRealPath("");
			ctx.setAttribute("userDAO", new UserDAO(contextPath));
		}
		System.out.println("userDAO");
	}

	@POST
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	public User login(@Context HttpServletRequest request, LoginUser temp) {
		UserDAO dao = (UserDAO) ctx.getAttribute("userDAO");
		User retUser = dao.find(temp.getUsername(), temp.getPassword());
		System.out.println(retUser);
		if (retUser != null) {
			request.getSession().setAttribute("logged", retUser);
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
