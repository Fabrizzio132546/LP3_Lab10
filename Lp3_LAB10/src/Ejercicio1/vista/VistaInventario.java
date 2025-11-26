/*************************************************************************************
ARCHIVO	: VistaInventario.java
FECHA	: 25/11/2025
*************************************************************************************/

package Ejercicio1.vista;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.util.List;
import Ejercicio1.modelo.ParAsociado;

public class VistaInventario extends JFrame {

    private final Color COLOR_SIDEBAR = new Color(44, 62, 80);
    private final Color COLOR_HEADER = new Color(44, 62, 80);
    private final Color COLOR_FONDO_CENTRO = new Color(236, 240, 241);   

    private final Color COLOR_BTN_NUEVO = new Color(52, 152, 219);
    private final Color COLOR_BTN_TXT = new Color(39, 174, 96);
    private final Color COLOR_BTN_BD = new Color(142, 68, 173);
    private final Color COLOR_BTN_DANGER = new Color(231, 76, 60);
    private final Color COLOR_BTN_DARK_RED = new Color(192, 57, 43);
    private final Color COLOR_BTN_ORANGE = new Color(211, 84, 0);
    private final Color COLOR_BTN_GRAY = new Color(52, 73, 94);

    private JTextArea txtAreaListado;
    private PanelEstadisticas panelEstadisticas;
    private PanelGrafico panelGrafico;

    private JComboBox<String> cmbFiltroTipo;
    private JComboBox<String> cmbModo; 
    private JComboBox<String> cmbVista; 

    private JButton btnRefrescar, btnNuevo, btnModificar, btnBorrarMant, btnBorrarEquipo, btnVaciar;
    private JButton btnGuardarTXT, btnCargarTXT, btnGuardarBD, btnCargarBD;
    private JButton btnBorrarLocal, btnBorrarBDFisica, btnCerrarSesion;

    public VistaInventario() {
        configurarVentana();
        inicializarComponentes();
    }

    private void configurarVentana() {
        setTitle("SISTEMA DE INVENTARIO");
        setSize(1280, 760);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    private void inicializarComponentes() {
        construirHeader();
        construirSidebar();
        construirAreaCentral();
    }

    private void construirHeader() {
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(COLOR_HEADER);
        panelHeader.setBorder(new MatteBorder(0, 0, 1, 0, new Color(60, 80, 100)));
        panelHeader.setPreferredSize(new Dimension(0, 60));

        JLabel lblTitulo = new JLabel("  SISTEMA DE INVENTARIO");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        
        panelHeader.add(lblTitulo, BorderLayout.WEST);
        add(panelHeader, BorderLayout.NORTH);
    }

    private void construirSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(COLOR_SIDEBAR);
        sidebar.setBorder(new EmptyBorder(20, 15, 20, 15));
        sidebar.setPreferredSize(new Dimension(260, 0));

        sidebar.add(crearTituloSidebar("MENÚ PRINCIPAL"));
        sidebar.add(Box.createVerticalStrut(10));

        panelEstadisticas = new PanelEstadisticas();
        sidebar.add(panelEstadisticas);
        sidebar.add(Box.createVerticalStrut(20));

        btnRefrescar = crearBotonSidebar("Refrescar Datos", COLOR_BTN_GRAY);
        sidebar.add(btnRefrescar);
        sidebar.add(Box.createVerticalStrut(5));

        btnNuevo = crearBotonSidebar("Nuevo Registro", COLOR_BTN_NUEVO);
        sidebar.add(btnNuevo);
        sidebar.add(Box.createVerticalStrut(25));

        sidebar.add(crearTituloSidebar("GESTIÓN"));
        sidebar.add(Box.createVerticalStrut(10));

        btnModificar = crearBotonSidebar("Modificar Equipo", COLOR_BTN_GRAY);
        sidebar.add(btnModificar);
        sidebar.add(Box.createVerticalStrut(5));

        btnBorrarMant = crearBotonSidebar("Borrar Mantenimiento", COLOR_BTN_DANGER);
        sidebar.add(btnBorrarMant);
        sidebar.add(Box.createVerticalStrut(5));

        btnBorrarEquipo = crearBotonSidebar("Borrar Equipo (Total)", COLOR_BTN_DARK_RED);
        sidebar.add(btnBorrarEquipo);
        sidebar.add(Box.createVerticalStrut(5));

        btnVaciar = crearBotonSidebar("Vaciar Todo", COLOR_BTN_ORANGE);
        sidebar.add(btnVaciar);
        sidebar.add(Box.createVerticalStrut(25));

        sidebar.add(crearTituloSidebar("DATOS Y ARCHIVOS"));
        sidebar.add(Box.createVerticalStrut(10));

        JPanel panelTxt = new JPanel(new GridLayout(1, 2, 5, 0)); 
        panelTxt.setBackground(COLOR_SIDEBAR);
        panelTxt.setMaximumSize(new Dimension(260, 30)); 
        panelTxt.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        btnGuardarTXT = crearBotonPeque("Guardar TXT", COLOR_BTN_TXT);
        btnCargarTXT = crearBotonPeque("Cargar TXT", COLOR_BTN_TXT);
        panelTxt.add(btnGuardarTXT);
        panelTxt.add(btnCargarTXT);
        sidebar.add(panelTxt);
        sidebar.add(Box.createVerticalStrut(5));

        JPanel panelBd = new JPanel(new GridLayout(1, 2, 5, 0));
        panelBd.setBackground(COLOR_SIDEBAR);
        panelBd.setMaximumSize(new Dimension(260, 30)); 
        panelBd.setAlignmentX(Component.LEFT_ALIGNMENT);

        btnGuardarBD = crearBotonPeque("Guardar BD", COLOR_BTN_BD);
        btnCargarBD = crearBotonPeque("Cargar BD", COLOR_BTN_BD);
        panelBd.add(btnGuardarBD);
        panelBd.add(btnCargarBD);
        sidebar.add(panelBd);
        
        sidebar.add(Box.createVerticalStrut(15));

        btnBorrarLocal = crearBotonSidebar("Borrar Archivo Local", Color.GRAY);
        sidebar.add(btnBorrarLocal);
        sidebar.add(Box.createVerticalStrut(5));

        btnBorrarBDFisica = crearBotonSidebar("Eliminar BD Física", Color.GRAY);
        sidebar.add(btnBorrarBDFisica);
        
        sidebar.add(Box.createVerticalGlue()); 

        btnCerrarSesion = crearBotonSidebar("Cerrar Sesión", new Color(30, 30, 30));
        sidebar.add(btnCerrarSesion);

        add(sidebar, BorderLayout.WEST);
    }

    private void construirAreaCentral() {
        JPanel panelCentro = new JPanel(new BorderLayout());
        panelCentro.setBackground(COLOR_FONDO_CENTRO);
        panelCentro.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        toolbar.setBackground(Color.WHITE);
        toolbar.setBorder(new LineBorder(new Color(200, 200, 200), 1));

        toolbar.add(crearLabelToolbar("Modo:"));
        cmbModo = new JComboBox<>(new String[]{"Todo", "Solo Equipos", "Solo Asociaciones"});
        estilizarCombo(cmbModo);
        toolbar.add(cmbModo);

        toolbar.add(crearLabelToolbar("Vista:"));
        cmbVista = new JComboBox<>(new String[]{"Compacta", "Detallada", "Técnica"});
        estilizarCombo(cmbVista);
        toolbar.add(cmbVista);

        toolbar.add(Box.createHorizontalStrut(20));

        toolbar.add(crearLabelToolbar("Filtrar Tipo:"));
        cmbFiltroTipo = new JComboBox<>();
        cmbFiltroTipo.addItem("Todos");
        for(String t : Ejercicio1.modelo.Equipo.TIPOS) cmbFiltroTipo.addItem(t);
        estilizarCombo(cmbFiltroTipo);
        toolbar.add(cmbFiltroTipo);

        panelCentro.add(toolbar, BorderLayout.NORTH);

        txtAreaListado = new JTextArea();
        txtAreaListado.setEditable(false);
        txtAreaListado.setFont(new Font("Consolas", Font.PLAIN, 13));
        txtAreaListado.setForeground(new Color(50, 50, 50));
        txtAreaListado.setMargin(new Insets(15, 15, 15, 15));
        
        JScrollPane scroll = new JScrollPane(txtAreaListado);
        scroll.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        
        panelGrafico = new PanelGrafico();
        
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabs.addTab("Inventario General", scroll);
        tabs.addTab("Estadísticas Visuales", panelGrafico);
        
        JPanel contentTabs = new JPanel(new BorderLayout());
        contentTabs.setOpaque(false);
        contentTabs.setBorder(new EmptyBorder(10, 0, 0, 0));
        contentTabs.add(tabs, BorderLayout.CENTER);

        panelCentro.add(contentTabs, BorderLayout.CENTER);
        add(panelCentro, BorderLayout.CENTER);
    }


    private JLabel crearTituloSidebar(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lbl.setForeground(new Color(160, 170, 180));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT); 
        return lbl;
    }

    private JLabel crearLabelToolbar(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        return lbl;
    }

    private void estilizarCombo(JComboBox<?> cmb) {
        cmb.setBackground(Color.WHITE);
        cmb.setFocusable(false);
    }

    private JButton crearBotonSidebar(String texto, Color bg) {
        JButton btn = new JButton(texto);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT); 
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35)); 
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.CENTER); 
        return btn;
    }

    private JButton crearBotonPeque(String texto, Color bg) {
        JButton btn = new JButton(texto);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 10));
        btn.setFocusPainted(false);
        btn.setBorder(null);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public void actualizarListado(List<ParAsociado> lista, String tipoVista) {
        StringBuilder sb = new StringBuilder();
        if (lista.isEmpty()) {
            sb.append("\n  [INFO] No hay registros para mostrar.");
        } else {
            for (ParAsociado p : lista) {
                switch (tipoVista) {
                    case "Compacta":
                        sb.append(formatoCompacto(p)).append("\n");
                        break;
                    case "Detallada":
                        sb.append(formatoDetallado(p)).append("\n");
                        break;
                    case "Técnica":
                        sb.append(formatoTecnico(p)).append("\n");
                        break;
                    default: 
                        sb.append(formatoCompacto(p)).append("\n");
                }
            }
        }
        txtAreaListado.setText(sb.toString());
        txtAreaListado.setCaretPosition(0);
    }

    private String formatoCompacto(ParAsociado p) {
        String est = (p.getMantenimiento().getId() == -1) ? "[NUEVO]" : "[SERV]";
        return String.format("%-7s | %-20s (%s) | %s", 
            est, p.getEquipo().getNombre(), p.getEquipo().getTipo(), 
            (p.getMantenimiento().getId() == -1) ? "Sin Mantenimiento" : p.getMantenimiento().getDescripcion());
    }

    private String formatoDetallado(ParAsociado p) {
        StringBuilder sb = new StringBuilder();
        sb.append("----------------------------------------------------------\n");
        sb.append(" EQUIPO: ").append(p.getEquipo().getNombre()).append(" (ID: ").append(p.getEquipo().getId()).append(")\n");
        sb.append(" TIPO:   ").append(p.getEquipo().getTipo()).append("\n");
        if(p.getMantenimiento().getId() != -1) {
            sb.append(" MANTENIMIENTO: ").append(p.getMantenimiento().getDescripcion()).append("\n");
            sb.append(" TÉCNICO:       ").append(p.getMantenimiento().getTecnico()).append("\n");
            sb.append(" FECHA:         ").append(p.getMantenimiento().getFecha()).append("\n");
            sb.append(" COSTO:         S/ ").append(p.getMantenimiento().getCosto()).append("\n");
        } else {
            sb.append(" ESTADO:        Pendiente de servicio.\n");
        }
        return sb.toString();
    }

    private String formatoTecnico(ParAsociado p) {
        return String.format("ID_EQ:%d::%s::DESC_EQ:[%s] -> ID_MAN:%d::TEC:[%s]",
            p.getEquipo().getId(), p.getEquipo().getTipo().toUpperCase(), p.getEquipo().getDescripcionTecnica(),
            p.getMantenimiento().getId(), p.getMantenimiento().getTecnico());
    }
    
    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void mostrarAlerta(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Atención", JOptionPane.WARNING_MESSAGE);
    }

    public boolean pedirConfirmacion(String mensaje) {
        int r = JOptionPane.showConfirmDialog(this, mensaje, "Confirmar", JOptionPane.YES_NO_OPTION);
        return r == JOptionPane.YES_OPTION;
    }

    public String pedirEntrada(String mensaje) {
        return JOptionPane.showInputDialog(this, mensaje);
    }

    public PanelEstadisticas getPanelEstadisticas() { return panelEstadisticas; }
    public JComboBox<String> getCmbFiltroTipo() { return cmbFiltroTipo; }
    public JComboBox<String> getCmbModo() { return cmbModo; }
    public JComboBox<String> getCmbVista() { return cmbVista; }
    public PanelGrafico getPanelGrafico() { return panelGrafico; }
    
    public JButton getBtnRefrescar() { return btnRefrescar; }
    public JButton getBtnNuevo() { return btnNuevo; }
    public JButton getBtnModificar() { return btnModificar; }
    public JButton getBtnBorrarMant() { return btnBorrarMant; }
    public JButton getBtnBorrarEquipo() { return btnBorrarEquipo; }
    public JButton getBtnVaciar() { return btnVaciar; }
    public JButton getBtnGuardarTXT() { return btnGuardarTXT; }
    public JButton getBtnCargarTXT() { return btnCargarTXT; }
    public JButton getBtnGuardarBD() { return btnGuardarBD; }
    public JButton getBtnCargarBD() { return btnCargarBD; }
    public JButton getBtnBorrarLocal() { return btnBorrarLocal; }
    public JButton getBtnBorrarBDFisica() { return btnBorrarBDFisica; }
    public JButton getBtnCerrarSesion() { return btnCerrarSesion; }
}