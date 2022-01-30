package services.registration;

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

	@PostConstruct
	public void init() {
		if (ctx.getAttribute("userDAO") == null) {
			String contextPath = ctx.getRealPath("");
			ctx.setAttribute("userDAO", new UserDAO(contextPath));
		}
	}

	@POST
	@Path("/register")
	@Produces(MediaType.APPLICATION_JSON)
	public User register(@Context HttpServletRequest request, RegisterUser registerUser) {
		if (!registerUser.getPassword().equals(registerUser.getPassword2())) {
			return null;
		}
		UserDAO userDao = (UserDAO) ctx.getAttribute("userDAO");
		User existingUser = userDao.findByUsername(registerUser.getUsername());
		if (existingUser == null) {
			User retUser = new User(userDao.generateId(), registerUser.getUsername(), registerUser.getEmail(),
					registerUser.getName(), registerUser.getSurname(), userDao.getGender(registerUser.getGender()),
					registerUser.getPassword(), false, userDao.getDate(registerUser.getDateOfBirth()));
			request.getSession().setAttribute("logged", retUser);
			userDao.add(retUser);
			return retUser;
		}
		return null;
	}

}
