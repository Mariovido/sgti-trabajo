import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Cuenta extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        Connection con;
        Statement st;
        ResultSet rs;
        String SQL, IdUsuario;
        PrintWriter out;

        out = res.getWriter();
        try {
            HttpSession sesion = req.getSession(false);

            if(sesion!=null) {
                IdUsuario = (String)sesion.getAttribute("IdUsuario");
                Class.forName("com.mysql.jdbc.Driver");

                con = DriverManager.getConnection("jdbc:mysql://");
                st = con.createStatement();
                SQL = "SELECT * FROM Usuarios WHERE Usuarios.IdUsuario = " + IdUsuario;
                rs=st.executeQuery(SQL);

                // Hacemos el HTML
                out.println("<!DOCTYPE html>");
                out.println("<html lang='en'>");
                out.println("<head>");
                out.println("    <meta charset='UTF-8'>");
                out.println("    <meta name='viewport' content='width=device-width, initial-scale=1.0'>");
                out.println("    <title>Cuatro En Raya</title>");
                out.println("    <link rel='stylesheet' href='resources/styles/main.css'>");
                out.println("    <link rel='stylesheet' href='resources/styles/registro.css'>");
                out.println("</head>");
                out.println("<body>");
                out.println("    <header class='main-header'>");
                out.println("        <nav class='main-header__nav'>");
                out.println("            <ul class='main-header__item-list'>");
                out.println("                <li class='main-header__item'><a href=''>Mis partidas</a></li>");
                out.println("                <li class='main-header__item'><a class='active' href=''>Mi cuenta</a></li>");
                out.println("            </ul>");
                out.println("        </nav>");
                out.println("    </header>");
                out.println("    <main>");
                out.println("        <div class='user'>");
                out.println("            <header class='user__header'>");
                out.println("                <h1 class='user__title'>Datos de la Cuenta</h1>");
                out.println("            </header>");
                out.println("            <div name='formulario-login' class='form'>");
                out.println("                <div class='form__group'>");
                out.println("                    <span class='form__input'>Nombre: " + rs.getString(6) + "</span>");
                out.println("                </div>");
                out.println("                <div class='form__group'>");
                out.println("                    <span class='form__input'>Correo: " + rs.getString(3) + "</span>");
                out.println("                </div>");
                out.println("            </div>");
                out.println("            </div>");
                out.println("    </main>");
                out.println("</body>");
                out.println("</html>");
                
                rs.close();
                st.close();
                con.close();
            } else {
                out.close();
                res.sendRedirect("http://localhost:8080/sgti-trabajo/inicio");
            }
        } catch(Exception e){
            out.println("<div> Error " + e + "</div>");
        }
        out.close();
    }
}
