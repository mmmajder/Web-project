package services.registration;

import java.io.File;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.Chat;
import beans.User;
import dao.ChatDAO;
import dao.RepositoryDAO;
import dao.UserDAO;

@Path("/register")
public class RegistrationService {
	@Context
	ServletContext ctx;

	@PostConstruct
	public void init() {
		String contextPath = ctx.getRealPath("");
		if (ctx.getAttribute("userDAO") == null) {
			ctx.setAttribute("userDAO", new UserDAO(contextPath));
		}
		if (ctx.getAttribute("chatDAO") == null) {
			ctx.setAttribute("chatDAO", new ChatDAO(contextPath));
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
		if (!RegistrationData.isValid(registerUser)) {
			return null;
		}
		User existingUser = userDao.findByUsername(registerUser.getUsername());
		if (existingUser == null) {
			User retUser = new User(userDao.generateId(), registerUser.getUsername(), registerUser.getEmail(),
					registerUser.getName(), registerUser.getSurname(), userDao.getGender(registerUser.getGender()),
					registerUser.getPassword(), false, userDao.getDate(registerUser.getDateOfBirth()));
			request.getSession().setAttribute("logged", retUser);
			userDao.add(retUser);
			RepositoryDAO repositoryDAO = new RepositoryDAO();
			String path = repositoryDAO.getPath() + "/images/userPictures/" + retUser.getId();
			new File(path).mkdirs();

			ChatDAO chatDao = (ChatDAO) ctx.getAttribute("chatDAO");
			for (User admin : userDao.getAdmins()) {
				Chat chat = chatDao.createChat(retUser, admin);
				userDao.addChatToUsers(retUser, admin, chat);
			}
			return retUser;
		}
		return null;
	}

}
