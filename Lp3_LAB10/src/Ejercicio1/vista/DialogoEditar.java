/*************************************************************************************
ARCHIVO	: DialogoEditar.java
FECHA	: 25/11/2025
*************************************************************************************/

package Ejercicio1.vista;

import javax.swing.*;
import java.awt.*;

public class DialogoEditar extends JDialog {

    private JTextField txtIdBusqueda, txtNombre, txtDesc;
    private JComboBox<String> cmbTipo;
    private JButton btnBuscar, btnGuardar, btnCancelar;
    
    private boolean confirmado = false;

    public DialogoEditar(JFrame parent) {
        super(parent, "Modificar Datos de Equipo", true);
        setSize(400, 350);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        setResizable(false);

        construirInterfaz();
    }

    private void construirInterfaz() {
        JPanel panelForm = new JPanel(new GridLayout(0, 2, 10, 15));
        panelForm.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelForm.setBackground(new Color(245, 250, 255)); 

        panelForm.add(new JLabel("ID a Modificar:"));
        JPanel panelId = new JPanel(new BorderLayout(5, 0));
        panelId.setOpaque(false);
        
        txtIdBusqueda = new JTextField();
        btnBuscar = new JButton("Buscar");
        btnBuscar.setBackground(Color.LIGHT_GRAY);
        btnBuscar.setFocusPainted(false);
        
        panelId.add(txtIdBusqueda, BorderLayout.CENTER);
        panelId.add(btnBuscar, BorderLayout.EAST);
        panelForm.add(panelId);

        panelForm.add(new JLabel("Nombre:")); 
        txtNombre = new JTextField(); 
        panelForm.add(txtNombre);

        panelForm.add(new JLabel("Tipo:"));
        cmbTipo = new JComboBox<>(Ejercicio1.modelo.Equipo.TIPOS);
        cmbTipo.setBackground(Color.WHITE);
        panelForm.add(cmbTipo);

        panelForm.add(new JLabel("Descripción:")); 
        txtDesc = new JTextField(); 
        panelForm.add(txtDesc);

        add(panelForm, BorderLayout.CENTER);

        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelSur.setBackground(Color.WHITE);

        btnGuardar = new JButton("Guardar Cambios");
        btnGuardar.setBackground(new Color(39, 174, 96));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setEnabled(false); 

        btnCancelar = new JButton("Cancelar");

        btnGuardar.addActionListener(e -> {
            confirmado = true;
            setVisible(false);
        });
        btnCancelar.addActionListener(e -> dispose());

        panelSur.add(btnCancelar);
        panelSur.add(btnGuardar);
        add(panelSur, BorderLayout.SOUTH);

        habilitarEdicion(false);
    }

    public void habilitarEdicion(boolean habilitar) {
        txtNombre.setEditable(habilitar);
        cmbTipo.setEnabled(habilitar);
        txtDesc.setEditable(habilitar);
        btnGuardar.setEnabled(habilitar);

        txtIdBusqueda.setEditable(!habilitar);
        btnBuscar.setEnabled(!habilitar);
    }
    
    public boolean isConfirmado() { return confirmado; }
    public JButton getBtnBuscar() { return btnBuscar; }
    public String getIdBusqueda() { return txtIdBusqueda.getText(); }

    public void setNombre(String t) { txtNombre.setText(t); }
    public void setTipo(String t) { cmbTipo.setSelectedItem(t); }
    public void setDesc(String t) { txtDesc.setText(t); }

    public String getNombre() { return txtNombre.getText(); }
    public String getTipo() { return (String) cmbTipo.getSelectedItem(); }
    public String getDesc() { return txtDesc.getText(); }
}