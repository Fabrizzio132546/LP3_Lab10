package javatro.vista.ventanas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import javatro.modelo.tienda.InfoJoker;
import javatro.vista.componentes.BotonBalatro;
import javatro.vista.componentes.FondoBalatro;
import javatro.vista.paneles.PanelItemColeccion;

public class VentanaColeccion extends JDialog {

    public VentanaColeccion(JFrame owner) {
        super(owner, "Colección de Jokers", true);
        setSize(900, 600);
        setLocationRelativeTo(owner);
        setUndecorated(true);

        inicializarUI();
    }

    private void inicializarUI() {
        FondoBalatro fondo = new FondoBalatro();
        fondo.setLayout(new BorderLayout());
        fondo.setMostrarScanlines(false);
        setContentPane(fondo);

        JLabel lblTitulo = new JLabel("COLECCIÓN DE JOKERS");
        lblTitulo.setFont(new Font("Monospaced", Font.BOLD, 30));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        fondo.add(lblTitulo, BorderLayout.NORTH);

        JPanel panelGrid = new JPanel(new GridLayout(0, 6, 15, 15));
        panelGrid.setOpaque(false);
        panelGrid.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        List<InfoJoker> listaJokers = generarDatosPrueba();

        for (InfoJoker info : listaJokers) {
            panelGrid.add(new PanelItemColeccion(info));
        }

        JScrollPane scroll = new JScrollPane(panelGrid);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        fondo.add(scroll, BorderLayout.CENTER);

        JPanel panelBoton = new JPanel();
        panelBoton.setOpaque(false);
        panelBoton.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        BotonBalatro btnVolver = new BotonBalatro("VOLVER", new Color(220, 60, 60));
        btnVolver.addActionListener(e -> dispose());
        panelBoton.add(btnVolver);

        fondo.add(panelBoton, BorderLayout.SOUTH);
    }

    private List<InfoJoker> generarDatosPrueba() {
        List<InfoJoker> lista = new ArrayList<>();

        for (javatro.modelo.core.Joker j : javatro.modelo.CatalogoJokers.CATALOGO) {
            lista.add(new InfoJoker(
                    j.getNombre(),
                    j.getDescripcion(),
                    "/jokers/" + j.getUrlImagen(),
                    true));
        }

        return lista;
    }
}