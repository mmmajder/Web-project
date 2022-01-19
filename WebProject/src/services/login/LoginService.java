package services.login;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
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
	// ctx polje je null u konstruktoru, mora se pozvati nakon konstruktora (@PostConstruct anotacija)
	public void init() {
		// Ovaj objekat se instancira više puta u toku rada aplikacije
		// Inicijalizacija treba da se obavi samo jednom
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
	public Person login(@Context HttpServletRequest request, LoginUser pom) {
		UserDAO dao = (UserDAO) ctx.getAttribute("userDAO");
		User retUser = dao.find(pom.getUsername(), pom.getPassword());
		if (retUser!=null) {
			request.getSession().setAttribute("logged", retUser);
			return retUser;
		}
		AdminDAO adminDAO = (AdminDAO) ctx.getAttribute("adminDAO");
		Admin retAdmin = adminDAO.find(pom.getUsername(), pom.getPassword());
		if (retAdmin!=null) {
			request.getSession().setAttribute("logged", retAdmin);
			return retAdmin;
		}
		return null;
	}
	/*@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_JSON)
	public User login(@Context HttpServletRequest request, @FormParam("username") String username, @FormParam("password") String password) {
		UserRepo.readFile();
		UserDaoImpl userDao = new UserDaoImpl();
		User retUser = userDao.read(username, password);
		if (retUser!=null) {
			request.getSession().setAttribute("user", retUser);
		}
		return retUser;
	}*/
}
