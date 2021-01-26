import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Registro extends HttpServlet {
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        RequestDispatcher rd = req.getRequestDispatcher("web/registro.html");
        rd.forward(req, res);

        Connection con;
        Statement st;
        ResultSet rs;
        String SQL, nick, correo, pass, metodo, nombre;
        int conectado;
        PrintWriter out;

        //para obtener la variable de sesion y saber si esta conectado o no, se pasa esta variable a un string
        //misesion = (String)sesion.getAttribute("misesion");
        
        nick = req.getParameter("USER");
        correo = req.getParameter("MAIL");
        pass = req.getParameter("PASS");
        metodo = "texto";
        nombre = req.getParameter("NOMBRE");
        conectado = 1;
        
        out = res.getWriter();

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch(Exception e) {
            out.println("<div> No se pudo cargar el puente jdbc </div>");
        }

        try { 
            con = DriverManager.getConnection("jdbc:mysql://");
            st = con.createStatement();
            SQL="INSERT INTO Usuarios (Nick, Correo, Contraseña, Metodo, Nombre, Conectado) VALUES ('" + nick + "', '" +
            correo + "', '" + pass + "', '" + metodo + "', '" + nombre + "', '" + conectado + ")";
            rs=st.executeQuery(SQL);
            
            // si hay un usuario se crea una variable de sessión
            HttpSession misesion = req.getSession(true);
            // y se redirige a la pantalla de registro de partidas iniciadas
            res.sendRedirect("http://localhost:8080/sgti-trabajo/principal");
        } catch(Exception e){
            out.println("<div> Error " + e + "</div>");
        }
    }
}
