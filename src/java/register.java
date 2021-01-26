import java.io.*;
import java.sql.*;
import javax.http.*;
import javax.http.servlet.*;



public class register extends HttpServlet{
    public void doGet(HttpServletRequest req, HttpServletResponse res)throws IOException, ServletException{
        RequestDispatcher rd = req.getRequestDispatcher("web/register.html");
        rd.forward(req, res);


        Connection con;
        Statement st;
        ResultSet rs;
        String SQL, nombre, nick, correo, conectado ,contrasena;
        PrintWriter out;

        //para obtener la variable de sesion y saber si esta conectado o no, se pasa esta variable a un string
        //misesion= (String)sesion.getAttribute("misesion");


        try{class.forName("com.mysql.jdbc.Driver");
    } catch(Exception e){
        out.println("<div> No se pudo cargar el puente jdbc </div>");
    }

    try{ 
        con = DriverManager.getConnection("jdbc:mysql://");
        st= con.createStatement();
        SQL="INSERT INTO nombreTabla (Nombre, nick, correo, conectado, contrasena) VALUES ('"+nombre+"','"+
        nick+"', '"+correo+"', '"+conectado+"', '"+contrasena+"')";
        
      
        }
    }catch(Exception e){
        out.println("<div> Error " +e+ "</div>");
    }
}