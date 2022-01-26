package services.messages;

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

import beans.User;
import dao.chat.ChatDAO;
import dao.dm.DmDAO;
import dao.person.UserDAO;

@Path("/messages")
public class MessageService {
	@Context
	ServletContext ctx;

	@PostConstruct
	public void init() {
		String contextPath = ctx.getRealPath("");
		if (ctx.getAttribute("chatDAO") == null) {
			ctx.setAttribute("chatDAO", new ChatDAO(contextPath));
		}
		if (ctx.getAttribute("userDAO") == null) {
			ctx.setAttribute("userDAO", new UserDAO(contextPath));
		}
		
		if (ctx.getAttribute("dmDAO") == null) {
			ctx.setAttribute("dmDAO", new DmDAO(contextPath));
		}
	}

	@GET
	@Path("/chats")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<ChatHeadData> getChats(@Context HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("logged");
		ChatDAO chatDao = (ChatDAO) ctx.getAttribute("chatDAO");
		UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
		return chatDao.getChatsForUser(user, userDAO);
	}
	
	@POST
	@Path("/chat")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public DmData getChatContent(@Context HttpServletRequest request, String id) {
		User user = (User) request.getSession().getAttribute("logged");
		ChatDAO chatDao = (ChatDAO) ctx.getAttribute("chatDAO");
		DmDAO dmDAO = (DmDAO) ctx.getAttribute("dmDAO");
		UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
		System.out.println("-------------------");
		System.out.println("chatId " + id);
		return chatDao.getDmData(id, dmDAO, userDAO, user);
	}

}
