/*************************************************************************************
ARCHIVO	: Mantenimiento.java
FECHA	: 25/11/2025
*************************************************************************************/
package Ejercicio1.modelo;

import java.time.LocalDate;

public class Mantenimiento {
    private int id;
    private String descripcion;
    private String tecnico;
    private LocalDate fecha;
    private double costo;

    public Mantenimiento(int id, String descripcion, String tecnico, LocalDate fecha, double costo) {
        this.id = id;
        this.descripcion = descripcion;
        this.tecnico = tecnico;
        this.fecha = fecha;
        this.costo = costo;
    }

    public int getId() { return id; }
    public String getDescripcion() { return descripcion; }
    public String getTecnico() { return tecnico; }
    public LocalDate getFecha() { return fecha; }
    public double getCosto() { return costo; }

    @Override
    public String toString() {
        return "Servicio: " + descripcion + " | Costo: " + costo;
    }
}