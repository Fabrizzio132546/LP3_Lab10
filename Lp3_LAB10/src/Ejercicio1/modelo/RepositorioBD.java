/*************************************************************************************
ARCHIVO	: RepositorioBD.java
FECHA	: 25/11/2025
*************************************************************************************/

package Ejercicio1.modelo;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RepositorioBD {
    private static final String URL = "jdbc:sqlite:inventario.db";

    public RepositorioBD() {
    }

    private void asegurarTablas(Connection con) throws SQLException {
        try (Statement stmt = con.createStatement()) {
            String sqlEquipos = "CREATE TABLE IF NOT EXISTS Equipos (" +
                                "id INTEGER PRIMARY KEY, nombre TEXT, tipo TEXT, descripcion TEXT)";
            
            String sqlMant = "CREATE TABLE IF NOT EXISTS Mantenimientos (" +
                             "id INTEGER PRIMARY KEY, descripcion TEXT, tecnico TEXT, fecha TEXT, " +
                             "costo REAL, equipo_id INTEGER, FOREIGN KEY(equipo_id) REFERENCES Equipos(id))";
            
            stmt.execute(sqlEquipos);
            stmt.execute(sqlMant);
        }
    }

    public void guardarTodo(List<ParAsociado> lista) throws SQLException {
        try (Connection con = DriverManager.getConnection(URL)) {
            asegurarTablas(con);
            
            con.setAutoCommit(false); 
            
            try (Statement stmt = con.createStatement()) {
                stmt.executeUpdate("DELETE FROM Mantenimientos");
                stmt.executeUpdate("DELETE FROM Equipos");
            }

            String insEq = "INSERT OR IGNORE INTO Equipos (id, nombre, tipo, descripcion) VALUES (?,?,?,?)";
            String insMan = "INSERT INTO Mantenimientos (id, descripcion, tecnico, fecha, costo, equipo_id) VALUES (?,?,?,?,?,?)";

            try (PreparedStatement psEq = con.prepareStatement(insEq);
                 PreparedStatement psMan = con.prepareStatement(insMan)) {

                for (ParAsociado par : lista) {
                    Equipo e = par.getEquipo();
                    Mantenimiento m = par.getMantenimiento();

                    psEq.setInt(1, e.getId());
                    psEq.setString(2, e.getNombre());
                    psEq.setString(3, e.getTipo());
                    psEq.setString(4, e.getDescripcionTecnica());
                    psEq.executeUpdate();

                    if (m.getId() != -1) {
                        psMan.setInt(1, m.getId());
                        psMan.setString(2, m.getDescripcion());
                        psMan.setString(3, m.getTecnico());
                        psMan.setString(4, m.getFecha().toString());
                        psMan.setDouble(5, m.getCosto());
                        psMan.setInt(6, e.getId());
                        psMan.executeUpdate();
                    }
                }
            }
            con.commit(); 
        }
    }

    public List<ParAsociado> cargarTodo() throws SQLException {
        List<ParAsociado> lista = new ArrayList<>();
        
        try (Connection con = DriverManager.getConnection(URL)) {
            asegurarTablas(con);

            String sql = "SELECT e.id, e.nombre, e.tipo, e.descripcion, " +
                         "m.id, m.descripcion, m.tecnico, m.fecha, m.costo " +
                         "FROM Equipos e LEFT JOIN Mantenimientos m ON e.id = m.equipo_id";

            try (Statement stmt = con.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                while (rs.next()) {
                    Equipo e = new Equipo(
                        rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4)
                    );

                    Mantenimiento m;
                    int idMant = rs.getInt(5);
                    if (rs.wasNull()) {
                        m = new Mantenimiento(-1, "Sin Asignar", "N/A", LocalDate.now(), 0.0);
                    } else {
                        m = new Mantenimiento(
                            idMant, rs.getString(6), rs.getString(7),
                            LocalDate.parse(rs.getString(8)), rs.getDouble(9)
                        );
                    }
                    lista.add(new ParAsociado(e, m));
                }
            }
        }
        return lista;
    }
}