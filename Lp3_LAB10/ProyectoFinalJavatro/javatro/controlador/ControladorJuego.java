package javatro.controlador;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;


import java.util.function.Consumer;

import javatro.modelo.JuegoModelo;
import javatro.modelo.core.Carta;
import javatro.modelo.core.Joker;
import javatro.modelo.interfaces.ObservadorJuego;
import javatro.modelo.tienda.ItemTienda;
import javatro.vista.VistaJuego;
import javatro.vista.ventanas.PanelAdmin;
import javatro.vista.paneles.PanelTienda;

public class ControladorJuego implements ObservadorJuego {

    private VistaJuego vista;
    private JuegoModelo modelo;
    private PanelAdmin panelAdmin;
    private PanelTienda panelTienda;
    private Runnable accionVolverMenu;

    public ControladorJuego(VistaJuego vista, Runnable accionVolverMenu) {
        this.vista = vista;
        this.accionVolverMenu = accionVolverMenu;
        this.modelo = new JuegoModelo();
        
        //avisar al controlador cuando se cambia algo
        modelo.agregarObservador(this);

        this.panelAdmin = new PanelAdmin(vista, this);
        this.panelTienda = new PanelTienda(this);

        vista.registrarTienda(panelTienda);
        vista.mostrarMesa();

        setupAdminKeys();
        asignarEventosBotones();

        modelo.reiniciarJuegoTotal();
    }

    private void setupAdminKeys() {
        java.awt.KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e -> {
            if (e.getID() == java.awt.event.KeyEvent.KEY_PRESSED && e.getKeyCode() == java.awt.event.KeyEvent.VK_F1) {
                panelAdmin.setVisible(!panelAdmin.isVisible());
                return true;
            }
            return false;
        });
    }
    
    // lo que pasa cuando hacemos click

    private void asignarEventosBotones() {
        vista.setAccionJugar(e -> {
            List<Carta> seleccionadas = obtenerSeleccionadas();

            if (seleccionadas.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Selecciona al menos 1 carta.");
            } else {
                vista.getPanelMano().getBtnJugar().setEnabled(false);
                modelo.jugarMano(seleccionadas);
            }
        });

        vista.setAccionDescartar(e -> {
            List<Carta> seleccionadas = obtenerSeleccionadas();
            if (!seleccionadas.isEmpty()) {
                modelo.descartar(seleccionadas);
                javatro.util.GestorAudio.getInstancia().reproducirEfecto("click.wav");
            }
        });

        vista.setAccionOrdenarValor(e -> modelo.ordenarManoPorValor());
        vista.setAccionOrdenarPalo(e -> modelo.ordenarManoPorPalo());

        vista.getPanelEstadisticas().getBtnInfoManos().addActionListener(e -> mostrarInfoManos());

        vista.setAccionVolverAlMenu(e -> {
            int confirm = JOptionPane.showConfirmDialog(vista,
                    "¿Seguro que quieres salir al menú?",
                    "Opciones", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION && accionVolverMenu != null) {
                accionVolverMenu.run();
            }
        });
    }

    private void gestionarClickCarta(Carta c) {
        if (c.isSeleccionada()) {
            c.toggleSeleccion();
        } else {
            if (obtenerSeleccionadas().size() < 5) {
                c.toggleSeleccion();
                javatro.util.GestorAudio.getInstancia().reproducirEfecto("click.wav");
            }
        }

        modelo.calcularPrediccion(obtenerSeleccionadas());
        vista.getPanelMano().repaint();
    }

    private List<Carta> obtenerSeleccionadas() {
        List<Carta> sel = new ArrayList<>();
        for (Carta c : modelo.getManoJugador()) {
            if (c.isSeleccionada())
                sel.add(c);
        }
        return sel;
    }

    private void mostrarInfoManos() {
        String info = "GUÍA DE PUNTAJES:\n\n" +
                "Escalera Real:  100 x 8\n" +
                "Poker:          60 x 7\n" +
                "Full House:     40 x 4\n" +
                "Color:          35 x 4\n" +
                "Escalera:       30 x 4\n" +
                "Trío:           30 x 3\n" +
                "Doble Par:      20 x 2\n" +
                "Par:            10 x 2\n" +
                "Carta Alta:     5 x 1";
        JOptionPane.showMessageDialog(vista, info, "Manos de Poker", JOptionPane.INFORMATION_MESSAGE);
    }

    // se ejecuta cuanod el modelo confirma que se uso una mano correcta
    @Override
    public void onManoJugada(String nombreMano, int fichasTotales, int mult, int totalMano, int totalAcumulado,
                             List<Carta> cartasPuntuan) {

        int sumaValorCartas = 0;
        for (Carta c : cartasPuntuan) {
            sumaValorCartas += c.getValor().getValorNumerico();
        }
        int fichasBaseInicio = fichasTotales - sumaValorCartas;

        vista.getPanelEstadisticas().actualizarPrediccion(nombreMano, fichasBaseInicio, mult);
        vista.getPanelEstadisticas().prepararAnimacion(fichasBaseInicio, mult);
        vista.getPanelMesa().mostrarNombreMano("");

        Consumer<Integer> accionAlPuntuar = (valorCarta) -> {
            vista.getPanelEstadisticas().sumarFichasInstantaneo(valorCarta);
        };

        Runnable accionAlFinalizar = () -> {
            vista.getPanelEstadisticas().finalizarAnimacion(fichasTotales, mult, totalMano, totalAcumulado, () -> {

                modelo.finalizarTurno();
                vista.getPanelMano().getBtnJugar().setEnabled(true);

                if (modelo.getPuntajeAcumulado() >= modelo.getPuntajeObjetivo()) {
                    modelo.cobrarRecompensa();
                    JOptionPane.showMessageDialog(vista, "¡RONDA SUPERADA!");
                    abrirTienda();
                } else if (modelo.getManosRestantes() <= 0) {
                    onFinalizarJuego(false, "Te quedaste sin manos.");
                }
            });
        };

        vista.getPanelMesa().reproducirSecuenciaPuntaje(cartasPuntuan, accionAlPuntuar, accionAlFinalizar);
    }
    
    // controaldor pasa los datos a la vista
    
    //se actualiza dinero, ronda .. 

    @Override
    public void onCambioEstadisticas(int puntaje, int objetivo, int manos, int descartes, int dinero, int ronda) {
        vista.actualizarEstadisticas(puntaje, objetivo, manos, descartes, dinero, ronda);
    }
    
    // lo mismo pero con tiendas

    @Override
    public void onCambioCartas(List<Carta> mano, List<Carta> mesa) {
        vista.actualizarManoJugador(mano, this::gestionarClickCarta);
        vista.actualizarMesa(mesa);
    }

    @Override
    public void onFinalizarJuego(boolean victoria, String mensaje) {
        
        // guardamos partida aqui en base bd
        javatro.datos.RegistroPartida registro = new javatro.datos.RegistroPartida(
            modelo.getRondaActual(),
            modelo.getPuntajeAcumulado(),
            modelo.getDinero(),
            victoria
        );
        javatro.datos.GestorHistorial.guardarPartida(registro);

        String titulo = victoria ? "¡Victoria!" : "Fin del Juego";
        int tipo = victoria ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE;

        JOptionPane.showMessageDialog(vista, mensaje, titulo, tipo);

        if (!victoria && accionVolverMenu != null) {
            accionVolverMenu.run();
        }
    }

    @Override
    public void onPrediccionPuntaje(String nombreMano, int fichas, int mult) {
        vista.actualizarPrediccion(nombreMano, fichas, mult);
    }

    @Override
    public void onCambioTienda(List<ItemTienda> items) {
        vista.actualizarTienda(items);
    }

    @Override
    public void onCambioJokers(List<Joker> jokers) {
        vista.actualizarJokers(jokers);
    }

    private void abrirTienda() {
        modelo.rerollTienda();
        vista.mostrarTienda();
    }

    public void cerrarTienda() {
        vista.mostrarMesa();
        modelo.prepararSiguienteRonda();
    }

    public boolean gastarDinero(int cantidad) {
        return modelo.gastarDinero(cantidad);
    }

    public void intentarComprar(int idx) {
        modelo.comprarItem(idx);
    }

    public void solicitarReroll() {
        modelo.intentarReroll();
    }

    public int getDinero() {
        return modelo.getDinero();
    }

    public int jugarSlots(int costo) {
        if (!modelo.gastarDinero(costo)) {
            return -1;
        }

        double azar = Math.random();

        if (azar < 0.10) {
            int premio = costo * 10;
            modelo.agregarDinero(premio);
            return 2;
        } else if (azar < 0.40) {
            int premio = costo * 2;
            modelo.agregarDinero(premio);
            return 1;
        } else {
            return 0;
        }
    }

    public void comprarCartaMisteriosa() {
        int precio = 4;
        if (modelo.gastarDinero(precio)) {
            Carta c = modelo.agregarCartaAlMazo();
            JOptionPane.showMessageDialog(vista,
                    "¡Has comprado una carta extra!\n" + c.toString(),
                    "Carta Misteriosa",
                    JOptionPane.INFORMATION_MESSAGE);
            javatro.util.GestorAudio.getInstancia().reproducirEfecto("click.wav");
        } else {
            JOptionPane.showMessageDialog(vista, "Necesitas $" + precio);
        }
    }

    public void adminGanarRonda() {
        modelo.agregarDinero(1000);
    }

    public void adminIrTienda() {
        abrirTienda();
    }

    public void adminDarDinero(int c) {
        modelo.agregarDinero(c);
    }

    public void adminResetManos() {
        modelo.iniciarNuevaRonda();
    }

    public void adminPerder() {
    }

    public void adminDarMult(int cantidad) {
        modelo.agregarJoker("Bufón");
        javax.swing.JOptionPane.showMessageDialog(vista, "CHEAT: ¡Joker de Multiplicador agregado!");
    }
}