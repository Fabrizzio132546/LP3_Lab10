package javatro.modelo.jokers;

import java.util.List;
import javatro.logica.evaluador.EvaluadorMano.ResultadoMano;
import javatro.modelo.core.Carta;

public abstract class Joker {
    protected String nombre;
    protected String descripcion;
    protected String nombreImagen; // Ej: "j_bufon.png"

    public Joker(String nombre, String descripcion, String nombreImagen) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.nombreImagen = nombreImagen;
    }

    public abstract void calcularEfecto(ResultadoMano res, List<Carta> cartasPuntuan);

    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public String getNombreImagen() { return nombreImagen; }
}