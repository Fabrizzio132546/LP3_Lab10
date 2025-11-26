/*************************************************************************************
ARCHIVO	: Equipo.java
FECHA	: 25/11/2025
*************************************************************************************/

package Ejercicio1.modelo;

public class Equipo {
    public static final String[] TIPOS = {
        "Computadora", "Impresora", "Servidor", "Laptop", "Router", "Otro"
    };

    private int id;
    private String nombre;
    private String tipo;
    private String descripcionTecnica;

    public Equipo(int id, String nombre, String tipo, String descripcionTecnica) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.descripcionTecnica = descripcionTecnica;
    }

    public int getId() { return id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getTipo() { return tipo; } 
    public void setTipo(String tipo) { this.tipo = tipo; }
    
    public String getDescripcionTecnica() { return descripcionTecnica; }
    public void setDescripcionTecnica(String descripcionTecnica) { this.descripcionTecnica = descripcionTecnica; }

    @Override
    public String toString() {
        return nombre + " (" + tipo + ")";
    }
}