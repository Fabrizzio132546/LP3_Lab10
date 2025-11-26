/*************************************************************************************
ARCHIVO	: Main.java
FECHA	: 25/11/2025
*************************************************************************************/
package Ejercicio1;
import Ejercicio1.modelo.SistemaInventario;
import Ejercicio1.vista.VistaInventario;
import Ejercicio1.controlador.ControladorInventario;

public class Main {
    public static void main(String[] args) {

        SistemaInventario modelo = new SistemaInventario();
        VistaInventario vista = new VistaInventario();
        new ControladorInventario(modelo, vista);
        vista.setVisible(true);
    }
}