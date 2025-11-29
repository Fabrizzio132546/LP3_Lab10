package laboratorio10.view;

import laboratorio10.model.Producto;
import laboratorio10.controller.ControladorProducto;

public class Main {
    public static void main(String[] args) {

        Producto p = new Producto("producto x", 10.0, 5, "general");

        VistaProducto vista = new VistaProducto();

        new ControladorProducto(p, vista);

    }
}
