import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Game extends HttpServlet {
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        Connection con;
        Statement st;
        ResultSet rs;
        String SQL, IdUsuario, IdPartida;
        PrintWriter out;
        
        IdPartida=req.getParameter("IDPARTIDA"); //le llega de principal al clickar una partida

        out = res.getWriter();
        try {
            HttpSession sesion = req.getSession(false);

            if(sesion==null) {
                out.close();
                res.sendRedirect("http://localhost:8080/sgti-trabajo/inicio");
            } else {
                IdUsuario = (String)sesion.getAttribute("IdUsuario");

                Class.forName("com.mysql.jdbc.Driver");
                 con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cuatroenraya?serverTimezone=UTC","root","1234");
                 if (con==null){
                     out.println("<div>no hay conexion</div>");
                 }
                st = con.createStatement();
                SQL = "SELECT Partidas.EstadoPartida, Partidas.Turno, Partidas.JugadorUno, Partidas.JugadorDos, PartidaStats.TurnosJugados FROM Partidas, PartidaStats WHERE Partidas.IdPartida = PartidaStats.IdPartida AND IdPartida.IdPartida = " + IdPartida;
                rs=st.executeQuery(SQL);

                //HTML
                out.println("<!DOCTYPE html>");
                out.println("<html lang='en'>");
                out.println("<head>");
                out.println("    <meta charset='UTF-8'>");
                out.println("    <meta name='viewport' content='width=device-width, initial-scale=1.0'>");
                out.println("    <title>Cuatro En Raya</title>");
                out.println("    <link rel='stylesheet' href='web/resources/styles/main.css'>");
                out.println("    <link rel='stylesheet' href='web/resources/styles/tabla.css'>");
                out.println("    <script src='web/resources/js/game.js'></script>");
                out.println("</head>");
                out.println("<body>");
                out.println("    <header class='main-header'>");
                out.println("        <nav class='main-header__nav'>");
                out.println("            <ul class='main-header__item-list'>");
                out.println("                <li class='main-header__item'><a href='/stgi-trabajo/principal'>Mis partidas</a></li>");
                out.println("            </ul>");
                out.println("        </nav>");
                out.println("    </header>");
                out.println("    <main>");
                out.println("        <div id='jugadores'>");
                out.println("            <span id='local'>Jugador1</span> vs.");
                out.println("            <span id='remoto'>Jugador2</span>");
                out.println("        </div>");
                out.println("    </br>");
                //Tablero
                out.println(" <table id='tablero'>");
                out.println("   <tbody>");
                for (int i=0; i<7; i++){
                    out.println("<tr>");
                    for (int j=0; j<7; j++){
                        out.println("<td id='"+String.valueOf(i)+String.valueOf(j)+"' onclick='paint()'></td>");
                    }
                    out.println("</tr>");
                }
                out.println("   </tbody>");
                out.println(" </table>");

                out.println("    </main>");
                out.println("</body>");
                out.println("</html>");
                
                rs.close();
                st.close();
                con.close();
            }
        } catch (Exception e){
            out.println("<div> Error " + e + "</div>");
        }
        out.close();
    }
}
