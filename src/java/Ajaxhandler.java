import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.json.*;

public class Ajaxhandler extends HttpServlet {
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        try{
            Connection con;
            Statement st, st4;
            PreparedStatement ps, ps3;
            ResultSet rs,rs4;
            String SQL, SQL2, SQL3, SQL4;
            String columna = req.getParameter("COLUMNA");
            int columnaInt = Integer.parseInt(columna);
            //sacar el id de partida
            String numeroPartida = req.getParameter("PARTIDA");
            // se pasa el idPartida a int para usarlo en la SQL
            int idPartida = Integer.parseInt(numeroPartida);
            //sacar el userid de la sesion
            HttpSession sesion = req.getSession(false);
            int IdUsuario= (int) sesion.getAttribute("IdUsuario");
            //mirar si es el turno del user que ha enviado la peticion
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cuatroenraya?serverTimezone=UTC","root","1234");
            st = con.createStatement();
            SQL="SELECT Partidas.Turno, Partidas.JugadorUno, Partidas.JugadorDos, Partidas.EstadoPartida FROM Partidas WHERE Partidas.IdPartida =" + idPartida;
            rs = st.executeQuery(SQL);
            if(rs.next()){
                int turno = rs.getInt(1);
                int j1 = rs.getInt(2);
                int j2 = rs.getInt(3);
                int nextJ = j1;
                //si es su turno, introducir la ficha que ha colocado en la base de datos

                //variables para la puntuacion

                if(turno == IdUsuario){
                    //primero sacar el mapa y ver quien es quien y luego meter la ficha con el insert
                    String tablero = rs.getString(4);

                    //ahora ver si colocar un 1 o un 2
                    boolean colocar1 = false;
                    if (IdUsuario == j1){
                        colocar1 = true;
                        nextJ = j2;
                    }
                    //si colocar1 == true, pondremos un 1, si no, un 2
                    //tenemos la columna en la que hay que ponerla que es String columna
                    //String columnaClickada = tablero.substring(7*columna,7+7*columna); //columna clickada
                    //bucle descendente para encontrar la ultima ficha colocada
                    String tableroRes;
                    for (int j=6; j>=0; j--){
                        if (tablero.charAt(j+8*columnaInt) == '0'){ //cuando encuentre un 0, si colocar1 es true coloca un 1 si no, un 2
                            if (colocar1){
                                tableroRes = changeCharInPosition(j+8*columnaInt, '1',tablero);
                            } else{
                                tableroRes = changeCharInPosition(j+8*columnaInt, '2',tablero);
                            }

                            int puntuacion;
                            int cero =0;
                            // sacamos de la bbdd los datos de la partida : puntos j1 y j2 turnos ...
                            st4= con.createStatement();
                            SQL4 ="SELECT Partidastats.PuntosJugadorUno, Partidastats.PuntosJugadorDos Partidastats.TurnosJugados FROM Partidastats";
                            rs4 = st4.executeQuery(SQL4);
                            rs4.next();
                            int turnosJugados = rs.getInt(3); // sacamos los turnos jugados 
                            turnosJugados = turnosJugados +1; // sumamos un turno mas 

                            // dependiendo de quien este jugando, el valor de puntuacion sera del jugadorUno o jugadorDos
                            if (j1 ==turno){// Sacar de la BBDD, (tablaStats), dependiendo del jugador
                                puntuacion = rs4.getInt(1) ; 
                            }else{
                                puntuacion = rs4.getInt(2);
                            }

                            puntuacion = getPuntuacion(puntuacion, tableroRes, j, columnaInt, colocar1); // Actualizar la BBDD.
                            if(j1 ==turno){// se actualiza la bbdd dependiendo si es el turno del j1 O j2
                                int puntJ2 = rs4.getInt(2);
                                con.setAutoCommit(false);
                                SQL3= "INSERT Partidastats (TurnosJugados, Ganador, PuntosJugadorUno, PuntosJugadorDos) VALUES"+
                                "(" + turnosJugados + ", " + cero + ", " + puntuacion + ", " + puntJ2 +")";
                                ps3= con.prepareStatement(SQL3);
                                int result3 = ps3.executeUpdate();
                                con.setAutoCommit(true);
                            }else{
                                int puntJ1 = rs4.getInt(1);
                                con.setAutoCommit(false);
                                SQL3= "INSERT Partidastats (TurnosJugados, Ganador, PuntosJugadorUno, PuntosJugadorDos) VALUES"+
                                "(" + turnosJugados + ", " + cero + ", " + puntJ1 + ", " + puntuacion + ")";
                                ps3= con.prepareStatement(SQL3);
                                int result3 = ps3.executeUpdate();
                                con.setAutoCommit(true);
                                
                            }
                            
                            con.setAutoCommit(false);
                            SQL2="UPDATE Partidas SET EstadoPartida = '"+tableroRes+"', Turno = "+nextJ+" WHERE IdPartida ="+idPartida;
                            ps = con.prepareStatement(SQL2);
                            int result = ps.executeUpdate();
                            con.commit();
                            con.setAutoCommit(true);

                            ps.close();
                            break;
                        }
                    }
                }
            }
            rs.close();
            st.close();
            con.close();
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
        try{
            Connection con;
            String SQL;
            res.setContentType("application/json; charset=UTF-8"); //decimos que la respuesta es un json
            HttpSession sesion = req.getSession(false);
            int IdUsuario= (int) sesion.getAttribute("IdUsuario");
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cuatroenraya?serverTimezone=UTC","root","1234");
            //sacar el id de partida
            String numeroPartida = req.getParameter("PARTIDA");
            // se pasa el idPartida a int para usarlo en la SQL
            int idPartida = Integer.parseInt(numeroPartida);
            Statement st;
            ResultSet rs;
            SQL="SELECT Partidas.EstadoPartida, Partidas.Turno, Partidas.JugadorUno FROM Partidas WHERE Partidas.IdPartida =" + idPartida;
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
            rs.close();
            st.close();
            con.close();
        }catch(Exception e){
            System.out.println("Error en ajaxhandler: "+e);
        }
    }

    public String changeCharInPosition(int position, char ch, String str){
        char[] charArray = str.toCharArray();
        charArray[position] = ch;
        return new String(charArray);
    }

    public int getPuntuacion(int puntuacion, String estadoPartida, int fila, int columna, boolean colocar1) { 
        int jugador = 2;
        if (colocar1) {
            jugador = 1;
        }
        int posicion = fila + 8*columna;
        int puntuacionSumada=0;

        if (estadoPartida.charAt(posicion + 9) == jugador) {
            puntuacionSumada += sigueRastro(estadoPartida, posicion, jugador, "UP_LEFT");
        }
        if (estadoPartida.charAt(posicion + 1) == jugador) {
            puntuacionSumada += sigueRastro(estadoPartida, posicion, jugador, "LEFT");
        }
        if (estadoPartida.charAt(posicion - 9) == jugador) {
            puntuacionSumada += sigueRastro(estadoPartida, posicion, jugador, "DOWN_LEFT");
        }
        if (estadoPartida.charAt(posicion - 8) == jugador) {
            puntuacionSumada += sigueRastro(estadoPartida, posicion, jugador, "DOWN");
        }
        if (estadoPartida.charAt(posicion - 9) == jugador) {
            puntuacionSumada += sigueRastro(estadoPartida, posicion, jugador, "DOWN_RIGHT");
        }
        if (estadoPartida.charAt(posicion - 1) == jugador) {
            puntuacionSumada += sigueRastro(estadoPartida, posicion, jugador, "RIGHT");
        }
        if (estadoPartida.charAt(posicion + 9) == jugador) {
            puntuacionSumada += sigueRastro(estadoPartida, posicion, jugador, "UP_RIGHT");
        }
        return puntuacion + puntuacionSumada;
    }

    public int sigueRastro(String estadoPartida, int posicion, int jugador, String direccion) {
        int puntuacion = 0;
        int sumaPosicion = 0;
        switch(direccion) {
            case "UP_LEFT": sumaPosicion = 9; 
            break;
            case "LEFT": sumaPosicion = 1;
            break;
            case "DOWN_LEFT": sumaPosicion = -9;
            break;
            case "DOWN": sumaPosicion = -8;
            break;
            case "DOWN_RIGHT": sumaPosicion = -9;
            break;
            case "RIGHT": sumaPosicion = -1;
            break;
            case "UP_RIGHT": sumaPosicion = 9;
            break;
            default: sumaPosicion = sumaPosicion;
            break;
        }
        while(estadoPartida.charAt(posicion + sumaPosicion) == jugador) {
            puntuacion++;
            posicion += sumaPosicion;
        }
        return puntuacion;
    }
}