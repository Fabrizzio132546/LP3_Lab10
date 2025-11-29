package javatro.vista.ventanas;

import javatro.datos.GestorHistorial;
import javatro.datos.RegistroPartida;
import javatro.vista.componentes.BotonBalatro;
import javatro.vista.componentes.FondoColeccion;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VentanaHistorial extends JDialog {

    public VentanaHistorial(JFrame owner) {
        super(owner, "Historial de Partidas", true);
        setSize(800, 500);
        setLocationRelativeTo(owner);
        setUndecorated(true);

        initUI();
    }

    private void initUI() {
        FondoColeccion fondo = new FondoColeccion();
        fondo.setLayout(new BorderLayout());
        setContentPane(fondo);

        // Título
        JLabel lblTitulo = new JLabel("HISTORIAL DE JUEGO");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 30));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        fondo.add(lblTitulo, BorderLayout.NORTH);

        // Tabla
        String[] columnas = {"Fecha", "Resultado", "Ronda", "Puntaje", "Dinero"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        // Cargar datos
        List<RegistroPartida> lista = GestorHistorial.cargarHistorial();
        for (RegistroPartida p : lista) {
            modeloTabla.addRow(new Object[]{
                p.getFecha(), p.getResultado(), "Ronda " + p.getRonda(), 
                p.getPuntaje(), "$" + p.getDinero()
            });
        }

        JTable tabla = new JTable(modeloTabla);
        estilizarTabla(tabla);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));
        
        fondo.add(scroll, BorderLayout.CENTER);

        // Botón Volver
        JPanel panelSur = new JPanel();
        panelSur.setOpaque(false);
        panelSur.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        BotonBalatro btnVolver = new BotonBalatro("VOLVER", new Color(220, 60, 60));
        btnVolver.addActionListener(e -> dispose());
        panelSur.add(btnVolver);
        
        fondo.add(panelSur, BorderLayout.SOUTH);
    }

    private void estilizarTabla(JTable tabla) {
        tabla.setRowHeight(30);
        tabla.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // filas oscurasa
        tabla.setBackground(new Color(60, 60, 65));
        tabla.setForeground(Color.WHITE);
        tabla.setGridColor(new Color(100, 100, 100));

        javax.swing.table.JTableHeader header = tabla.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 16));
        header.setBackground(new Color(30, 30, 30)); 
        header.setForeground(Color.ORANGE);
        header.setOpaque(true); 
        
        // lo que es el texto centrado
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tabla.getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        tabla.setFillsViewportHeight(true);
    }
}