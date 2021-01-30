import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Principal extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        Connection con;
        Statement st;
        ResultSet rs;
        String SQL, IdUsuario;
        PrintWriter out;

        out = res.getWriter();
        try {
            HttpSession sesion = req.getSession(false);

            if(sesion!=null) {
                IdUsuario = (String)sesion.getAttribute("IdUsuario");

                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection("jdbc:mysql://");
                st = con.createStatement();
                SQL = "SELECT Partidas.IdPartida, Partidas.Turno, Partidas.Finalizada FROM Partidas, UsuarioPartidas WHERE Partidas.IdPartida = UsuarioPartidas.IdPartida AND UsuarioPartidas.IdUsuario = " + IdUsuario;
                rs=st.executeQuery(SQL);

                //HTML
                out.println("<!DOCTYPE html>");
                out.println("<html lang='en'>");
                out.println("<head>");
                out.println("    <meta charset='UTF-8'>");
                out.println("    <meta name='viewport' content='width=device-width, initial-scale=1.0'>");
                out.println("    <title>Cuatro En Raya</title>");
                out.println("    <link rel='stylesheet' href='web/resources/styles/main.css'>");
                out.println("    <link rel='stylesheet' href='web/resources/styles/partidas.css'>");
                out.println("    <link rel='stylesheet' href='web/resources/styles/registro.css'>");
                out.println("</head>");
                out.println("<body>");
                out.println("    <header class='main-header'>");
                out.println("        <nav class='main-header__nav'>");
                out.println("            <ul class='main-header__item-list'>");
                out.println("                <li class='main-header__item'><a class='active' href=''>Mis partidas</a></li>");
                out.println("                <li class='main-header__item'><a href=''>Mi cuenta</a></li>");
                out.println("            </ul>");
                out.println("        </nav>");
                out.println("    </header>");
                out.println("    <main>");
                out.println("        <h1>Bienvenido</h1>");
                out.println("        <p>Aquí podrá ver todas sus partidas iniciadas</p>");
                out.println("                        <div class='user'>");
                out.println("                    <header class='user__header'>");
                out.println("                        <h1 class='user__title'>Inicie una nueva partida</h1>");
                out.println("                    </header>");
                out.println("                <form class='form' name='nueva' action='' method='POST' onsubmit='validar()'>");
                out.println("                    <div class='form__group'>");
                out.println("                        <input type='text' name='IDUSUARIO' class='form__input'>");
                out.println("                    </div>");
                out.println("                    <div class='form__group'>");
                out.println("                        <input type='submit' value='Crear nueva partida' class='form__input'>");
                out.println("                    </div>");
                out.println("                </form>");
                out.println("                </div>");
                out.println("        <div class='container'>");
                while (!rs.next()) {
                    // En el onclick hay que poner el servlet de game y enviar la id del juego seleccionado
                    if(rs.getString(3) == 1){
                        out.println("            <div class='bloque' onclick='location.href=''>Partida finalizada </br>Id:"+rs.getString(1)+"</div>");
                        
                    }
                    else if(rs.getString(2) == IdUsuario){  
                        out.println("            <div class='bloque' onclick='location.href=''>Su turno </br>Id:"+rs.getString(1));
                        out.println("               <form method='POST' action=''>");
                        out.println("                   <input type='hidden' value='"+rs.getString(1)+"' name='ID'>");
                        out.println("                   <input type='submit' value='Seleccionar' class='form__input'>");
                        out.println("               </form>");
                        out.println("             </div>");
                    }
                    else{
                        out.println("            <div class='bloque' onclick='location.href=''>Turno del oponente </br>Id:"+rs.getString(1));
                        out.println("               <form method='POST' action=''>");
                        out.println("                   <input type='hidden' value='"+rs.getString(1)"' name='ID'>");
                        out.println("                   <input type='submit' value='Seleccionar' class='form__input'>");
                        out.println("               </form>");
                        out.println("             </div>");
                    }
                }
                out.println("        </div>");
                out.println("    </main>");
                out.println("</body>");
                out.println("</html>");
                
                rs.close();
                st.close();
                con.close();
            } else {
                out.close();
                res.sendRedirect("http://localhost:8080/sgti-trabajo/inicio");
            }
        } catch (Exception e){
            out.println("<div> Error " + e + "</div>");
        }
        out.close();
    }
}