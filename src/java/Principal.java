import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Principal extends HttpServlet {
    public void doPost(HttpServletRequest req, HttpServletResponse res) {
        Connection con;
        Statement st,st2;
        ResultSet rs,rs2;
        String SQL, SQL2, nick="";
        PrintWriter out;
        int IdUsuario=0;

        try {
            HttpSession sesion = req.getSession(false);     

            if(sesion!=null) {
                out = res.getWriter();
                //IdUsuario = (String)sesion.getAttribute("IdUsuario");
                Class.forName("com.mysql.cj.jdbc.Driver");
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cuatroenraya?serverTimezone=UTC","root","1234");
                //recogemos el nick del form 
                // recogemos el nick del form anterior y hacemos un select para saber el IDusuario
                nick= req.getParameter("USER");
                st2 = con.createStatement();
                SQL2= "SELECT * FROM Usuarios WHERE Usuarios.Nick='"+nick+"'";
                rs2 =st2.executeQuery(SQL2);
                // se ha comprobado que ni el nick ni el rs2 es null.
                if(rs2.next()){
                    IdUsuario = rs2.getInt(1);
                    sesion.setAttribute("IdUsuario", IdUsuario);
                    //out.println(IdUsuario);
                    // se ha comprobado que se recoge bien el IdUsuario

                    st = con.createStatement();
                    SQL = "SELECT Partidas.IdPartida, Partidas.Turno, Partidas.Finalizada FROM Partidas, UsuariosPartidas WHERE Partidas.IdPartida = UsuariosPartidas.IdPartida AND UsuariosPartidas.IdUsuario =" + IdUsuario;
                    rs = st.executeQuery(SQL);

                    //HTML
                    res.setContentType("text/html");
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
                    out.println("                <li class='main-header__item'><form method='POST' action='/sgti-trabajo/cuenta'><button type='submit'>Mi cuenta</button></form></li>");
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
                    out.println("                <form class='form' name='nueva' action='/sgti-trabajo/nuevapartida' method='POST' onsubmit='validar()'>");
                    out.println("                    <div class='form__group'>");
                    out.println("                        <input type='text' name='NICK' class='form__input' placeholder='nickname'>");
                    out.println("                    </div>");
                    out.println("                    <div class='form__group'>");
                    out.println("                        <input type='submit' value='Crear nueva partida' class='form__input'>");
                    out.println("                    </div>");
                    out.println("                </form>");
                    out.println("                </div>");
                    out.println("        <div class='container'>");
                    while (!rs.next()) {
                        if(rs.getString(3).equals("1")){
                            out.println("            <div class='bloque' onclick='location.href=''>Partida finalizada </br>Id:"+rs.getString(1)+"</div>");

                        }
                        else if(rs.getString(2).equals(String.valueOf(IdUsuario))){  
                            out.println("            <div class='bloque'>Su turno </br>Id:"+rs.getString(1));
                            out.println("               <form method='POST' action='/sgti-trabajo/game'>");
                            out.println("                   <input type='hidden' value='"+rs.getString(1)+"' name='ID'>");
                            out.println("                   <input type='submit' value='Seleccionar' class='form__input'>");
                            out.println("               </form>");
                            out.println("             </div>");
                        }
                        else{
                            out.println("            <div class='bloque'>Turno del oponente </br>Id:"+rs.getString(1));
                            out.println("               <form method='POST' action='/sgti-trabajo/game'>");
                            out.println("                   <input type='hidden' value='"+rs.getString(1)+"' name='ID'>");
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
                    out.close();

                }
            } else {   
                res.sendRedirect("http://juegocraya.duckdns.org:8080/sgti-trabajo/inicio");
            }
        } catch (Exception e){
            System.out.println("<div> Error " + e + "</div>");
        }
    }
}