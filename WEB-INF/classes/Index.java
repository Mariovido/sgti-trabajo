import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Index extends HttpServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse res)  throws IOException, ServletException {
        RequestDispatcher rd = req.getRequestDispatcher("web/inicio.html");
        rd.forward(req, res);
        /*
        PrintWriter out = res.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("<title>Cuatro En Raya</title>");
        out.println("<link rel='stylesheet' href='http://juegocraya.duckdns.org:8080/sgti-trabajo/web/resources/styles/main.css'>");
        out.println("</head>");
        out.println("<body>");
        out.println("<header class='main-header'>");
        out.println("<nav class='main-header__nav'>");
        out.println("<ul class='main-header__item-list'>");
        out.println("<li class='main-header__item'><a href='web/registro.html'>Registrarse</a></li>");
        out.println("<li class='main-header__item'><a href='web/login.html'>Iniciar sesión</a></li>");
        out.println("</ul>");
        out.println("</nav>");
        out.println("</header>");

        out.println("<main>");
        out.println("<h1>Bienvenido</h1>");
        out.println("<p>Aquí podrá jugar al 4 en raya</p>");
        out.println("</main>");
        out.println("</body>");
        out.println("</html>");
        out.close();
        */
    }
}