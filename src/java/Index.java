import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Index extends HttpServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse res)  throws IOException, ServletException {
        RequestDispatcher rd = req.getRequestDispatcher("web/inicio.html");
        rd.forward(req, res);
    }
}