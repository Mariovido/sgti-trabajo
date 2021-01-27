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
                // Falta por hacer el HTML
            } else {
                res.sendRedirect("http://localhost:8080/sgti-trabajo/inicio");
            }
        } catch(Exception e){
            out.println("<div> Error " + e + "</div>");
        }
    }
}
