package services.search;

import java.util.ArrayList;

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
import dao.UserDAO;

@Path("/search")
public class SearchService {

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
	@Path("/search")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ArrayList<UserSearchData> searchUsers(@Context HttpServletRequest request, SearchData data) {
		UserDAO dao = (UserDAO) ctx.getAttribute("userDAO");
		return dao.searchUsers(data, (User) request.getSession().getAttribute("logged"));
	}

	@POST
	@Path("/userById")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public User getUserById(@Context HttpServletRequest request, String id) {
		UserDAO dao = (UserDAO) ctx.getAttribute("userDAO");
		return dao.findById(id.split("=")[1]);
	}
}
