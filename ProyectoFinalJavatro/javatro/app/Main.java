package javatro.app;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javatro.vista.ventanas.MenuPrincipal;
import javatro.datos.GestorHistorial;

public class Main {
    public static void main(String[] args) {
    	
    	GestorHistorial.inicializarBaseDeDatos();
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
            }

            MenuPrincipal menu = new MenuPrincipal();

            javatro.util.GestorAudio.getInstancia().reproducirMusica("musica1.wav");

            menu.setVisible(true);
        });
    }
}