import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Login extends HttpServlet {
    public void doPost(HttpServletRequest req, HttpServletResponse res) {
        Connection con;
        Statement st;
        ResultSet rs;
        String SQL, user, pass;
        PrintWriter out;

        user = req.getParameter("USER");
        pass = req.getParameter("PASS");

        try {
            out = res.getWriter();
            Class.forName("com.mysql.jdbc.Driver"); 
            con = DriverManager.getConnection("jdbc:mysql://jdbc:mysql://localhost:3306/cuatroenraya?serverTimezone=UTC","root","1234");

            st= con.createStatement();
            SQL="SELECT * FROM Usuarios WHERE Nick='" + user + "' AND Contraseña='" + pass + "'";
            rs=st.executeQuery(SQL);

            if(!rs.next()) {
                rs.close();
                st.close();
                con.close();
                out.close();
                res.sendRedirect("http://localhost:8080/sgti-trabajo/inicio");
            } else {
                // si hay un usuario se crea una variable de session
                HttpSession misesion = req.getSession(true);
                // y se redirige a la pantalla de registro de partidas iniciadas
                res.setContentType("text/html");
                out.println("<HTML><BODY>");
                out.println("<DIV>El login ha sido un éxito clicke para continuar</DIV>");
                out.println("<FORM ACTION = '/sgti-trabajo/principal' METHOD = 'POST'>");
                out.println("<INPUT TYPE = 'TEXT' NAME = 'USER' VALUE ='" + user + "'>");
                out.println("<INPUT TYPE = 'SUBMIT' VALUE = 'CONTINUAR'>");
                out.println("</FORM></BODY></HTML>");
                rs.close();
                st.close();
                con.close();
                out.close();
            }
        }catch(Exception e){
            System.out.println("<div> Error " + e + "</div>");
        }
    }
}
