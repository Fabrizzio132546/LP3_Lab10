/*************************************************************************************
ARCHIVO	: PanelGrafico.java
FECHA	: 25/11/2025
*************************************************************************************/
package Ejercicio1.vista;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class PanelGrafico extends JPanel {

    private Map<String, Integer> datos;

    public PanelGrafico() {
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
    }

    public void setDatos(Map<String, Integer> datos) {
        this.datos = datos;
        repaint(); 
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (datos == null || datos.isEmpty()) {
            g.drawString("No hay datos para graficar.", 50, 50);
            return;
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int anchoPanel = getWidth();
        int altoPanel = getHeight();
        int margen = 50;
        int maxValor = datos.values().stream().max(Integer::compare).orElse(1);

        g2d.drawLine(margen, altoPanel - margen, anchoPanel - margen, altoPanel - margen);
        g2d.drawLine(margen, altoPanel - margen, margen, margen); 

        int numBarras = datos.size();
        int anchoBarra = (anchoPanel - 2 * margen) / numBarras;
        int x = margen + 10;

        Color[] colores = {new Color(52, 152, 219), new Color(46, 204, 113), new Color(155, 89, 182), new Color(241, 196, 15)};
        int colorIdx = 0;

        for (Map.Entry<String, Integer> entrada : datos.entrySet()) {
            String tipo = entrada.getKey();
            int cantidad = entrada.getValue();

            int alturaBarra = (int) (((double) cantidad / maxValor) * (altoPanel - 2 * margen));

            g2d.setColor(colores[colorIdx % colores.length]);
            g2d.fillRect(x, altoPanel - margen - alturaBarra, anchoBarra - 20, alturaBarra);
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.BOLD, 12));
            g2d.drawString(String.valueOf(cantidad), x + (anchoBarra/4), altoPanel - margen - alturaBarra - 5);
            g2d.setFont(new Font("Arial", Font.PLAIN, 10));
            String label = tipo.length() > 10 ? tipo.substring(0, 8)+".." : tipo;
            g2d.drawString(label, x, altoPanel - margen + 15);

            x += anchoBarra;
            colorIdx++;
        }
    }
}