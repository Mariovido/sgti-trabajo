import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Registro extends HttpServlet {
    public void doPost(HttpServletRequest req, HttpServletResponse res) {
        Connection con;
        PreparedStatement ps;
        String SQL, nick, correo, pass, metodo, nombre;
        byte conectado;
        PrintWriter out;
        int result =0;

        //para obtener la variable de sesion y saber si esta conectado o no, se pasa esta variable a un string
        //misesion = (String)sesion.getAttribute("misesion");
        
        nick = req.getParameter("USER");
        correo = req.getParameter("MAIL");
        pass = req.getParameter("PASS");
        metodo = "texto";
        nombre = req.getParameter("NAME");
        conectado = 1;
        
        try{
            out = res.getWriter();
       
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch(Exception e) {
                out.println("<div> No se pudo cargar el puente jdbc </div>");
            }
            
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cuatroenraya?serverTimezone=UTC","root","1234");
            con.setAutoCommit(false);
            SQL="INSERT INTO Usuarios (Nick, Correo, Contraseña, Metodo, Nombre, Conectado) VALUES ('" + nick + "', '" +
            correo + "', '" + pass + "', '" + metodo + "', '" + nombre + "', " + conectado + ")";
            ps = con.prepareStatement(SQL);
            result = ps.executeUpdate();
            con.commit();
            con.setAutoCommit(true);
          
            // si hay un usuario se crea una variable de session
            HttpSession misesion = req.getSession(true);
            
            res.setContentType("text/html");
            out.println("<HTML><BODY>");
            out.println("<DIV>El registro ha sido un Ã©xito clicke para continuar</DIV>");
            out.println("<FORM ACTION = '/sgti-trabajo/principal' METHOD = 'POST'>");
            out.println("<INPUT TYPE = 'TEXT' NAME = 'MAIL' VALUE ='" + correo + "'>");
            out.println("<INPUT TYPE = 'SUBMIT' VALUE = 'CONTINUAR'>");
            out.println("</FORM></BODY></HTML>");
            ps.close();
            con.close();
            out.close();
        } catch(Exception e){
            System.out.println( "<div> Hay un error en el codigo </div>"+ e );
        }
        
    }
}
