import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.json.*;

public class Game extends HttpServlet {
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		try{
			Statement st;
			ResultSet rs;
			String SQL, SQL2;
			String columna = req.getParameter("COLUMNA");
			//sacar el id de partida
			String idpartida = req.getParameter("PARTIDA");
			//sacar el userid de la sesion
			HttpSession sesion = req.getSession(false);
			int IdUsuario= (int) sesion.getAttribute("IdUsuario");
			//mirar si es el turno del user que ha enviado la peticion
			Class.forName("com.mysql.jdbc.Driver");
        	con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cuatroenraya?serverTimezone=UTC","root","1234");
        	st = con.createStatement();
			SQL="SELECT Partidas.Turno, Partidas.JugadorUno, Partidas.JugadorDos, Partidas.EstadoPartida FROM Partidas WHERE Partidas.IdPartida =" + IdPartida;
			rs = st.executeQuery(SQL);
			int turno = rs.getInt(1);
			int j1 = rs.getInt(2);
			int j2 = rs.getInt(3);
		//si es su turno, introducir la ficha que ha colocado en la base de datos
			if(turno == IdUsuario){
				//primero sacar el mapa y ver quien es quien y luego meter la ficha con el insert
				String tablero = rs.getString(4);
				//ahora ver si colocar un 1 o un 2
				boolean colocar1 = false;
				if (IdUsuario == j1){
					colocar1 = true;
				}
				//si colocar1 == true, pondremos un 1, si no, un 2
				//tenemos la columna en la que hay que ponerla que es String columna
				String columnaClickada = tablero.substring(7*columna,7+7*columna); //columna clickada
				//bucle descendente para encontrar la ultima ficha colocada
				for (int j=6; j>=0; j--){
					if (columnaClickada.charAt(j) == "0"){ //cuando encuentre un 0, si colocar1 es true coloca un 1 si no, un 2
						if (colocar1){
							String columnaNueva = changeCharInPosition(j, '1',columnaClickada);
						} else{
							String columnaNueva = changeCharInPosition(j, '2',columnaClickada);
						}
						//finalmente reintroducimos la columna en la matriz grande
						//matrizSt=matrizSt.replaceAt(NCol*7+i,"1"); //esto es en js
						break;
					}
				}
			}
		//cambiar el turno al otro jugador
	}catch(Exception e){
		System.out.println("Error en ajaxhandler: "+e);
	}
	}

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		//GET 
		/*
		Debe recibir el parametro PARTIDA que contiene el id de la partida.
		Busca en la base de datos el estado de la partida y lo envia de vuelta como json
		*/
		res.setContentType("application/json; charset=UTF-8"); //decimos que la respuesta es un json
		HttpSession sesion = req.getSession(false);
		int IdUsuario= (int) sesion.getAttribute("IdUsuario");
		Class.forName("com.mysql.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cuatroenraya?serverTimezone=UTC","root","1234");
        String idpartida = req.getParameter("PARTIDA");
        Statement st;
		ResultSet rs;
		SQL="SELECT Partidas.EstadoPartida, Partidas.Turno, Partidas.JugadorUno FROM Partidas WHERE Partidas.IdPartida =" + IdPartida;
		st = con.createStatement();
		rs = st.executeQuery(SQL);
		if (rs.next()){
			//sacamos el mapa del tablero
			String tablero = rs.getString(1);
			//sacamos de quien es el turno para enviarlo
			boolean turnoCliente = false; 
			int turno = rs.getInt(2);
			if (turno == IdUsuario){
				turnoCliente = true;
			}
			//indicamos quien es el j1, no hace falta saber quien es j2 porq es implicito que es el otro
			String jugador1 = "contrincante"; 
			int j1 = rs.getInt(3);
			if (j1 == IdUsuario){
				jugador1 = "clientelocal";
			}
			//ahora formamos la respuesta, que esta compuesta por el string de 012, por el turno y por quien es cada unno
			JSONObject json = new JSONObject();
			json.put("tablero",tablero);
			json.put("turno",turnoCliente);
			json.put("j1",jugador1);
			//enviamos el json
			res.getWriter().println(json);
		}
	}

	public String changeCharInPosition(int position, char ch, String str){
		char[] charArray = str.toCharArray();
		charArray[position] = ch;
		return new String(charArray);
	}
}