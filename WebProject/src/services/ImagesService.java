package services;

import java.io.File;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.websocket.server.PathParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import dao.chat.ChatDAO;
import dao.dm.DmDAO;
import dao.person.UserDAO;

@Path("/images")
public class ImagesService {
	@Context
	ServletContext ctx;

	@PostConstruct
	public void init() {
		String contextPath = ctx.getRealPath("");
		
	}
	
	@GET
	@Path("/{fileName}")
	public Response getFile(@PathParam("fileName") String fileName) {
		File f = new File("C:/Users/Lenovo/Documents/apache-tomcat-8.0.47/data/resources/images/" + fileName);
		Response response = Response.ok(f).build();
		return response;
	}
	
}
