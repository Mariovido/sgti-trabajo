import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.json.*;

public class Ajaxhandler extends HttpServlet {
    public void doPost(HttpServletRequest req, HttpServletResponse res) {
        try{
            Connection con;
            Statement st, st4;
            PreparedStatement ps, ps3;
            ResultSet rs,rs4;
            String SQL, SQL2, SQL3, SQL4;
            String columna = req.getParameter("COLUMNA");
            int columnaInt = Integer.parseInt(columna);
            //sacar el id de partida
            String numeroPartida = req.getParameter("PARTIDA");
            // se pasa el idPartida a int para usarlo en la SQL
            int idPartida = Integer.parseInt(numeroPartida);
            //sacar el userid de la sesion
            HttpSession sesion = req.getSession(false);
            int IdUsuario= (int) sesion.getAttribute("IdUsuario");
            //mirar si es el turno del user que ha enviado la peticion
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cuatroenraya?serverTimezone=UTC","root","1234");
            st = con.createStatement();
            SQL="SELECT Partidas.Turno, Partidas.JugadorUno, Partidas.JugadorDos, Partidas.EstadoPartida FROM Partidas WHERE Partidas.IdPartida =" + idPartida;
            rs = st.executeQuery(SQL);
            if(rs.next()){
                int turno = rs.getInt(1);
                int j1 = rs.getInt(2);
                int j2 = rs.getInt(3);
                int nextJ = j1;// aqui en siguiente jugador creo que esta mal y seria lo siguiente:

                //si es su turno, introducir la ficha que ha colocado en la base de datos

                //variables para la puntuacion

                if(turno == IdUsuario){
                    //primero sacar el mapa y ver quien es quien y luego meter la ficha con el insert
                    String tablero = rs.getString(4);

                    //ahora ver si colocar un 1 o un 2
                    boolean colocar1 = false;
                    if (IdUsuario == j1){
                        colocar1 = true;
                        nextJ = j2;
                    }
                    //si colocar1 == true, pondremos un 1, si no, un 2
                    //tenemos la columna en la que hay que ponerla que es String columna
                    //String columnaClickada = tablero.substring(7*columna,7+7*columna); //columna clickada
                    //bucle descendente para encontrar la ultima ficha colocada
                    String tableroRes;
                    for (int j=6; j>=0; j--){
                        if (tablero.charAt(j+8*columnaInt) == '0'){ //cuando encuentre un 0, si colocar1 es true coloca un 1 si no, un 2
                            if (colocar1){
                                tableroRes = changeCharInPosition(j+8*columnaInt, '1',tablero);
                            } else{
                                tableroRes = changeCharInPosition(j+8*columnaInt, '2',tablero);
                            }
                            int pos = j + 8*columnaInt;// para saber la fila

                            int puntuacion;
                            int cero =0;
                            // sacamos de la bbdd los datos de la partida : puntos j1 y j2 turnos ...
                            st4= con.createStatement();
                            SQL4 ="SELECT Partidastats.PuntosJugadorUno, Partidastats.PuntosJugadorDos, Partidastats.TurnosJugados FROM Partidastats WHERE Partidastats.IdPartida= " +idPartida;
                            rs4 = st4.executeQuery(SQL4);
                            rs4.next();
                            int turnosJugados = rs4.getInt(3); // sacamos los turnos jugados 
                            turnosJugados = turnosJugados +1; // sumamos un turno mas 

                            // dependiendo de quien este jugando, el valor de puntuacion sera del jugadorUno o jugadorDos
                            if (j1 ==turno){// Sacar de la BBDD, (tablaStats), dependiendo del jugador
                                puntuacion = rs4.getInt(1) ; 
                            }else{
                                puntuacion = rs4.getInt(2);
                            }

                            puntuacion = getPuntuacion(puntuacion, tableroRes, j, columnaInt, colocar1);

                            //System.out.println("Puntuacion = " + puntuacion);
                            // Actualizar la BBDD.
                            if(j1 ==turno){// se actualiza la bbdd dependiendo si es el turno del j1 O j2
                                //int puntJ2 = rs4.getInt(2);
                                con.setAutoCommit(false);
                                SQL3= "UPDATE Partidastats SET TurnosJugados=" + turnosJugados+", PuntosJugadorUno=" + puntuacion + " WHERE Partidastats.IdPartida =" +idPartida;
                                ps3= con.prepareStatement(SQL3);
                                int result3 = ps3.executeUpdate();
                                con.setAutoCommit(true);
                                ps3.close();
                            }else{
                                //int puntJ1 = rs4.getInt(1);
                                con.setAutoCommit(false);
                                SQL3= "UPDATE Partidastats SET TurnosJugados=" + turnosJugados+", PuntosJugadorDos=" + puntuacion + " WHERE Partidastats.IdPartida =" +idPartida;
                                ps3= con.prepareStatement(SQL3);
                                int result3 = ps3.executeUpdate();
                                con.setAutoCommit(true);
                                ps3.close();

                            }

                            con.setAutoCommit(false);
                            SQL2="UPDATE Partidas SET EstadoPartida = '"+tableroRes+"', Turno = "+nextJ+" WHERE Partidas.IdPartida ="+idPartida;
                            ps = con.prepareStatement(SQL2);
                            int result = ps.executeUpdate();
                            con.commit();
                            con.setAutoCommit(true);

                            rs4.close();
                            ps.close();
                            st4.close();
                            break;
                        }
                    }
                }
            }
            rs.close();
            st.close();
            con.close();
        }catch(Exception e){
            System.out.println("Error en ajaxhandler: "+e);
        }
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res)  {
        //GET 
        /*
        Debe recibir el parametro PARTIDA que contiene el id de la partida.
        Busca en la base de datos el estado de la partida y lo envia de vuelta como json
         */
        try{
            Connection con;
            String SQL;
            HttpSession sesion = req.getSession(false);
            int IdUsuario= (int) sesion.getAttribute("IdUsuario");
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cuatroenraya?serverTimezone=UTC","root","1234");
            //sacar el id de partida
            String numeroPartida = req.getParameter("PARTIDA");
            // se pasa el idPartida a int para usarlo en la SQL
            int idPartida = Integer.parseInt(numeroPartida);
            Statement st, statsST;
            ResultSet rs, statsRS;
            SQL="SELECT Partidas.EstadoPartida, Partidas.Turno, Partidas.JugadorUno FROM Partidas WHERE Partidas.IdPartida =" + idPartida;
            st = con.createStatement();
            rs = st.executeQuery(SQL);
            if (rs.next()){
                //sacamos el mapa del tablero
                String tablero = rs.getString(1);
                //sacamos de quien es el turno para enviarlo
                boolean turnoCliente = false; 
                int turno = rs.getInt(2);
                if (turno == IdUsuario){
                    turnoCliente = true;
                }
                //indicamos quien es el j1, no hace falta saber quien es j2 porq es implicito que es el otro
                String jugador1 = "contrincante"; 
                int j1 = rs.getInt(3);
                if (j1 == IdUsuario){
                    jugador1 = "clientelocal";
                }

                //sacamos la puntuacion
                statsST=con.createStatement();
                String statSQL= "SELECT Partidastats.PuntosJugadorUno, Partidastats.PuntosJugadorDos, Partidastats.TurnosJugados FROM Partidastats WHERE Partidastats.IdPartida="+ idPartida ;
                statsRS= statsST.executeQuery(statSQL);
                int pj1 = 0;
                int pj2 = 0;
                if (statsRS.next()){
                    pj1 = statsRS.getInt(1);
                    pj2 = statsRS.getInt(2);
                }
                //ahora formamos la respuesta, que esta compuesta por el string de 012, por el turno y por quien es cada unno
                JSONObject json = new JSONObject();
                json.put("tablero",tablero);
                json.put("turno",turnoCliente);
                json.put("j1",jugador1);
                json.put("puntos1",pj1);
                json.put("puntos2",pj2);

                PrintWriter out = res.getWriter();
                //decimos que la respuesta es un json
                res.setContentType("application/json"); 
                res.setCharacterEncoding("UTF-8");
                //enviamos el json
                out.print(json);
                out.flush();
                out.close();
                statsRS.close();
                statsST.close();
            }
            rs.close();
            st.close();
            
            con.close();
        }catch(Exception e){
            System.out.println("Error en ajaxhandler: "+e);
        }
    }

    public String changeCharInPosition(int position, char ch, String str){
        char[] charArray = str.toCharArray();
        charArray[position] = ch;
        return new String(charArray);
    }

    public int getPuntuacion(int puntuacion, String estadoPartida, int fila, int columna, boolean colocar1) { 
        char jugador = (char) '2';
        if(colocar1){
            jugador = (char) '1';
        }

        int posicion = fila + 8*columna;
        int puntuacionSumada = 0;
        int longitudTablero = estadoPartida.length();

        int [] puntuacionHold = new int[7]; 
        /*
        System.out.println("Tablero: " + estadoPartida);
        System.out.println("LongitudTablero: " + longitudTablero);
        System.out.println("Posicion: " + posicion);
        System.out.println("Fila: " + fila);
        System.out.println("Columna: " + columna);
        System.out.println("Jugador: " + jugador);

        System.out.println("Primer if: " + longitudTablero + " > " + (posicion + 9));  
        */
        if(longitudTablero > posicion + 9 ){
            //System.out.println("DOWN_RIGHT: " + estadoPartida.charAt(posicion + 9));
            if (estadoPartida.charAt(posicion + 9) == jugador) {
               // System.out.println("entra en el if de down_right ");
                int puntuacionHoldSimple = sigueRastro(estadoPartida, posicion, jugador, "DOWN_RIGHT", puntuacionHold);
                if (puntuacionHoldSimple >= 2) {
                    puntuacionSumada += puntuacionHoldSimple + 1;
                }
                puntuacionHold[0] = puntuacionHoldSimple;
            }
            //System.out.println("RIGHT: " + estadoPartida.charAt(posicion + 8));
            if (estadoPartida.charAt(posicion + 8) == jugador) {
                int puntuacionHoldSimple = sigueRastro(estadoPartida, posicion, jugador, "RIGHT", puntuacionHold);
                //System.out.println("entra en el if de right ");
                if (puntuacionHoldSimple >= 2) {
                    puntuacionSumada += puntuacionHoldSimple + 1;
                }
                puntuacionHold[1] = puntuacionHoldSimple;
            }
            //System.out.println("UP_RIGHT: " + estadoPartida.charAt(posicion + 7));
            if (estadoPartida.charAt(posicion + 7) == jugador) {
                int puntuacionHoldSimple = sigueRastro(estadoPartida, posicion, jugador, "UP_RIGHT", puntuacionHold);
                if (puntuacionHoldSimple >= 2) {
                    puntuacionSumada += puntuacionHoldSimple + 1;
                }
                puntuacionHold[2] = puntuacionHoldSimple;
            }
            //System.out.println("DOWN: " + estadoPartida.charAt(posicion + 1));
            if (estadoPartida.charAt(posicion + 1) == jugador) {
                int puntuacionHoldSimple = sigueRastro(estadoPartida, posicion, jugador, "DOWN", puntuacionHold);
                //System.out.println("entra en el if de UP_right ");
                if (puntuacionHoldSimple >= 2) {
                    puntuacionSumada += puntuacionHoldSimple + 1;
                }
                puntuacionHold[3] = puntuacionHoldSimple;
            }
        }
        //System.out.println("Segundo if: " + (posicion - 9) + " >= 0"); 
        if( posicion - 9 >= 0 ){
            //System.out.println("UP_LEFT: " + estadoPartida.charAt(posicion - 9));
            if (estadoPartida.charAt(posicion - 9) == jugador) {
                int puntuacionHoldSimple = sigueRastro(estadoPartida, posicion, jugador, "UP_LEFT", puntuacionHold);
                puntuacionSumada += puntuacionHoldSimple;
                puntuacionHold[4] = puntuacionHoldSimple;
            }
           // System.out.println("LEFT: " + estadoPartida.charAt(posicion - 8));
            if (estadoPartida.charAt(posicion - 8) == jugador) {
                int puntuacionHoldSimple = sigueRastro(estadoPartida, posicion, jugador, "LEFT", puntuacionHold);
                puntuacionSumada += puntuacionHoldSimple;
                puntuacionHold[5] = puntuacionHoldSimple;
            }
            //System.out.println("DOWN_LEFT: " + estadoPartida.charAt(posicion - 7));
            if (estadoPartida.charAt(posicion - 7) == jugador) {
                int puntuacionHoldSimple = sigueRastro(estadoPartida, posicion, jugador, "DOWN_LEFT", puntuacionHold);
                puntuacionSumada += puntuacionHoldSimple;
                puntuacionHold[6] = puntuacionHoldSimple;
            }
        }

        //System.out.println("Puntuacion final: " +puntuacion + puntuacionSumada);
        return puntuacion + puntuacionSumada;
    }

    public int sigueRastro(String estadoPartida, int posicion, int jugador, String direccion, int [] puntuacionHold) {
        int puntuacion = 0;
        int sumaPosicion = 0;
        //System.out.println("entra en SIGUERASTRO");
        switch(direccion) {
            case "UP_LEFT": sumaPosicion = - 7; 
            break;
            case "LEFT": sumaPosicion = - 8;
            break;
            case "DOWN_LEFT": sumaPosicion = - 9;
            break;
            case "DOWN": sumaPosicion = + 1;
            break;
            case "UP_RIGHT": sumaPosicion = + 7;
            break;
            case "RIGHT": sumaPosicion = + 8;
            break;
            case "DOWN_RIGHT": sumaPosicion = + 9;
            break;
            default: sumaPosicion = sumaPosicion;
            break;
        }   

        int longitudTablero = estadoPartida.length();
        //System.out.print("Posicion y sumaPosicion: "+posicion + sumaPosicion);

        while((posicion + sumaPosicion) >= 0 && (posicion + sumaPosicion) < longitudTablero) {
            //System.out.println("If dentro del while: " + estadoPartida.charAt(posicion + sumaPosicion));
            if(estadoPartida.charAt(posicion + sumaPosicion) == jugador){
                puntuacion++;
                posicion += sumaPosicion;
                //System.out.println("Puntuacion: " + puntuacion);
                //System.out.println("Nuevo posicion: " + posicion );
            } else {
                break;
            }
        }
        switch(direccion) {
            case "UP_LEFT": if (puntuacionHold[0] < 2) {
                puntuacion = puntuacion + puntuacionHold[0];
                if(puntuacion >= 2) {
                    puntuacion += 1;
                } else {
                    puntuacion = 0;
                };
            }; 
            break;
            case "LEFT": if (puntuacionHold[1] < 2) {
                puntuacion = puntuacion + puntuacionHold[1];
                if(puntuacion >= 2) {
                    puntuacion += 1;
                } else {
                    puntuacion = 0;
                };
            };
            break;
            case "DOWN_LEFT": if (puntuacionHold[2] < 2) {
                puntuacion = puntuacion + puntuacionHold[2];
                if(puntuacion >= 2) {
                    puntuacion += 1;
                } else {
                    puntuacion = 0;
                };
            };
            break;
            default: puntuacion = puntuacion;
            break;
        }   
        return puntuacion;
    }

}