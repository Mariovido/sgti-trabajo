import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Registro extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse res) {

        try{
            PrintWriter out;
            out = res.getWriter();
            res.setContentType("text/html");
            out.println("<!DOCTYPE html>");
            out.println("<html lang='en'>");
            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
            out.println("<title>Cuatro En Raya</title>");
            out.println("<link rel='stylesheet' href='http://juegocraya.duckdns.org:8080/sgti-trabajo/web/resources/styles/main.css'> ");
            out.println("<link rel='stylesheet' href='http://juegocraya.duckdns.org:8080/sgti-trabajo/web/resources/styles/registro.css'>");
            out.println("<link rel='stylesheet' href='web/resources/styles/main.css'> ");
            out.println("<link rel='stylesheet' href='web/resources/styles/registro.css'>");
            out.println("<script src='web/resources/js/util.js'></script>");
            out.println("</head>");
            out.println("<body>");
            out.println("<header class='main-header'>");
            out.println("<nav class='main-header__nav'>");
            out.println("<ul class='main-header__item-list'>");
            out.println("<li class='main-header__item'><a class='active' href=''>Registrarse</a></li>");
            out.println("<li class='main-header__item'><a href='/sgti-trabajo/login'>Iniciar sesión</a></li>");
            out.println("</ul>");
            out.println("</nav>");
            out.println("</header>");

            out.println("<main>");
            out.println("<div class='user'>");
            out.println("<header class='user__header'>");
            out.println("<img src='https://s3-us-west-2.amazonaws.com/s.cdpn.io/3219/logo.svg' alt='logo' />");
            out.println("<h1 class='user__title'>Regístrese para poder jugar</h1>");
            out.println("</header>");
            out.println("<form name='formulario' class='form' action='http://juegocraya.duckdns.org:8080/sgti-trabajo/registro' method='POST' onsubmit='return validarRegistro()'>");
            out.println("<div class='form__group'>");
            out.println("<input name='NAME' type='text' placeholder='Nombre' class='form__input' />");
            out.println("</div>");

            out.println("<div class='form__group'>");
            out.println("<input name='USER' type='text' placeholder='Usuario' class='form__input' />");
            out.println("</div>");

            out.println("<div class='form__group'>");
            out.println("<input name='MAIL' type='email' placeholder='Email' class='form__input' />");
            out.println("</div>");

            out.println("<div class='form__group'>");
            out.println("<input name='PASS' type='password' placeholder='Contraseñaa' class='form__input' />");
            out.println("</div>");

            out.println("<button class='btn' type='submit'>Registrarse</button>");
            out.println("</form>");
            out.println("</div>");
            out.println("</main>");
            out.println("</body>");
            out.println("</html>");
            out.close();

        }catch(Exception e){
            System.out.println("<div> Error en REGISTRO " + e + "</div>");
        }
    }

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
                misesion.setAttribute("Nick", nick);
                //enviamos a principal
                res.sendRedirect("http://juegocraya.duckdns.org:8080/sgti-trabajo/principal");
                //RequestDispatcher rd = getServletContext().getRequestDispatcher("/principal");
                //rd.forward(req, res);
                /*
                out = res.getWriter();
                res.setContentType("text/html");
                out.println("<HTML><BODY>");
                out.println("<DIV>El registro ha sido un Ã©xito clicke para continuar</DIV>");
                out.println("<FORM ACTION = '/sgti-trabajo/principal' METHOD = 'POST'>");
                out.println("<INPUT TYPE = 'TEXT' NAME = 'USER' VALUE ='" + nick + "'>");
                out.println("<INPUT TYPE = 'SUBMIT' VALUE = 'CONTINUAR'>");
                out.println("</FORM></BODY></HTML>");
                 */
                ps.close();
                con.close();
                //out.close();
            }
        } catch(Exception e){
            System.out.println( "<div> Hay un error en el codigo </div>"+ e );
        }

    }
}
