package javatro.datos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class GestorHistorial {


    private static final String URL_DB = "jdbc:sqlite:historial_javatro.db";


    private static Connection conectar() {
        Connection conn = null;
        try {

            conn = DriverManager.getConnection(URL_DB);
        } catch (SQLException e) {
            System.err.println("Error de conexión SQLite: " + e.getMessage());
        }
        return conn;
    }


    public static void inicializarBaseDeDatos() {
        String sql = "CREATE TABLE IF NOT EXISTS partidas (\n"
                + " id integer PRIMARY KEY AUTOINCREMENT,\n"
                + " fecha text NOT NULL,\n"
                + " resultado text NOT NULL,\n"
                + " ronda integer,\n"
                + " puntaje integer,\n"
                + " dinero integer\n"
                + ");";

        try (Connection conn = conectar();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
            System.out.println("Base de datos verificada/creada correctamente.");
        } catch (SQLException e) {
            System.err.println("Error inicializando DB: " + e.getMessage());
        }
    }


    public static void guardarPartida(RegistroPartida partida) {
        String sql = "INSERT INTO partidas(fecha, resultado, ronda, puntaje, dinero) VALUES(?,?,?,?,?)";

        try (Connection conn = conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, partida.getFecha());
            pstmt.setString(2, partida.getResultado());
            pstmt.setInt(3, partida.getRonda());
            pstmt.setInt(4, partida.getPuntaje());
            pstmt.setInt(5, partida.getDinero());
            
            pstmt.executeUpdate();
            System.out.println("Partida guardada en SQLite.");
            
        } catch (SQLException e) {
            System.err.println("Error al guardar partida: " + e.getMessage());
        }
    }


    public static List<RegistroPartida> cargarHistorial() {
        List<RegistroPartida> lista = new ArrayList<>();

        String sql = "SELECT fecha, resultado, ronda, puntaje, dinero FROM partidas ORDER BY id DESC";

        try (Connection conn = conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                RegistroPartida p = new RegistroPartida(
                    rs.getString("fecha"),
                    rs.getInt("ronda"),
                    rs.getInt("puntaje"),
                    rs.getInt("dinero"),
                    rs.getString("resultado")
                );
                lista.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar historial: " + e.getMessage());
        }
        return lista;
    }
}