import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Registro extends HttpServlet {
    public void doPost(HttpServletRequest req, HttpServletResponse res) {
        Connection con;
        Statement st;
        ResultSet rs;
        PreparedStatement ps;
        String SQL, nick, correo, pass, metodo, nombre,SQL2;
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

        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cuatroenraya?serverTimezone=UTC","root","1234");
            con.setAutoCommit(false);

            st= con.createStatement();
            SQL2 = "SELECT * FROM Usuarios Where Usuarios.Nick='"+nick+"'";
            rs =st.executeQuery(SQL2);
            if(rs.next()){
                rs.close();
                st.close();
                con.close();
                res.sendRedirect("http://juegocraya.duckdns.org:8080/sgti-trabajo/inicio");

            }else{

            
                SQL="INSERT INTO Usuarios (Nick, Correo, Contraseña, Metodo, Nombre, Conectado) VALUES ('" + nick + "', '" +
                correo + "', '" + pass + "', '" + metodo + "', '" + nombre + "', " + conectado + ")";
                ps = con.prepareStatement(SQL);
                result = ps.executeUpdate();
                con.commit();
                con.setAutoCommit(true);

                // si hay un usuario se crea una variable de session
                HttpSession misesion = req.getSession(true);

                out = res.getWriter();
                res.setContentType("text/html");
                out.println("<HTML><BODY>");
                out.println("<DIV>El registro ha sido un Ã©xito clicke para continuar</DIV>");
                out.println("<FORM ACTION = '/sgti-trabajo/principal' METHOD = 'POST'>");
                out.println("<INPUT TYPE = 'TEXT' NAME = 'USER' VALUE ='" + nick + "'>");
                out.println("<INPUT TYPE = 'SUBMIT' VALUE = 'CONTINUAR'>");
                out.println("</FORM></BODY></HTML>");
                ps.close();
                con.close();
                out.close();
            }
        } catch(Exception e){
            System.out.println( "<div> Hay un error en el codigo </div>"+ e );
        }

    }
}
