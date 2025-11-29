package Actividad2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class RegistroTemperaturasApp {
  static class GraphPanel extends JPanel {
        private double[] temperatures = new double[7];
        private boolean hasData = false;
        private final String[] days = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};

        public void setTemperatures(double[] temps) {
            System.arraycopy(temps, 0, this.temperatures, 0, 7);
            this.hasData = true;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (!hasData) return;

            int width = getWidth();
            int height = getHeight();
            int margin = 50;
            int graphWidth = width - 2 * margin;
            int graphHeight = height - 2 * margin;

            // Encontrar min y max
            double minTemp = Double.MAX_VALUE;
            double maxTemp = -Double.MAX_VALUE;
            for (double t : temperatures) {
                minTemp = Math.min(minTemp, t);
                maxTemp = Math.max(maxTemp, t);
            }
            if (minTemp == maxTemp) {
                minTemp -= 5;
                maxTemp += 5;
            }
            double tempRange = maxTemp - minTemp;

            // Dibujar ejes
            g.drawLine(margin, height - margin, width - margin, height - margin); // Eje X
            g.drawLine(margin, margin, margin, height - margin); // Eje Y

            // Etiquetas días (eje X)
            int xStep = graphWidth / 6;
            for (int i = 0; i < 7; i++) {
                int x = margin + i * xStep;
                g.drawString(days[i].substring(0, 3), x - 10, height - margin + 20);
            }

            // Etiquetas temperaturas (eje Y)
            int ySteps = 5;
            double yStepValue = tempRange / ySteps;
            for (int i = 0; i <= ySteps; i++) {
                double val = minTemp + i * yStepValue;
                int y = height - margin - (int) (i * (graphHeight / (double) ySteps));
                g.drawString(String.format("%.1f", val), margin - 40, y + 5);
            }

            // Puntos y línea
            int[] xPoints = new int[7];
            int[] yPoints = new int[7];
            for (int i = 0; i < 7; i++) {
                xPoints[i] = margin + i * xStep;
                double normalized = (temperatures[i] - minTemp) / tempRange;
                yPoints[i] = height - margin - (int) (normalized * graphHeight);
            }

            g.setColor(Color.BLUE);
            g.drawPolyline(xPoints, yPoints, 7);

            g.setColor(Color.RED);
            for (int i = 0; i < 7; i++) {
                g.fillOval(xPoints[i] - 3, yPoints[i] - 3, 6, 6);
            }
        }
    }

}

