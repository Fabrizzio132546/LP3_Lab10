package javatro.modelo.jokers;

import java.util.List;
import javatro.logica.evaluador.EvaluadorMano.ResultadoMano;
import javatro.modelo.core.Carta;
import javatro.modelo.core.Valor;

public class FabricaJokers {

    public static Joker crearJoker(String id) {
        switch (id) {
            case "j_bufon":
                return new Joker("Bufón", "+4 Mult", "j_bufon.png") {
                    @Override
                    public void calcularEfecto(ResultadoMano res, List<Carta> cartas) {
                        res.multiplicadorBase += 4;
                    }
                };

            case "j_microfono":
                return new Joker("Comediante", "+10 Mult", "j_microfono.png") {
                    @Override
                    public void calcularEfecto(ResultadoMano res, List<Carta> cartas) {
                        res.multiplicadorBase += 10;
                    }
                };

            // 3. FIBONACCI (Jokers52.png)
            case "j_fibonacci":
                return new Joker("Fibonacci", "+8 Mult si juegas A,2,3,5,8", "j_fibonacci.png") {
                    @Override
                    public void calcularEfecto(ResultadoMano res, List<Carta> cartas) {
                        for(Carta c : cartas) {
                            int v = c.getValor().getValorNumerico();
                            // En Fibonacci: As(11 o 1), 2, 3, 5, 8
                            if(v == 2 || v == 3 || v == 5 || v == 8 || c.getValor() == Valor.AS) {
                                res.multiplicadorBase += 8;
                            }
                        }
                    }
                };

            case "j_banana":
                return new Joker("Gros Michel", "+15 Mult (1/4 romperse)", "j_banana.png") {
                    @Override
                    public void calcularEfecto(ResultadoMano res, List<Carta> cartas) {
                        res.multiplicadorBase += 15;
                        // Aquí podrías agregar lógica para que se elimine, por ahora solo suma
                    }
                };


            case "j_baron":
                return new Joker("Barón", "x1.5 Mult por cada Rey", "j_baron.png") {
                    @Override
                    public void calcularEfecto(ResultadoMano res, List<Carta> cartas) {
                        for(Carta c : cartas) {
                            if(c.getValor() == Valor.REY) {
                                res.multiplicadorBase = (int)(res.multiplicadorBase * 1.5);
                            }
                        }
                    }
                };

            default: return null;
        }
    }
}