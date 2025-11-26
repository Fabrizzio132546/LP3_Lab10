/*************************************************************************************
ARCHIVO	: ParAsociado.java
FECHA	: 25/11/2025
*************************************************************************************/

package Ejercicio1.modelo;

public class ParAsociado {
    private Equipo equipo;
    private Mantenimiento mantenimiento;

    public ParAsociado(Equipo equipo, Mantenimiento mantenimiento) {
        this.equipo = equipo;
        this.mantenimiento = mantenimiento;
    }

    public Equipo getEquipo() { return equipo; }
    public Mantenimiento getMantenimiento() { return mantenimiento; }
}