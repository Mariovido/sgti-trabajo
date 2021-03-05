import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Login extends HttpServlet {
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
            //out.println("<link rel='stylesheet' href='http://juegocraya.duckdns.org:8080/sgti-trabajo/web/resources/styles/main.css'> ");
            //out.println("<link rel='stylesheet' href='http://juegocraya.duckdns.org:8080/sgti-trabajo/web/resources/styles/registro.css'>");
            out.println("<link rel='stylesheet' href='web/resources/styles/main.css'> ");
            out.println("<link rel='stylesheet' href='web/resources/styles/registro.css'>");
            out.println("<script src='web/resources/js/util.js'></script>");
            out.println("</head>");
            out.println("<body>");
            out.println("<header class='main-header'>");
            out.println("<nav class='main-header__nav'>");
            out.println("<ul class='main-header__item-list'>");
            out.println("<li class='main-header__item'><a href='/registro'>Registrarse</a></li>");
            out.println("<li class='main-header__item'><a class='active' href=''>Iniciar sesiónn</a></li>");
            out.println("</ul>");
            out.println("</nav>");
            out.println("</header>");

            out.println("<main>");
            out.println("<div class='user'>");
            out.println("<header class='user__header'>");
            out.println("<img src='https://s3-us-west-2.amazonaws.com/s.cdpn.io/3219/logo.svg' alt='logo' />");
            out.println("<h1 class='user__title'>Login</h1>");
            out.println("</header>");
            out.println("<form name='formulario-login' class='form' action='http://juegocraya.duckdns.org:8080/sgti-trabajo/login' method='POST' onsubmit='return validarLogin()'>");
            out.println("<div class='form__group'>");
            out.println("<input name='USER' type='text' placeholder='Usuario' class='form__input' />");
            out.println("</div>");

            out.println("<div class='form__group'>");
            out.println("<input name='PASS' type='password' placeholder='Contraseña' class='form__input' />");
            out.println("</div>");

            out.println("<button class='btn' type='submit'>Iniciar sesión</button>");
            out.println("</form>");
            out.println("</div>");
            out.println("</main>");
            out.println("</body>");
            out.println("</html>");
            out.close();

        }catch(Exception e){
            System.out.println("<div> Error en LOGIN " + e + "</div>");
        }
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) {
        Connection con;
        Statement st;
        ResultSet rs;
        String SQL, user, pass;
        //PrintWriter out;

        user = req.getParameter("USER");
        pass = req.getParameter("PASS");

        try {

            Class.forName("com.mysql.jdbc.Driver"); 
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cuatroenraya?serverTimezone=UTC","root","1234");

            st= con.createStatement();
            SQL="SELECT * FROM Usuarios WHERE Nick='" + user + "' AND Contraseña='" + pass + "'";
            rs=st.executeQuery(SQL);

            if(!rs.next()) {
                rs.close();
                st.close();
                con.close();
                res.sendRedirect("http://juegocraya.duckdns.org:8080/sgti-trabajo/inicio");
            } else {
                // si hay un usuario se crea una variable de session
                HttpSession misesion = req.getSession(true);
                //insertamos el nick en una variable de sesion
                misesion.setAttribute("Nick", user);

                // y se redirige a principal
                //RequestDispatcher rd = getServletContext().getRequestDispatcher("/principal");
                //rd.forward(req, res);
                res.sendRedirect("http://juegocraya.duckdns.org:8080/sgti-trabajo/principal");

                
                /*
                out = res.getWriter();
                res.setContentType("text/html");
                out.println("<HTML><BODY>");
                out.println("<DIV>El login ha sido un Ã©xito clicke para continuar</DIV>");
                out.println("<FORM ACTION = '/sgti-trabajo/principal' METHOD = 'POST'>");
                out.println("<INPUT TYPE = 'TEXT' NAME = 'USER' VALUE ='" + user + "'>");
                out.println("<INPUT TYPE = 'SUBMIT' VALUE = 'CONTINUAR'>");
                out.println("</FORM></BODY></HTML>");
                */
                rs.close();
                st.close();
                con.close();
                //out.close();
            }
        }catch(Exception e){
            System.out.println("<div> Error " + e + "</div>");
        }
    }
}
