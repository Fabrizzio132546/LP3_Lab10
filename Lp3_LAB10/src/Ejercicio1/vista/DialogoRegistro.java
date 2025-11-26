/*************************************************************************************
ARCHIVO	: DialogoRegistro.java
FECHA	: 25/11/2025
*************************************************************************************/

package Ejercicio1.vista;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class DialogoRegistro extends JDialog {

    private JTextField txtIdEquipo, txtNombre, txtDescTecnica;
    private JComboBox<String> cmbTipo;
    private JButton btnNuevoEq, btnBuscarEq;

    private JPanel panelMant; 
    private JTextField txtIdMant, txtDescMant, txtTecnico, txtFecha, txtCosto;

    private JButton btnGuardarSoloEquipo, btnAgregarMant, btnGuardarTodo, btnCancelar;

    private boolean confirmado = false; 
    private boolean soloEquipo = false;

    public DialogoRegistro(JFrame parent) {
        super(parent, "Registro de Inventario", true);
        setSize(500, 380);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        setResizable(false);
        
        construirFormulario();
    }

    private void construirFormulario() {
        JPanel panelContenido = new JPanel();
        panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));
        panelContenido.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JPanel panelEquipo = new JPanel(new GridLayout(0, 2, 5, 5));
        panelEquipo.setBorder(BorderFactory.createTitledBorder("Paso 1: Datos del Equipo"));
        panelEquipo.setBackground(new Color(245, 250, 255));

        JPanel panelId = new JPanel(new BorderLayout(5, 0));
        panelId.setOpaque(false);
        
        txtIdEquipo = new JTextField();
        txtIdEquipo.setEditable(true); 
        txtIdEquipo.setBackground(Color.WHITE); 
        
        JPanel panelBtnsId = new JPanel(new GridLayout(1, 2, 2, 0));
        
        btnNuevoEq = new JButton("Nuevo");
        btnNuevoEq.setBackground(new Color(52, 152, 219)); 
        btnNuevoEq.setForeground(Color.WHITE);
        btnNuevoEq.setFocusPainted(false);
        
        btnBuscarEq = new JButton("Buscar");
        btnBuscarEq.setBackground(Color.LIGHT_GRAY);
        btnBuscarEq.setFocusPainted(false);
        
        panelBtnsId.add(btnNuevoEq); 
        panelBtnsId.add(btnBuscarEq);
        
        panelId.add(txtIdEquipo, BorderLayout.CENTER);
        panelId.add(panelBtnsId, BorderLayout.EAST);

        panelEquipo.add(new JLabel("ID Equipo:")); panelEquipo.add(panelId);
        panelEquipo.add(new JLabel("Nombre:")); txtNombre = new JTextField(); panelEquipo.add(txtNombre);
        panelEquipo.add(new JLabel("Tipo:")); 
        cmbTipo = new JComboBox<>(Ejercicio1.modelo.Equipo.TIPOS); 
        cmbTipo.setBackground(Color.WHITE);
        panelEquipo.add(cmbTipo);
        panelEquipo.add(new JLabel("Desc. Técnica:")); txtDescTecnica = new JTextField(); panelEquipo.add(txtDescTecnica);
        
        panelContenido.add(panelEquipo);
        panelContenido.add(Box.createVerticalStrut(10));

        panelMant = new JPanel(new GridLayout(0, 2, 5, 5));
        panelMant.setBorder(BorderFactory.createTitledBorder("Paso 2: Datos del Mantenimiento"));
        panelMant.setVisible(false); 
        
        panelMant.add(new JLabel("ID Mant.:")); 
        txtIdMant = new JTextField(); 
        txtIdMant.setEditable(false);
        txtIdMant.setBackground(new Color(230, 230, 230));
        panelMant.add(txtIdMant);
        
        panelMant.add(new JLabel("Descripción:")); txtDescMant = new JTextField(); panelMant.add(txtDescMant);
        panelMant.add(new JLabel("Técnico:")); txtTecnico = new JTextField(); panelMant.add(txtTecnico);
        panelMant.add(new JLabel("Fecha:")); txtFecha = new JTextField(LocalDate.now().toString()); panelMant.add(txtFecha);
        panelMant.add(new JLabel("Costo:")); txtCosto = new JTextField("0.0"); panelMant.add(txtCosto);

        panelContenido.add(panelMant);
        add(panelContenido, BorderLayout.CENTER);

        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnCancelar = new JButton("Cancelar");
        btnGuardarSoloEquipo = new JButton("Guardar y Cerrar");
        btnGuardarSoloEquipo.setBackground(new Color(39, 174, 96));
        btnGuardarSoloEquipo.setForeground(Color.WHITE);
        
        btnAgregarMant = new JButton("Continuar a Mantenimiento ▼");
        btnAgregarMant.setBackground(new Color(41, 128, 185));
        btnAgregarMant.setForeground(Color.WHITE);
        
        btnGuardarTodo = new JButton("Guardar Todo");
        btnGuardarTodo.setBackground(new Color(142, 68, 173));
        btnGuardarTodo.setForeground(Color.WHITE);
        btnGuardarTodo.setVisible(false);

        btnAgregarMant.addActionListener(e -> mostrarPanelMantenimiento());
        btnCancelar.addActionListener(e -> dispose());

        panelSur.add(btnCancelar);
        panelSur.add(btnGuardarSoloEquipo);
        panelSur.add(btnAgregarMant);
        panelSur.add(btnGuardarTodo);
        
        add(panelSur, BorderLayout.SOUTH);
    }

    public void activarModoNuevo(String idGenerado) {
        txtIdEquipo.setText(idGenerado);
        txtIdEquipo.setEditable(false);
        txtIdEquipo.setBackground(new Color(230, 230, 230)); 
        txtNombre.setText("");
        txtDescTecnica.setText("");
        habilitarCamposEquipo(true);
    }

    public void activarModoBusqueda() {
        txtIdEquipo.setText("");
        txtIdEquipo.setEditable(true); 
        txtIdEquipo.setBackground(Color.WHITE);
        txtIdEquipo.requestFocus();
        txtNombre.setText("");
        txtDescTecnica.setText("");
    }

    public boolean isIdEditable() {
        return txtIdEquipo.isEditable();
    }

    public void mostrarPanelMantenimiento() {
        panelMant.setVisible(true);
        btnGuardarSoloEquipo.setVisible(false);
        btnAgregarMant.setVisible(false);
        btnGuardarTodo.setVisible(true);
        setSize(500, 580);
    }

    public void cerrarConConfirmacion(boolean soloEq) {
        this.confirmado = true;
        this.soloEquipo = soloEq;
        this.setVisible(false);
    }

    public boolean isConfirmado() { return confirmado; }
    public boolean isSoloEquipo() { return soloEquipo; }
    public JButton getBtnNuevoEq() { return btnNuevoEq; }
    public JButton getBtnBuscarEq() { return btnBuscarEq; }
    public JButton getBtnGuardarSoloEquipo() { return btnGuardarSoloEquipo; }
    public JButton getBtnGuardarTodo() { return btnGuardarTodo; }

    public String getIdEquipoStr() { return txtIdEquipo.getText(); }
    public String getNombre() { return txtNombre.getText(); }
    public void setTxtNombre(String t) { txtNombre.setText(t); }
    public String getTipo() { return (String) cmbTipo.getSelectedItem(); }
    public void setCmbTipo(String t) { cmbTipo.setSelectedItem(t); }
    public String getDescEq() { return txtDescTecnica.getText(); }
    public void setTxtDescEq(String t) { txtDescTecnica.setText(t); }
    public void habilitarCamposEquipo(boolean b) {
        txtNombre.setEditable(b);
        cmbTipo.setEnabled(b);
        txtDescTecnica.setEditable(b);
    }

    public String getIdMantStr() { return txtIdMant.getText(); }
    public void setTxtIdMant(String t) { txtIdMant.setText(t); }
    public String getDescMant() { return txtDescMant.getText(); }
    public String getTecnico() { return txtTecnico.getText(); }
    public String getFecha() { return txtFecha.getText(); }
    public double getCosto() { return Double.parseDouble(txtCosto.getText()); }
}