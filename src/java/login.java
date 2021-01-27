import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Login extends HttpServlet {
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        Connection con;
        Statement st;
        ResultSet rs;
        String SQL, user, pass;
        PrintWriter out;

        user = req.getParameter("USER");
        pass = req.getParameter("PASS");
        
        out = res.getWriter();
        
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch(Exception e) {
            out.println("<div> No se pudo cargar el puente jdbc </div>");
        }

        try { 
            con = DriverManager.getConnection("jdbc:mysql://");
            st= con.createStatement();
            SQL="SELECT * FROM Usuarios WHERE Nick='" + user + "' AND Contraseña='" + pass + "'";
            rs=st.executeQuery(SQL);

            if(!rs.next()) {
                // si no hay coincidencia se vuelve a inicio
                res.sendRedirect("http://localhost:8080/sgti-trabajo/inicio");

            } else {
                // si hay un usuario se crea una variable de sessión
                HttpSession misesion = req.getSession(true);
                // y se redirige a la pantalla de registro de partidas iniciadas
                res.sendRedirect("http://localhost:8080/sgti-trabajo/principal");

            }
        }catch(Exception e){
            out.println("<div> Error " + e + "</div>");
        }
    }
}
