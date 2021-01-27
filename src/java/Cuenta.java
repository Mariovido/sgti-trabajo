import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Cuenta extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        PrintWriter out = res.getWriter();
        try {
            HttpSession sesion = req.getSession(false);

            if(sesion!=null) {
                RequestDispatcher rd = req.getRequestDispatcher("/web/cuenta.html");
                rd.forward(req, res);
            } else {
                res.sendRedirect("http://localhost:8080/sgti-trabajo/inicio");
            }
        } catch(Exception e){
            out.println("<div> Error " + e + "</div>");
        }
    }
}
