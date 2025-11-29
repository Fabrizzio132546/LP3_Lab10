package javatro.datos;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RegistroPartida {
    
    private String fecha;
    private int rondaAlcanzada;
    private int puntajeFinal;
    private int dineroFinal;
    private String resultado; // "VICTORIA" o "DERROTA"


    public RegistroPartida(int ronda, int puntaje, int dinero, boolean gano) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        this.fecha = dtf.format(LocalDateTime.now());
        
        this.rondaAlcanzada = ronda;
        this.puntajeFinal = puntaje;
        this.dineroFinal = dinero;
        this.resultado = gano ? "VICTORIA" : "DERROTA";
    }


    public RegistroPartida(String fecha, int ronda, int puntaje, int dinero, String resultado) {
        this.fecha = fecha;
        this.rondaAlcanzada = ronda;
        this.puntajeFinal = puntaje;
        this.dineroFinal = dinero;
        this.resultado = resultado;
    }


    public String getFecha() { return fecha; }
    public int getRonda() { return rondaAlcanzada; }
    public int getPuntaje() { return puntajeFinal; }
    public int getDinero() { return dineroFinal; }
    public String getResultado() { return resultado; }
}