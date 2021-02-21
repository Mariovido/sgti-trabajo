import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Game extends HttpServlet {
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		String columna = req.getParameter("COLUMNA");
		//sacar el userid de la sesion
		//sacar el id de partida
		//mirar si es el turno del user que ha enviado la peticion
		//si es su turno, introducir la ficha que ha colocado en la base de datos
		//cambiar el turno al otro jugador
	}
}