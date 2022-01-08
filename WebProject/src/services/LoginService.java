package services;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.User;

@Path("/login")
public class LoginService {
	@POST 
	@Path("/login") 
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public User login(@Context HttpServletRequest request, User user) {
		User retVal = null;
		System.out.println("Stigao");
		retVal = (User) request.getSession().getAttribute("user");
		if (retVal == null) {
			request.getSession().setAttribute("user", user);
			retVal = user;
		}
		return retVal;
	}
}
