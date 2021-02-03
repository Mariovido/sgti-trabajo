import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Registro extends HttpServlet {
    public void doPost(HttpServletRequest req, HttpServletResponse res) /*throws IOException, ServletException*/ {
        Connection con;
        Statement st,st2;
        ResultSet rs2;
        String SQL,SQL2, nick, correo, pass, metodo, nombre;
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

               //try { 
            
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cuatroenraya?serverTimezone=UTC","root","1234");
            if (con==null){
                out.println("<div>no hay conexion</div>");
            }
            
            //if(con!=null){
               // out.println("<div> hay conexion con bbdd </div>");
            //}
            con.setAutoCommit(false);
            st = con.createStatement();
            SQL="INSERT INTO Usuarios (Nick, Correo, Contraseña, Metodo, Nombre, Conectado) VALUES ('" + nick + "', '" +
            correo + "', '" + pass + "', '" + metodo + "', '" + nombre + "', " + conectado + ")";
            
                //aqui se inserta el nuevo usuario que se ha registrado
                result =st.executeUpdate(SQL); // si result es 1 significa que se ha hecho bien el insert
                con.commit();
                con.setAutoCommit(true);
          
                // si hay un usuario se crea una variable de sessión
                HttpSession misesion = req.getSession(true);
                
                // creamos la SQL para recoger el IdUsuario
                st2 = con.createStatement();
                SQL2 = "SELECT * FROM Usuarios WHERE Usuarios.Nick='"+nick+"'";
                rs2 = st2.executeQuery(SQL2);
               //rs2.next();
               if(rs2==null){
                   out.println("<div> el objeto rs2 es nulo </div>");
                }
               while(!rs2.next()){
                   misesion.setAttribute("IdUsuario", rs2.getString(1));
               }
                rs2.close();
                // y se redirige a la pantalla de registro de partidas iniciadas
                st.close();
                st2.close();
                con.close();
              out.close();
            res.sendRedirect("http://localhost:8080/sgti-trabajo/principal");
                
        } catch(Exception e){
            System.out.println( "<div> Hay un error en el codigo </div>"+ e );
        }
        
    }
}
