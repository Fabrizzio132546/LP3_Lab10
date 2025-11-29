package view;

import model.Producto;
import view.VistaProducto;
import controller.ControladorProducto;

public class Main {
    public static void main(String[] args) {

        // modelo inicial
        Producto p = new Producto("producto x", 10.0, 5, "general");

        // vista
        VistaProducto vista = new VistaProducto();

        // controlador
        new ControladorProducto(p, vista);

    }
}
