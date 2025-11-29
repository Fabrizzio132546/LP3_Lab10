package Actividad2;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Actividad2.RegistroTemperaturasApp.GraphPanel;

public class Main {
  public static void main(String[] args) {
        JFrame frame = new JFrame("Registro de Temperaturas");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(8, 2, 10, 10));
        String[] days = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};
        JTextField[] tempFields = new JTextField[7];

        for (int i = 0; i < 7; i++) {
            inputPanel.add(new JLabel(days[i] + ":"));
            tempFields[i] = new JTextField();
            inputPanel.add(tempFields[i]);
        }

        JButton btnMostrar = new JButton("Mostrar Gráfico");
        inputPanel.add(new JLabel());
        inputPanel.add(btnMostrar);

        GraphPanel graphPanel = new GraphPanel();

        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(graphPanel, BorderLayout.CENTER);

        btnMostrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double[] temps = new double[7];
                    for (int i = 0; i < 7; i++) {
                        String text = tempFields[i].getText();
                        if (text.isEmpty()) {
                            throw new NumberFormatException("Campo vacío");
                        }
                        temps[i] = Double.parseDouble(text);
                    }
                    graphPanel.setTemperatures(temps);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Ingrese temperaturas válidas en todos los campos.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        frame.setVisible(true);
    }
}


