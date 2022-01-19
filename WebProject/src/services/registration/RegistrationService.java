package services.registration;

import java.util.Random;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.User;
import dao.person.UserDAO;

@Path("/register")
public class RegistrationService {
	@Context
	ServletContext ctx;
	
	/*@PostConstruct
	// ctx polje je null u konstruktoru, mora se pozvati nakon konstruktora (@PostConstruct anotacija)
	public void init() {
		// Ovaj objekat se instancira više puta u toku rada aplikacije
		// Inicijalizacija treba da se obavi samo jednom
		if (ctx.getAttribute("usersDAO") == null) {
	    //	String contextPath = ctx.getRealPath("");
	    //	System.out.println(contextPath);
			ctx.setAttribute("usersDAO", new UsersDAO());
	    //	ctx.setAttribute("userDao", new User());
		}
	}*/
	
	@POST
	@Path("/register")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public User register(@Context HttpServletRequest request, RegisterUser registerUser) {
		UserDAO dao = (UserDAO) ctx.getAttribute("usersDAO");
		User existingUser = dao.find(registerUser.getUsername(), registerUser.getPassword());
		if (existingUser==null) {
			return new User(generateId(), registerUser.getUsername(), registerUser.getEmail(), registerUser.getName(), registerUser.getSurname(), registerUser.getGender(), registerUser.getPassword());
		}
		return null;
	}
	
	// example of id = U00001
	private String generateId() {
		UserDAO  dao = (UserDAO) ctx.getAttribute("usersDAO");
		StringBuilder sb = new StringBuilder();
		sb.append("U");
		sb.append(new Integer(dao.findAll().size()+1));
		return sb.toString();
	}
	

}
