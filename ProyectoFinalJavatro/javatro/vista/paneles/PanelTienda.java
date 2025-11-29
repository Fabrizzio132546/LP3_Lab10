package javatro.vista.paneles;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.net.URL;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import javatro.controlador.ControladorJuego;
import javatro.modelo.tienda.ItemTienda;
import javatro.vista.componentes.BotonBalatro;

public class PanelTienda extends JPanel {

    private ControladorJuego controlador;
    private JLabel lblDinero;
    private JPanel panelItems;

    public PanelTienda(ControladorJuego controlador) {
        this.controlador = controlador;
        this.setLayout(new BorderLayout());
        this.setBackground(new Color(35, 39, 45));
        inicializarUI();
    }

    private void inicializarUI() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(30, 50, 20, 50));

        JLabel lblTitulo = new JLabel("TIENDA");
        lblTitulo.setFont(new Font("Monospaced", Font.BOLD, 50));
        lblTitulo.setForeground(Color.WHITE);

        lblDinero = new JLabel("$ 0");
        lblDinero.setFont(new Font("Monospaced", Font.BOLD, 40));
        lblDinero.setForeground(new Color(255, 215, 0));

        header.add(lblTitulo, BorderLayout.WEST);
        header.add(lblDinero, BorderLayout.EAST);
        this.add(header, BorderLayout.NORTH);

        panelItems = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 20));
        panelItems.setOpaque(false);
        panelItems.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20),
                BorderFactory.createDashedBorder(Color.GRAY, 2, 5)));

        this.add(panelItems, BorderLayout.CENTER);

        JPanel panelDerecha = crearPanelSlots();
        this.add(panelDerecha, BorderLayout.EAST);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 30));
        footer.setOpaque(false);
        footer.setPreferredSize(new Dimension(0, 150));

        BotonBalatro btnReroll = new BotonBalatro("REROLL ($5)", new Color(220, 60, 60));
        btnReroll.addActionListener(e -> controlador.solicitarReroll());

        BotonBalatro btnSiguiente = new BotonBalatro("SIGUIENTE RONDA", new Color(0, 120, 255));
        btnSiguiente.setPreferredSize(new Dimension(300, 60));
        btnSiguiente.addActionListener(e -> controlador.cerrarTienda());

        footer.add(btnReroll);
        footer.add(btnSiguiente);
        this.add(footer, BorderLayout.SOUTH);
    }

    private JPanel crearPanelSlots() {
        JPanel panelSlots = new JPanel();
        panelSlots.setLayout(new BoxLayout(panelSlots, BoxLayout.Y_AXIS));
        panelSlots.setOpaque(false);
        panelSlots.setBorder(BorderFactory.createEmptyBorder(50, 10, 50, 40));
        panelSlots.setPreferredSize(new Dimension(220, 0));

        JLabel lblMini = new JLabel("CASINO");
        lblMini.setFont(new Font("Monospaced", Font.BOLD, 24));
        lblMini.setForeground(new Color(255, 100, 100));
        lblMini.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Pantalla corregida con tamaño fijo más grande y centrada
        JLabel lblPantalla = new JLabel("[ 7 - 7 - 7 ]");
        lblPantalla.setFont(new Font("Monospaced", Font.BOLD, 20));
        lblPantalla.setForeground(Color.YELLOW);
        lblPantalla.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblPantalla.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 2));
        lblPantalla.setPreferredSize(new Dimension(180, 40));
        lblPantalla.setMaximumSize(new Dimension(180, 40));
        lblPantalla.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel lblResultado = new JLabel("Costo: $5");
        lblResultado.setFont(new Font("Arial", Font.BOLD, 14));
        lblResultado.setForeground(Color.WHITE);
        lblResultado.setAlignmentX(Component.CENTER_ALIGNMENT);

        BotonBalatro btnSpin = new BotonBalatro("GIRAR", new Color(150, 50, 200));
        btnSpin.setPreferredSize(new Dimension(120, 50));
        btnSpin.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnSpin.addActionListener(e -> {
            if (controlador.getDinero() < 5) {
                lblResultado.setText("¡Falta dinero!");
                lblResultado.setForeground(Color.RED);
                return;
            }

            btnSpin.setEnabled(false);
            lblResultado.setText("Girando...");
            lblResultado.setForeground(Color.CYAN);

            javax.swing.Timer timerAnim = new javax.swing.Timer(50, null);
            final int[] count = { 0 };

            String[] simbolos = { "7", "J", "Q", "K", "A", "$", "O" };

            timerAnim.addActionListener(evt -> {
                String s1 = simbolos[(int) (Math.random() * simbolos.length)];
                String s2 = simbolos[(int) (Math.random() * simbolos.length)];
                String s3 = simbolos[(int) (Math.random() * simbolos.length)];
                lblPantalla.setText("[ " + s1 + " - " + s2 + " - " + s3 + " ]");

                count[0]++;
                if (count[0] > 20) {
                    timerAnim.stop();

                    int resultado = controlador.jugarSlots(5);
                    btnSpin.setEnabled(true);
                    actualizarDinero();

                    if (resultado == 2) {
                        lblPantalla.setText("[ 7 - 7 - 7 ]");
                        lblResultado.setText("¡JACKPOT! +$50");
                        lblResultado.setForeground(Color.GREEN);
                        javatro.util.GestorAudio.getInstancia().reproducirEfecto("click.wav");
                    } else if (resultado == 1) {
                        lblPantalla.setText("[ J - J - K ]");
                        lblResultado.setText("¡GANASTE! +$10");
                        lblResultado.setForeground(Color.GREEN);
                    } else {
                        lblPantalla.setText("[ J - 7 - Q ]");
                        lblResultado.setText("Perdiste...");
                        lblResultado.setForeground(Color.LIGHT_GRAY);
                    }
                }
            });
            timerAnim.start();
        });

        // --- BOTÓN CARTA MISTERIOSA ---
        BotonBalatro btnMystery = new BotonBalatro("CARTA EXTRA ($4)", new Color(100, 100, 255));
        btnMystery.setPreferredSize(new Dimension(180, 50));
        btnMystery.setFont(new Font("Monospaced", Font.BOLD, 14));
        btnMystery.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnMystery.addActionListener(e -> controlador.comprarCartaMisteriosa());

        panelSlots.add(lblMini);
        panelSlots.add(Box.createRigidArea(new Dimension(0, 15)));
        panelSlots.add(lblPantalla);
        panelSlots.add(Box.createRigidArea(new Dimension(0, 10)));
        panelSlots.add(lblResultado);
        panelSlots.add(Box.createRigidArea(new Dimension(0, 15)));
        panelSlots.add(btnSpin);
        panelSlots.add(Box.createRigidArea(new Dimension(0, 40))); // Separador
        panelSlots.add(btnMystery);

        return panelSlots;
    }

    public void abrirTienda() {
        actualizarDinero();
        this.setVisible(true);
    }

    public void actualizarDinero() {
        if (controlador != null) {
            lblDinero.setText("$ " + controlador.getDinero());
        }
    }

    public void mostrarItems(List<ItemTienda> items) {
        panelItems.removeAll();

        for (int i = 0; i < items.size(); i++) {
            ItemTienda item = items.get(i);
            final int index = i;

            JPanel panelCarta = crearItemVisual(item, e -> controlador.intentarComprar(index));
            panelItems.add(panelCarta);
        }
        panelItems.revalidate();
        panelItems.repaint();
    }

    private JPanel crearItemVisual(ItemTienda item, java.awt.event.ActionListener accionCompra) {
        JPanel panelCarta = new JPanel();
        panelCarta.setLayout(new BoxLayout(panelCarta, BoxLayout.Y_AXIS));
        panelCarta.setOpaque(false);
        panelCarta.setPreferredSize(new Dimension(160, 260));
        panelCarta.setMaximumSize(new Dimension(160, 260));
        panelCarta.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60), 2));

        JLabel lblImg = new JLabel();
        lblImg.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblImg.setHorizontalAlignment(SwingConstants.CENTER);
        lblImg.setPreferredSize(new Dimension(100, 140));
        lblImg.setMaximumSize(new Dimension(100, 140));

        try {
            // Aseguramos ruta correcta
            String rutaCompleta = "/javatro/recursos/imagenes" + item.getRutaImagen();
            URL url = getClass().getResource(rutaCompleta);

            if (url != null) {
                ImageIcon icon = new ImageIcon(javax.imageio.ImageIO.read(url));
                Image img = icon.getImage().getScaledInstance(100, 140, Image.SCALE_SMOOTH);
                lblImg.setIcon(new ImageIcon(img));
            } else {
                lblImg.setText("<html><center>" + item.getNombre() + "</center></html>");
                lblImg.setForeground(Color.WHITE);
                lblImg.setFont(new Font("Arial", Font.BOLD, 14));
            }
        } catch (Exception e) {
            System.err.println(" Error cargando imagen: " + e.getMessage());
            lblImg.setText("?");
        }

        String tooltipHTML = "<html><div style='text-align:center;'><b>" + item.getNombre() + "</b><br>"
                + item.getDescripcion() + "</div></html>";
        panelCarta.setToolTipText(tooltipHTML);
        lblImg.setToolTipText(tooltipHTML);

        BotonBalatro btnComprar = new BotonBalatro("$" + item.getPrecio(), new Color(0, 180, 80));
        btnComprar.setFont(new Font("Monospaced", Font.BOLD, 18));
        btnComprar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnComprar.setPreferredSize(new Dimension(140, 45));
        btnComprar.setMaximumSize(new Dimension(140, 45));
        btnComprar.addActionListener(accionCompra);

        panelCarta.add(Box.createRigidArea(new Dimension(0, 15)));
        panelCarta.add(lblImg);
        panelCarta.add(Box.createVerticalGlue());
        panelCarta.add(btnComprar);
        panelCarta.add(Box.createRigidArea(new Dimension(0, 15)));

        panelCarta.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                panelCarta.setBackground(new Color(255, 255, 255, 20));
                panelCarta.setOpaque(true);
                panelCarta.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
                panelCarta.repaint();
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                panelCarta.setOpaque(false);
                panelCarta.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60), 2));
                panelCarta.repaint();
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        return panelCarta;
    }
}