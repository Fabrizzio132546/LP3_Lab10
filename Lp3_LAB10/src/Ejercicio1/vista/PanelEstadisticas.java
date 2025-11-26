/*************************************************************************************
ARCHIVO	: PanelEstadisticas.java
FECHA	: 25/11/2025
*************************************************************************************/

package Ejercicio1.vista;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;

public class PanelEstadisticas extends JPanel {

    private JLabel lblTotalEquipos;
    private JLabel lblTotalDinero;

    private final Color COLOR_ACCENT_BLUE = new Color(52, 152, 219); 
    private final Color COLOR_ACCENT_GREEN = new Color(39, 174, 96); 

    public PanelEstadisticas() {
        setLayout(new GridLayout(2, 1, 0, 10));
        setOpaque(false); 

        setAlignmentX(Component.LEFT_ALIGNMENT);
        setMaximumSize(new Dimension(260, 110)); 
        setPreferredSize(new Dimension(260, 110));

        lblTotalEquipos = new JLabel("9");
        JPanel cardEquipos = crearTarjeta("EQUIPOS", lblTotalEquipos, COLOR_ACCENT_BLUE);

        lblTotalDinero = new JLabel("S/ 1.890,00");
        JPanel cardDinero = crearTarjeta("INVERSIÓN", lblTotalDinero, COLOR_ACCENT_GREEN);

        add(cardEquipos);
        add(cardDinero);
    }

    private JPanel crearTarjeta(String titulo, JLabel lblValor, Color colorBorde) {
        JPanel tarjeta = new JPanel(new BorderLayout(5, 0));
        tarjeta.setBackground(Color.WHITE);

        tarjeta.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(0, 5, 0, 0, colorBorde),
            BorderFactory.createEmptyBorder(5, 10, 5, 10) 
        ));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblTitulo.setForeground(Color.GRAY);

        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblValor.setForeground(new Color(44, 62, 80));

        JPanel contenido = new JPanel(new GridLayout(2, 1));
        contenido.setOpaque(false);
        contenido.add(lblTitulo);
        contenido.add(lblValor);

        tarjeta.add(contenido, BorderLayout.CENTER);
        
        return tarjeta;
    }

    public void actualizarValores(int cantidad, double dinero) {
        lblTotalEquipos.setText(String.valueOf(cantidad));
        lblTotalDinero.setText(String.format("S/ %,.2f", dinero));
    }
}