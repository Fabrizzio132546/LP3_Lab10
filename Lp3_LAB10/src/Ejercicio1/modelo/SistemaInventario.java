/*************************************************************************************
ARCHIVO	: SistemaInventario.java
FECHA	: 25/11/2025
*************************************************************************************/
package Ejercicio1.modelo; 

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SistemaInventario {
    
    private List<ParAsociado> inventario;
    private RepositorioTXT repoTxt;
    private RepositorioBD repoBd;

    public SistemaInventario() {
        this.inventario = new ArrayList<>();
        this.repoTxt = new RepositorioTXT();
        this.repoBd = new RepositorioBD();
    }
    public void agregarRegistro(Equipo e, Mantenimiento m) {
        if (m.getId() != -1) {
            inventario.removeIf(p -> p.getEquipo().getId() == e.getId() && p.getMantenimiento().getId() == -1);
        }
        for (ParAsociado p : inventario) {
            if (p.getEquipo().getId() == e.getId()) {
                p.getEquipo().setNombre(e.getNombre());
                p.getEquipo().setTipo(e.getTipo());
                p.getEquipo().setDescripcionTecnica(e.getDescripcionTecnica());
            }
        }
        inventario.add(new ParAsociado(e, m));
    }
    public void eliminarPorIdMantenimiento(int idMant) {
        ParAsociado target = inventario.stream()
                .filter(p -> p.getMantenimiento().getId() == idMant)
                .findFirst()
                .orElse(null);

        if (target != null) {
            Equipo eq = target.getEquipo();
            inventario.remove(target);

            boolean existeTodavia = inventario.stream()
                    .anyMatch(p -> p.getEquipo().getId() == eq.getId());

            if (!existeTodavia) {
                Mantenimiento fantasma = new Mantenimiento(-1, "Sin Asignar", "N/A", LocalDate.now(), 0.0);
                inventario.add(new ParAsociado(eq, fantasma));
            }
        }
    }
    public void eliminarEquipoTotal(int idEquipo) {
        inventario.removeIf(p -> p.getEquipo().getId() == idEquipo);
    }
    
    public void vaciarInventario() {
        inventario.clear();
    }

    public Equipo buscarEquipoPorId(int id) {
        return inventario.stream()
                .filter(p -> p.getEquipo().getId() == id)
                .map(ParAsociado::getEquipo)
                .findFirst()
                .orElse(null);
    }

    public List<ParAsociado> obtenerDatos(String filtroTipo, String filtroModo) {
        java.util.stream.Stream<ParAsociado> stream = inventario.stream();

        if (filtroTipo != null && !filtroTipo.equals("Todos") && !filtroTipo.isEmpty()) {
            stream = stream.filter(p -> p.getEquipo().getTipo().equalsIgnoreCase(filtroTipo));
        }

        if (filtroModo != null) {
            switch (filtroModo) {
                case "Solo Equipos": 
                    stream = stream.filter(p -> p.getMantenimiento().getId() == -1);
                    break;
                case "Solo Asociaciones":
                    stream = stream.filter(p -> p.getMantenimiento().getId() != -1);
                    break;
                default: break;
            }
        }
        return stream.collect(Collectors.toList());
    }
    public int calcularCantidadEquipos(List<ParAsociado> listaFiltrada) {
        if (listaFiltrada == null) return 0;
        return (int) listaFiltrada.stream().map(p -> p.getEquipo().getId()).distinct().count();
    }
    public double calcularTotalInversion(List<ParAsociado> listaFiltrada) {
        if (listaFiltrada == null) return 0.0;
        return listaFiltrada.stream().mapToDouble(p -> p.getMantenimiento().getCosto()).sum();
    }
    public int getCantidadEquipos() { return calcularCantidadEquipos(this.inventario); }
    public double getTotalInversion() { return calcularTotalInversion(this.inventario); }
    public int generarIdEquipo() {
        return inventario.stream().mapToInt(p -> p.getEquipo().getId()).max().orElse(0) + 1;
    }
    public int generarIdMantenimiento() {
        return inventario.stream().mapToInt(p -> p.getMantenimiento().getId()).max().orElse(0) + 1;
    }
    public void guardarEnTXT() throws IOException {
        repoTxt.guardar(inventario);
    }
    public void cargarDeTXT() throws IOException {
        this.inventario = repoTxt.cargar();
    }
    public void borrarArchivoLocal() {
        repoTxt.borrarArchivoFisico();
        vaciarInventario();
    }
    public void guardarEnBD() throws SQLException {
        repoBd.guardarTodo(inventario);
    }
    public void cargarDeBD() throws SQLException {
        this.inventario = repoBd.cargarTodo();
    }
    public boolean borrarBaseDatosFisica() {
        System.gc(); 
        File f = new File("inventario.db");
        if(f.exists()) {
            return f.delete();
        }
        return false;
    }
    public java.util.Map<String, Integer> obtenerConteoPorTipo() {
        return inventario.stream()
                .collect(Collectors.groupingBy(
                        p -> p.getEquipo().getTipo(), 
                        Collectors.summingInt(e -> 1)
                ));
    }
}


