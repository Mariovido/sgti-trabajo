import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Nuevapartida extends HttpServlet {
    public void doPost(HttpServletRequest req, HttpServletResponse res) {
        Connection con;
        PrintWriter out;
        String SQL;
        PreparedStatement ps;
        ResultSet rs;

        try{
            HttpSession sesion = req.getSession(false); //devuelve la sesion si existe, si no, no crea una nueva
            if (sesion != null){   
                Class.forName("com.mysql.jdbc.Driver"); 
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cuatroenraya?serverTimezone=UTC","root","1234");

                IdUsuario= (int) sesion.getAttribute("IdUsuario"); //id del j1 que quiere empezar la partida (en int)
                nick = req.getParameter("NICK"); //nick del j2 con el que se quiere empezar la partida

                SQL="";
                ps = con.prepareStatement(SQL);
                result = ps.executeUpdate();
                con.commit();
                con.setAutoCommit(true);
            }
        } catch(Exception e){
            System.out.println("<div> Error " + e + "</div>");
        }
    }
}