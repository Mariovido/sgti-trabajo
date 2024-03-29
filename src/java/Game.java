import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Game extends HttpServlet {
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        Connection con;
        Statement st, stnicks,st2;
        ResultSet rs, rsnicks,rs2;
        String SQL,SQL2;
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

                    // consulta PartidaStats para que muestre los puntos
                    st2=con.createStatement();
                    SQL2= "SELECT Partidastats.PuntosJugadorUno, Partidastats.PuntosJugadorDos, Partidastats.TurnosJugados FROM Partidastats WHERE Partidastats.IdPartida="+ IdPartida ;
                    rs2= st2.executeQuery(SQL2);

                    rs2.next();
                    int puntosJ1=rs2.getInt(1);
                    int puntosJ2 = rs2.getInt(2);
                    int turnos = rs2.getInt(3);
                            //System.out.println("PUNTOS JUGADOR UNO: "+ puntosJ1 +" , PUNTOS JUGADOR DOS: "+ puntosJ2);
                    int puntosGanador = 0;
                    int Ganador = 0;
                    if(turnos == 49){
                        if(puntosJ1>puntosJ2){
                            puntosGanador = puntosJ1;
                            Ganador= rs.getInt(3);
                            System.out.println("Ganador: " + Ganador);
                            
                        }else{
                            puntosGanador = puntosJ2;
                            Ganador = rs.getInt(4);
                            System.out.println("Ganador: " + Ganador);
                        }
                    }
                    System.out.println("puntosGanador " + puntosGanador +" id ganador " +Ganador);
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
                    out.println(" <li class='main-header__item'><a href='/sgti-trabajo/principal'>Mis partidas</a></li>"); 
                    out.println("            </ul>");
                    out.println("        </nav>");
                    out.println("    </header>");
                    out.println("    <main>");
                    out.println("        <div id='jugadores'>");
                    if(rsnicks.next()){
                        out.println("            <span id='local'>"+rsnicks.getString(1)+"</span>: <span id='puntosj1'>"+puntosJ1 +"</span> vs. ");
                    }
                    if(rsnicks.next()){
                        out.println("            <span id='remoto'>"+rsnicks.getString(1)+"</span>: <span id ='puntosj2'>" + puntosJ2 +"</span> ");
                    }
                     
                    if(turnos ==49){
                        out.println("           <span id='ganador'>" + Ganador +"</span>, con puntos: " +puntosGanador);
                    }
                    out.println("                <span puntosJ1");
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
                    stnicks.close();
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
