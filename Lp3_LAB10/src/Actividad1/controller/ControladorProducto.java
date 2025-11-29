package laboratorio10.controller;

import laboratorio10.model.Producto;
import laboratorio10.view.VistaProducto;

public class ControladorProducto {

    private Producto producto;
    private VistaProducto vista;

    public ControladorProducto(Producto producto, VistaProducto vista) {
        this.producto = producto;
        this.vista = vista;
        inicializarControlador();
    }

    private void inicializarControlador() {
        vista.btnActualizar.addActionListener(e -> actualizarProducto());
    }

    private void actualizarProducto() {

        try {

            String nombre = vista.txtNombre.getText();
            String categoria = vista.txtCategoria.getText();

            double precio = Double.parseDouble(vista.txtPrecio.getText());
            int stock = Integer.parseInt(vista.txtStock.getText());

            producto.setNombre(nombre);
            producto.setPrecio(precio);
            producto.setCantidadStock(stock);
            producto.setCategoria(categoria);

            String info = "<html>producto actualizado:<br>"
                    + "nombre: " + producto.getNombre() + "<br>"
                    + "precio: " + producto.getPrecio() + "<br>"
                    + "stock: " + producto.getCantidadStock() + "<br>"
                    + "categoria: " + producto.getCategoria() + "</html>";

            vista.lblResultado.setText(info);

        } catch (NumberFormatException ex) {
            System.out.println("error: valores numericos invalidos");
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
