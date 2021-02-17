import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Nuevapartida extends HttpServlet {
    public void doPost(HttpServletRequest req, HttpServletResponse res) {
        Connection con;
        PrintWriter out;
        String SQL, SQL2;
        Statement st;
        PreparedStatement ps;
        ResultSet rs;

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

                RequestDispatcher rd = getServletContext.getRequestDispatcher("/pricipal");
                
                if (!rs.next()) {
                    rs.close();
                    st.close();
                    con.close();

                    rd.forward(req, res);
                   // res.sendRedirect("http://juegocraya.duckdns.org:8080/sgti-trabajo/principal");

                } else {
                    String estadoPartida = "";
                    int turno = IdUsuario;
                    byte finalizada = 0;
                    byte topeJugadores = 1;
                    int jugadorUno = IdUsuario;
                    int jugadorDos = rs.getInt(1);

                    SQL="INSERT INTO Partidas (EstadoPartida, Turno, Finalizada, TopeJugadores, JugadorUno, JugadorDos) VALUES ('" + estadoPartida + "', '" +
                    turno + "', '" + finalizada + "', '" + topeJugadores + "', '" + jugadorUno + "', " + jugadorDos + ")";
                    ps = con.prepareStatement(SQL);
                    int result = ps.executeUpdate();
                    con.commit();
                    con.setAutoCommit(true);
                                        
                    rs.close();
                    st.close();
                    ps.close();
                    con.close();

                    rd.forward(req, res); 
                    //res.sendRedirect("http://juegocraya.duckdns.org:8080/sgti-trabajo/principal"); 
                }
            } else {   
                res.sendRedirect("http://juegocraya.duckdns.org:8080/sgti-trabajo/inicio");
            }
        } catch(Exception e){
            System.out.println("<div> Error " + e + "</div>");
        }
    }
}