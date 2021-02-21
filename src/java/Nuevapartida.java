import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Nuevapartida extends HttpServlet {
    public void doPost(HttpServletRequest req, HttpServletResponse res) {
        Connection con;
        PrintWriter out;
        String SQL, SQL2,SQL3,SQL4,SQL5,SQL6;
        Statement st,st3;
        PreparedStatement ps,ps4,ps5,ps6;
        ResultSet rs,rs3;

        try{
            HttpSession sesion = req.getSession(false); //devuelve la sesion si existe, si no, no crea una nueva
            if (sesion != null){   
                Class.forName("com.mysql.jdbc.Driver"); 
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cuatroenraya?serverTimezone=UTC","root","1234");

                int IdUsuario= (int) sesion.getAttribute("IdUsuario"); //id del j1 que quiere empezar la partida (en int)
                String nick = req.getParameter("NICK"); //nick del j2 con el que se quiere empezar la partida

                st = con.createStatement();
                SQL2 = "SELECT * FROM Usuarios Where Usuarios.Nick='"+nick+"'";
                rs =st.executeQuery(SQL2);

                RequestDispatcher rd = getServletContext().getRequestDispatcher("/principal");
                
                if (!rs.next()) {
                    rs.close();
                    st.close();
                    con.close();

                    //enviamos la peticion a Principal
                    rd.forward(req, res);

                } else {
                    String estadoPartida = "0000000;0000000;0000000;0000000;0000000;0000000;0000000";
                    int turno = IdUsuario;
                    byte finalizada = 0;
                    byte topeJugadores = 1;
                    int jugadorUno = IdUsuario;
                    int jugadorDos = rs.getInt(1);
                    
                    con.setAutoCommit(false);
                    SQL="INSERT INTO Partidas (EstadoPartida, Turno, Finalizada, TopeJugadores, JugadorUno, JugadorDos) VALUES ('" + estadoPartida + "', '" +
                    turno + "', " + finalizada + ", " + topeJugadores + ", " + jugadorUno + ", " + jugadorDos + ")";
                    ps = con.prepareStatement(SQL);
                    int result = ps.executeUpdate();
                    con.commit();
                    con.setAutoCommit(true);
                    
                    //recogemos el IDpartida creada anteriormente
                    st3=con.createStatement();
                    SQL3= "SELECT * FROM Partidas";
                    rs3=st3.executeQuery(SQL3);
                    int idPartida = rs.getInt(1);
                                        
                    //añadimos la relacion de jugador-partida de los dos jugadores
                    int cero=0;
                    con.setAutoCommit(false);
                    SQL4= "INSERT INTO Usuariospartidas(IdUsuario, IdPartida) VALUES (" + jugadorUno + ", " + idPartida + ")";
                    SQL5= "INSERT INTO Usuariospartidas(IdUsuario, IdPartida) VALUES (" + jugadorDos + ", " + idPartida + ")";
                    // SQL6= "INSERTO INTO Partidasstats (IdPartida, TurnosJugados, Ganador, PuntosJugadorUno, PuntosJugadorDos) VALUES (" + idPartida + ", "+ cero +", "+ jugadorUno +", "+ cero +", "+ cero +")"; 
                    ps4= con.prepareStatement(SQL4);
                    ps5 = con.prepareStatement(SQL5);
                    //ps6 = con.prepareStatement(SQL6);
                    int result4 = ps4.executeUpdate();
                    int result5 = ps5.executeUpdate();
                    //int result6 = ps.executeUpdate();
                    con.commit();
                    con.setAutoCommit(true);
                    
                    rs.close();
                    rs3.close();
                    st.close();
                    st3.close();
                    ps.close();
                    ps4.close();
                    ps5.close();
                    //ps6.close();
                    con.close();

                    //enviamos la peticion a Principal
                    rd.forward(req, res); 
                }
            } else {   
                res.sendRedirect("http://juegocraya.duckdns.org:8080/sgti-trabajo/inicio");
            }
        } catch(Exception e){
            System.out.println("<div> Error de NUEVAPARTIDA" + e + "</div>");
        }
    }
}