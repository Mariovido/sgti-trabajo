import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Game extends HttpServlet {
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        Connection con;
        Statement st, stnicks;
        ResultSet rs, rsnicks;
        String SQL;
        int IdUsuario, IdPartida;
        PrintWriter out;

        String numeroPartida =req.getParameter("IDPARTIDA"); //le llega de principal al clickar una partida
        IdPartida = Integer.parseInt(numeroPartida);// tiene que ser int para que no salga error en la SQL

        out = res.getWriter();
        try {
            HttpSession sesion = req.getSession(false);

            if(sesion==null) {
                out.close();
                res.sendRedirect("http://localhost:8080/sgti-trabajo/inicio");
            } else {
                IdUsuario= (int) sesion.getAttribute("IdUsuario");// se cambia a INT para que en la SQL no salga error

                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cuatroenraya?serverTimezone=UTC","root","1234");
                if (con==null){
                    out.println("<div>no hay conexion</div>");
                }
                st = con.createStatement();
                SQL = "SELECT Partidas.EstadoPartida, Partidas.Turno, Partidas.JugadorUno, Partidas.JugadorDos FROM Partidas WHERE Partidas.IdPartida =" + IdPartida;
                rs=st.executeQuery(SQL);

                if(rs.next()){
                    String SQLNicks = "SELECT Usuarios.Nick FROM Usuarios WHERE IdUsuario = '"+rs.getString(3)+"' OR IdUsuario = '"+rs.getString(4)+"'";
                    stnicks = con.createStatement();
                    rsnicks = stnicks.executeQuery(SQLNicks);

                    //* Consulta provisional hasta arreglar metodos
                     
                    String SQLprov = "SELECT * FROM Usuarios WHERE IdUsuario =" +IdUsuario;
                    Statement stprov;
                    ResultSet rsprov;
                    stprov = con.createStatement();
                    rsprov = stprov.executeQuery(SQLprov);

                    //HTML
                    res.setContentType("text/html");
                    out.println("<!DOCTYPE html>");
                    out.println("<html lang='en'>");
                    out.println("<head>");
                    out.println("    <meta charset='UTF-8'>");
                    out.println("    <meta name='viewport' content='width=device-width, initial-scale=1.0'>");
                    out.println("    <title>Cuatro En Raya</title>");
                    out.println("    <link rel='stylesheet' href='web/resources/styles/main.css'>");
                    out.println("    <link rel='stylesheet' href='web/resources/styles/tabla.css'>");
                    out.println("    <script src='web/resources/js/game.js'></script>");
                    out.println("    <script src='https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js'></script>");
                    out.println("    <script>var idpartida = '"+IdPartida+"'</script>");
                    out.println("    <script src='web/resources/js/async.js'></script>");
                    out.println("</head>");
                    out.println("<body>");
                    out.println("    <header class='main-header'>");
                    out.println("        <nav class='main-header__nav'>");
                    out.println("            <ul class='main-header__item-list'>");
                    if(rsprov.next()){
                        out.println(" <li class='main-header_item'><form method ='POST' action='/sgti-trabajo/principal'><input type='hidden' name='USER' value='"+rsprov.getString(2)+"'><button type ='submit'>Mis partidas </button></form></li>"); 
                    }   
                    out.println("            </ul>");
                    out.println("        </nav>");
                    out.println("    </header>");
                    out.println("    <main>");
                    out.println("        <div id='jugadores'>");
                    if(rsnicks.next()){
                        out.println("            <span id='local'>"+rsnicks.getString(1)+"</span> vs.");
                    }
                    if(rsnicks.next()){
                        out.println("            <span id='remoto'>"+rsnicks.getString(1)+"</span>");
                    }
                    out.println("        </div>");
                    out.println("    </br>");
                    //Tablero
                    out.println(" <table id='tablero'>");
                    out.println("   <tbody>");
                    for (int i=0; i<7; i++){
                        out.println("<tr>");
                        for (int j=0; j<7; j++){
                            out.println("<td id='"+String.valueOf(i)+String.valueOf(j)+"'></td>"); //quito onclick paint porq lo hara el jquery
                        }
                        out.println("</tr>");
                    }
                    out.println("   </tbody>");
                    out.println(" </table>");
                    out.println("<script>  ");
                    out.println("$(document).on('click', 'td', function() {"); // Cuando hay un "click" en un td se ejecuta la siguiente funcion
                    out.println("var id = $(this).attr('id');");
                    out.println("var col = id.charAt(1);");
                    out.println("$.ajax({");
                    out.println("url     : '/sgti-trabajo/ajaxhandler',"); //aqui poner la url del ajaxhandler
                    out.println("method     : 'POST',");
                    out.println("data     : {COLUMNA : col, PARTIDA : '"+IdPartida+"'},");
                    out.println("success    : paint()});");
                    out.println("});");
                    out.println("</script>");
                    out.println("    </main>");
                    out.println("</body>");
                    out.println("</html>");

                    rs.close();
                    rsnicks.close();
                    rsprov.close();
                    stnicks.close();
                    stprov.close();
                    st.close();
                    con.close();
                }
            }
        } catch (Exception e){
            out.println("<div> Error de GAME " + e + "</div>");
        }
        out.close();
    }
}
