/*************************************************************************************
ARCHIVO : ControladorInventario.java
FECHA   : 25/11/2025
*************************************************************************************/
package Ejercicio1.controlador;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import Ejercicio1.modelo.Equipo;
import Ejercicio1.modelo.Mantenimiento;
import Ejercicio1.modelo.ParAsociado;
import Ejercicio1.modelo.ReproductorAudio;
import Ejercicio1.modelo.SistemaInventario;
import Ejercicio1.vista.DialogoEditar;
import Ejercicio1.vista.DialogoRegistro;
import Ejercicio1.vista.VistaInventario;

public class ControladorInventario {

    private SistemaInventario modelo;
    private VistaInventario vista;

    public ControladorInventario(SistemaInventario modelo, VistaInventario vista) {
        this.modelo = modelo;
        this.vista = vista;

        asignarEventos();
        actualizarInterfaz();
    }

    private void asignarEventos() {
        vista.getBtnRefrescar().addActionListener(e -> actualizarInterfaz());
        vista.getBtnNuevo().addActionListener(e -> mostrarDialogoRegistro());
        vista.getBtnModificar().addActionListener(e -> mostrarDialogoEditar());
        vista.getBtnBorrarMant().addActionListener(e -> eliminarMantenimiento()); 
        vista.getBtnBorrarEquipo().addActionListener(e -> eliminarEquipoTotal());
        
        vista.getBtnVaciar().addActionListener(e -> {
            if (vista.pedirConfirmacion("¿Estás seguro de vaciar TODO el inventario en memoria?")) {
                modelo.vaciarInventario();
                actualizarInterfaz();
                ReproductorAudio.destruir();
            }
        });

        vista.getCmbFiltroTipo().addActionListener(e -> actualizarInterfaz());
        vista.getCmbModo().addActionListener(e -> actualizarInterfaz());
        vista.getCmbVista().addActionListener(e -> actualizarInterfaz());
        
        vista.getBtnGuardarBD().addActionListener(e -> {
            try {
                modelo.guardarEnBD();
                ReproductorAudio.exito();
                vista.mostrarMensaje("Sincronización con BD exitosa.");
            } catch (SQLException ex) {
                ReproductorAudio.error();
                vista.mostrarError("Error SQL al guardar: " + ex.getMessage());
            }
        });

        vista.getBtnCargarBD().addActionListener(e -> {
            if (vista.pedirConfirmacion("Cargar de BD reemplazará los datos actuales. ¿Continuar?")) {
                try {
                    modelo.cargarDeBD();
                    actualizarInterfaz();
                    ReproductorAudio.exito();
                    vista.mostrarMensaje("Datos cargados desde SQLite.");
                } catch (SQLException ex) {
                    ReproductorAudio.error();
                    vista.mostrarError("Error SQL al cargar: " + ex.getMessage());
                }
            }
        });
        
        vista.getBtnBorrarBDFisica().addActionListener(e -> {
            if(vista.pedirConfirmacion("¡ATENCIÓN! Esto eliminará el archivo 'inventario.db' del disco.\nSe perderán los datos guardados en BD.\n¿Está seguro?")) {
                boolean borrado = modelo.borrarBaseDatosFisica();
                if(borrado) {
                    ReproductorAudio.destruir();
                    vista.mostrarMensaje("Base de datos eliminada correctamente.");
                } else {
                    ReproductorAudio.error();
                    vista.mostrarAlerta("No se pudo borrar. Asegúrese de que no haya conexiones abiertas o reinicie la app.");
                }
            }
       });

        vista.getBtnGuardarTXT().addActionListener(e -> {
            try {
                modelo.guardarEnTXT();
                ReproductorAudio.exito();
                vista.mostrarMensaje("Archivo 'inventario.txt' generado correctamente.");
            } catch (IOException ex) {
                ReproductorAudio.error();
                vista.mostrarError("Error de E/S al guardar: " + ex.getMessage());
            }
        });
        
        vista.getBtnCargarTXT().addActionListener(e -> {
            if (vista.pedirConfirmacion("Cargar de TXT reemplazará los datos actuales. ¿Continuar?")) {
                try {
                    modelo.cargarDeTXT();
                    actualizarInterfaz();
                    ReproductorAudio.exito();
                    vista.mostrarMensaje("Datos cargados desde archivo de texto.");
                } catch (IOException ex) {
                    ReproductorAudio.error();
                    vista.mostrarError("Error al leer archivo: " + ex.getMessage());
                }
            }
        });
        
        vista.getBtnBorrarLocal().addActionListener(e -> {
            if(vista.pedirConfirmacion("¿Eliminar archivo 'inventario.txt'?")) {
                modelo.borrarArchivoLocal();
                ReproductorAudio.destruir();
                vista.mostrarMensaje("Archivo local eliminado.");
            }
        });
        
        vista.getBtnCerrarSesion().addActionListener(e -> System.exit(0));
    }

    private void actualizarInterfaz() {
        String filtroTipo = (String) vista.getCmbFiltroTipo().getSelectedItem();
        String filtroModo = (String) vista.getCmbModo().getSelectedItem();   
        String tipoVista  = (String) vista.getCmbVista().getSelectedItem();  

        List<ParAsociado> listaDatos = modelo.obtenerDatos(filtroTipo, filtroModo);

        vista.actualizarListado(listaDatos, tipoVista);

        int totalEq = modelo.calcularCantidadEquipos(listaDatos);
        double totalDinero = modelo.calcularTotalInversion(listaDatos);
        vista.getPanelEstadisticas().actualizarValores(totalEq, totalDinero);

        if(vista.getPanelGrafico() != null) {
            vista.getPanelGrafico().setDatos(modelo.obtenerConteoPorTipo());
        }
    }
    
    private void mostrarDialogoEditar() {
        DialogoEditar dialogo = new DialogoEditar(vista);

        dialogo.getBtnBuscar().addActionListener(e -> {
            String idStr = dialogo.getIdBusqueda();
            if (idStr.isEmpty()) {
                vista.mostrarAlerta("Ingrese un ID.");
                return;
            }

            try {
                int id = Integer.parseInt(idStr);
                Equipo eq = modelo.buscarEquipoPorId(id);

                if (eq != null) {
                    dialogo.setNombre(eq.getNombre());
                    dialogo.setTipo(eq.getTipo());
                    dialogo.setDesc(eq.getDescripcionTecnica());
                    dialogo.habilitarEdicion(true);
                    vista.mostrarMensaje("Equipo encontrado. Puede editar.");
                } else {
                    vista.mostrarError("ID no encontrado.");
                }
            } catch (NumberFormatException ex) {
                vista.mostrarAlerta("El ID debe ser numérico.");
            }
        });

        dialogo.setVisible(true);

        if (dialogo.isConfirmado()) {
            try {
                int id = Integer.parseInt(dialogo.getIdBusqueda());
                Equipo eq = modelo.buscarEquipoPorId(id);
                
                if (eq != null) {
                    eq.setNombre(dialogo.getNombre());
                    eq.setTipo(dialogo.getTipo());
                    eq.setDescripcionTecnica(dialogo.getDesc());

                    actualizarInterfaz();
                    ReproductorAudio.exito(); 
                    vista.mostrarMensaje("Datos del equipo actualizados correctamente.");
                }
            } catch (Exception ex) {
                ReproductorAudio.error();
                vista.mostrarError("Error al actualizar: " + ex.getMessage());
            }
        }
    }

    private void mostrarDialogoRegistro() {
        DialogoRegistro dialogo = new DialogoRegistro(vista);

        dialogo.getBtnNuevoEq().addActionListener(e -> {
            int nextId = modelo.generarIdEquipo();
            dialogo.activarModoNuevo(String.valueOf(nextId)); 
        });

        dialogo.getBtnBuscarEq().addActionListener(e -> {
            if (!dialogo.isIdEditable()) {
                dialogo.activarModoBusqueda(); 
                return;
            }

            try {
                String textoId = dialogo.getIdEquipoStr();
                if(textoId.isEmpty()) {
                    vista.mostrarAlerta("Escriba un ID para buscar.");
                    return;
                }
                
                int idBusqueda = Integer.parseInt(textoId);
                Equipo encontrado = modelo.buscarEquipoPorId(idBusqueda);
                
                if (encontrado != null) {
                    dialogo.setTxtNombre(encontrado.getNombre());
                    dialogo.setCmbTipo(encontrado.getTipo());
                    dialogo.setTxtDescEq(encontrado.getDescripcionTecnica());
                    dialogo.habilitarCamposEquipo(false); 
                    vista.mostrarMensaje("Equipo encontrado.");
                } else {
                    vista.mostrarAlerta("El equipo no existe. Presione 'Nuevo' para crearlo.");
                    dialogo.habilitarCamposEquipo(true);
                }
            } catch (NumberFormatException ex) {
                vista.mostrarAlerta("El ID debe ser un número.");
            }
        });

        dialogo.setTxtIdMant(String.valueOf(modelo.generarIdMantenimiento()));

        dialogo.getBtnGuardarSoloEquipo().addActionListener(e -> dialogo.cerrarConConfirmacion(true));
        dialogo.getBtnGuardarTodo().addActionListener(e -> dialogo.cerrarConConfirmacion(false));

        dialogo.setVisible(true);

        if (dialogo.isConfirmado()) {
            procesarGuardado(dialogo);
        }
    }

    private void procesarGuardado(DialogoRegistro dialogo) {
        try {
            int idEq = Integer.parseInt(dialogo.getIdEquipoStr());
            String nom = dialogo.getNombre();
            String tipo = dialogo.getTipo();
            String descEq = dialogo.getDescEq();

            Equipo eq = modelo.buscarEquipoPorId(idEq);
            if (eq == null) {
                eq = new Equipo(idEq, nom, tipo, descEq);
            } else {
                eq.setNombre(nom);
                eq.setTipo(tipo);
                eq.setDescripcionTecnica(descEq);
            }

            Mantenimiento mant;
            if (dialogo.isSoloEquipo()) {
                mant = new Mantenimiento(-1, "Sin Asignar", "N/A", LocalDate.now(), 0.0);
            } else {
                int idMan = Integer.parseInt(dialogo.getIdMantStr());
                String descM = dialogo.getDescMant();
                String tec = dialogo.getTecnico();
                LocalDate fecha;
                try { fecha = LocalDate.parse(dialogo.getFecha()); } 
                catch (Exception ex) { fecha = LocalDate.now(); }
                double costo = dialogo.getCosto();
                mant = new Mantenimiento(idMan, descM, tec, fecha, costo);
            }

            modelo.agregarRegistro(eq, mant);
            
            actualizarInterfaz();    
            ReproductorAudio.exito(); 
            
        } catch (Exception ex) {
            ReproductorAudio.error(); 
            vista.mostrarError("Error al guardar datos: " + ex.getMessage());
        }
    }

    private void eliminarMantenimiento() {
        String idStr = vista.pedirEntrada("Ingrese ID del Mantenimiento a eliminar:");
        if (idStr != null && !idStr.isEmpty()) {
            try {
                int id = Integer.parseInt(idStr);
                modelo.eliminarPorIdMantenimiento(id);
                actualizarInterfaz();
                ReproductorAudio.destruir();
                vista.mostrarMensaje("Operación realizada.");
            } catch (NumberFormatException e) {
                ReproductorAudio.error();
                vista.mostrarError("ID inválido.");
            }
        }
    }
    
    private void eliminarEquipoTotal() {
        String idStr = vista.pedirEntrada("Ingrese ID del EQUIPO a eliminar (Se borrará todo su historial):");
        if (idStr != null && !idStr.isEmpty()) {
            try {
                int idEq = Integer.parseInt(idStr);
                modelo.eliminarEquipoTotal(idEq);
                actualizarInterfaz();
                ReproductorAudio.destruir();
                vista.mostrarMensaje("Equipo y sus mantenimientos eliminados.");
            } catch (NumberFormatException e) {
                ReproductorAudio.error();
                vista.mostrarError("ID inválido.");
            }
        }
    }
}