import java.io.*;
import java.sql.*;
import javax.http.*;
import javax.http.servlet.*;



public class login extends HttpServlet{
    public void doGet(HttpServletRequest req, HttpServletResponse res)throws IOException, ServletException{
        RequestDispatcher rd = req.getRequestDispatcher("web/login.html");
        rd.forward(req, res);


        Connection con;
        Statement st;
        ResultSet rs;
        String SQL, user, pass;
        PrintWriter out;

        user=req.getParameter("user");
        pass=req.getParameter("pass");
        try{class.forName("com.mysql.jdbc.Driver");
    } catch(Exception e){
        out.println("<div> No se pudo cargar el puente jdbc </div>");
    }

    try{ 
        con = DriverManager.getConnection("jdbc:mysql://");
        st= con.createStatement();
        SQL="SELECT * FROM usuarios WHERE usuarios.Nombre='"+user+"' AND usuarios.constrasena='"+pass+"'";
        rs=st.executeQuery(SQL);

        if(!rs.next()){
            // si no hay coincidencia se vuelve a inicio
            res.sendRedirect("http://localhost:8080/sgti-trabajo/inicio");

        }else{
            // si hay un usuario se crea una variable de sessión
            HttpSession misesion = req.getSession(true);
            // y se redirige a la pantalla de registro de partidas iniciadas
            res.sendRedirect("http://localhost:8080/sgti-trabajo/")
            
        }
    }catch(Exception e){
        out.println("<div> Error " +e+ "</div>");
    }
}