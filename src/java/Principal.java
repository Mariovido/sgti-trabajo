import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Principal extends HttpServlet {
    public void doPost(HttpServletRequest req, HttpServletResponse res) {
        Connection con;
        Statement st;
        ResultSet rs;
        String SQL, IdUsuario;
        PrintWriter out;
        
        try {
            HttpSession sesion = req.getSession(false);     

            if(sesion!=null) {

                
                System.out.println("Estoy aquí 2");
                out = res.getWriter();
                //IdUsuario = (String)sesion.getAttribute("IdUsuario");
                IdUsuario = "13";
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cuatroenraya?serverTimezone=UTC","root","1234");
                
                st = con.createStatement();
                SQL = "SELECT Partidas.IdPartida, Partidas.Turno, Partidas.Finalizada FROM Partidas, UsuarioPartidas WHERE Partidas.IdPartida = UsuarioPartidas.IdPartida AND UsuarioPartidas.IdUsuario = " + IdUsuario;
                rs = st.executeQuery(SQL);

                //HTML
                res.setContentType("texxt/html");
                out.println("<!DOCTYPE html>");
                out.println("<html lang='en'>");
                out.println("<head>");
                out.println("    <meta charset='UTF-8'>");
                out.println("    <meta name='viewport' content='width=device-width, initial-scale=1.0'>");
                out.println("    <title>Cuatro En Raya</title>");
                out.println("    <link rel='stylesheet' href='web/resources/styles/main.css'>");
                out.println("    <link rel='stylesheet' href='web/resources/styles/partidas.css'>");
                out.println("</head>");
                out.println("<body>");
                out.println("    <header class='main-header'>");
                out.println("        <nav class='main-header__nav'>");
                out.println("            <ul class='main-header__item-list'>");
                out.println("                <li class='main-header__item'><a class='active' href=''>Mis partidas</a></li>");
                out.println("            </ul>");
                out.println("        </nav>");
                out.println("    </header>");
                out.println("    <main>");
                out.println("        <h1>Bienvenido</h1>");
                out.println("        <p>Aquí podrá ver todas sus partidas iniciadas</p>");
                out.println("        <div class='container'>");
                while (!rs.next()) {
                    // Aquí podemos no se que poner en el href xD.
                    out.println("            <div class='bloque' onclick='location.href=''>Partida 1</div>");
                }
                out.println("        </div>");
                out.println("    </main>");
                out.println("</body>");
                out.println("</html>");
                
                rs.close();
                st.close();
                con.close();
                out.close();
                
            } else {
                
                res.sendRedirect("http://localhost:8080/sgti-trabajo/inicio");
                
            }
        } catch (Exception e){
            System.out.println("<div> Error " + e + "</div>");
        }
    }
}