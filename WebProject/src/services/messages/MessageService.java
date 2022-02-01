package services.messages;

import java.time.LocalDateTime;
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

import beans.Chat;
import beans.DM;
import beans.User;
import dao.ChatDAO;
import dao.DmDAO;
import dao.UserDAO;

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
		DmDAO dmDAO = (DmDAO) ctx.getAttribute("dmDAO");
		return chatDao.getChatsForUser(user, userDAO, dmDAO);
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

	@POST
	@Path("/saveMessage")
	@Consumes(MediaType.APPLICATION_JSON)
	public boolean addDm(@Context HttpServletRequest request, DmAddition data) {
		try {
			DmDAO dmDAO = (DmDAO) ctx.getAttribute("dmDAO");
			DM dm = new DM(dmDAO.generateId(), data.getContent(), LocalDateTime.now(), data.getSender().getId(),
					data.getReciever().getId());
			System.out.println(dm);
			dmDAO.addDM(dm);
			ChatDAO chatDao = (ChatDAO) ctx.getAttribute("chatDAO");
			chatDao.addDM(dm, data.getChat());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@POST
	@Path("/seen")
	@Consumes(MediaType.APPLICATION_JSON)
	public void getChat(@Context HttpServletRequest request, DmData chat) {
		ChatDAO chatDao = (ChatDAO) ctx.getAttribute("chatDAO");
		User user = (User) request.getSession().getAttribute("logged");
		chatDao.seenMessage(chat, user);
	}

	@GET
	@Path("/notSeenMessages")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean notSeenMessages(@Context HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("logged");
		UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
		DmDAO dmDAO = (DmDAO) ctx.getAttribute("dmDAO");
		ChatDAO chatDao = (ChatDAO) ctx.getAttribute("chatDAO");
		return userDAO.hasNotSeenMessages(user, dmDAO, chatDao);
	}

	@POST
	@Path("/openedChat")
	@Consumes(MediaType.APPLICATION_JSON)
	public void getChat(@Context HttpServletRequest request, String id) {
		User user = (User) request.getSession().getAttribute("logged");
		UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
		User otherUser = userDAO.findById(id);
		ChatDAO chatDao = (ChatDAO) ctx.getAttribute("chatDAO");
		Chat chat = chatDao.getChatForUsers(user, otherUser);
		request.getSession().setAttribute("openedChat", chat);
	}

	@GET
	@Path("/openChatIfNeeded")
	@Produces(MediaType.APPLICATION_JSON)
	public DmData openedChat(@Context HttpServletRequest request) {
		Chat chat = (Chat) request.getSession().getAttribute("openedChat");
		if (chat == null) {
			return null;
		} else {
			return getChatContent(request, chat.getId());
		}
	}
}
