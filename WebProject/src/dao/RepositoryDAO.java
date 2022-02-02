package dao;

import java.util.ArrayList;
import java.util.Arrays;

public class RepositoryDAO {
	// 0 - Andjela
	// 1 - Milan
	private static int programmer = 0;
	private static ArrayList<String> paths = new ArrayList<String>(
			Arrays.asList("C:/Users/Lenovo/Desktop/Web/Projekat/web-project/WebProject/WebContent",
					"C:/Users/Korisnik/Desktop/WEBProjekat/web-project/WebProject/WebContent"));

	private static ArrayList<String> tomcatPaths = new ArrayList<String>(Arrays.asList(
			"C:/Users/Lenovo/Documents/apache-tomcat-8.0.47/webapps/WebProject",
			"C:/Users/Korisnik/eclipse-workspace.metadata.plugins/org.eclipse.wst.server.core/tmp1/wtpwebapps/WebProject"));

	public String getPath() {
		return paths.get(programmer);
	}

	public String getTomcatPath() {
		return tomcatPaths.get(programmer);
	}
}
