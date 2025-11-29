package laboratorio10.view;

import javax.swing.*;

public class VistaProducto extends JFrame {

    public JTextField txtNombre;
    public JTextField txtPrecio;
    public JTextField txtStock;
    public JTextField txtCategoria;

    public JButton btnActualizar;

    public JLabel lblResultado;

    public VistaProducto() {

        setTitle("gestion de producto");
        setSize(350, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new java.awt.FlowLayout());

        txtNombre = new JTextField(15);
        txtPrecio = new JTextField(10);
        txtStock = new JTextField(10);
        txtCategoria = new JTextField(15);

        btnActualizar = new JButton("actualizar producto");

        lblResultado = new JLabel("datos del producto");

        add(new JLabel("nombre:"));
        add(txtNombre);

        add(new JLabel("precio:"));
        add(txtPrecio);

        add(new JLabel("stock:"));
        add(txtStock);

        add(new JLabel("categoria:"));
        add(txtCategoria);

        add(btnActualizar);
        add(lblResultado);

        setVisible(true);
    }
}
