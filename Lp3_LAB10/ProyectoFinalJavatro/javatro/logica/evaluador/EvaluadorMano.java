package javatro.logica.evaluador;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javatro.modelo.core.Carta;
import javatro.modelo.core.Palo;
import javatro.modelo.core.Valor;

public class EvaluadorMano {

    public static class ResultadoMano {
        public String nombreMano;
        public int fichasBase;
        public int multiplicadorBase;
        public List<Carta> cartasQuePuntuan;

        public ResultadoMano(String nombre, int fichas, int mult, List<Carta> cartasPuntuan) {
            this.nombreMano = nombre;
            this.fichasBase = fichas;
            this.multiplicadorBase = mult;
            this.cartasQuePuntuan = cartasPuntuan;
        }
    }

    public static ResultadoMano evaluar(List<Carta> cartas) {
        if (cartas == null || cartas.isEmpty()) 
            return new ResultadoMano("CARTA ALTA", 5, 1, new ArrayList<>());

        List<Carta> ordenada = new ArrayList<>(cartas);
        ordenada.sort((c1, c2) -> Integer.compare(c2.getValor().ordinal(), c1.getValor().ordinal()));

        Map<Integer, List<Carta>> gruposValor = new HashMap<>();
        Map<Palo, List<Carta>> gruposPalo = new HashMap<>();

        for (Carta c : ordenada) {
            int val = c.getValor().ordinal();
            gruposValor.putIfAbsent(val, new ArrayList<>());
            gruposValor.get(val).add(c);
            
            Palo p = c.getPalo();
            gruposPalo.putIfAbsent(p, new ArrayList<>());
            gruposPalo.get(p).add(c);
        }

        List<Carta> pokerCards = new ArrayList<>();
        List<Carta> trioCards = new ArrayList<>();
        List<List<Carta>> paresCards = new ArrayList<>();

        for (List<Carta> grupo : gruposValor.values()) {
            if (grupo.size() == 4) pokerCards.addAll(grupo);
            if (grupo.size() == 3) trioCards.addAll(grupo);
            if (grupo.size() == 2) paresCards.add(grupo);
        }

        List<Carta> colorCards = null;
        for (List<Carta> grupo : gruposPalo.values()) {
            if (grupo.size() >= 5) colorCards = grupo.subList(0, 5); // Tomamos las 5 mejores
        }
        
        List<Carta> escaleraCards = buscarEscalera(ordenada);


        if (escaleraCards != null && colorCards != null) {

            if (contieneAs(escaleraCards) && contieneRey(escaleraCards))
                return new ResultadoMano("ESCALERA REAL", 100, 8, escaleraCards);
            return new ResultadoMano("ESCALERA COLOR", 100, 8, escaleraCards);
        }


        if (!pokerCards.isEmpty()) {
            return new ResultadoMano("POKER", 60, 7, pokerCards);
        }


        if (!trioCards.isEmpty() && !paresCards.isEmpty()) {
            List<Carta> full = new ArrayList<>(trioCards);
            full.addAll(paresCards.get(0));
            return new ResultadoMano("FULL HOUSE", 40, 4, full);
        }

        if (colorCards != null) {
            return new ResultadoMano("COLOR", 35, 4, colorCards);
        }


        if (escaleraCards != null) {
            return new ResultadoMano("ESCALERA", 30, 4, escaleraCards);
        }


        if (!trioCards.isEmpty()) {
            return new ResultadoMano("TRIO", 30, 3, trioCards);
        }


        if (paresCards.size() >= 2) {
            List<Carta> doblePar = new ArrayList<>();
            doblePar.addAll(paresCards.get(0));
            doblePar.addAll(paresCards.get(1));
            return new ResultadoMano("DOBLE PAR", 20, 2, doblePar);
        }


        if (paresCards.size() == 1) {
            return new ResultadoMano("PAR", 10, 2, paresCards.get(0));
        }


        List<Carta> cartaAlta = new ArrayList<>();
        cartaAlta.add(ordenada.get(0)); // La primera es la mayor porque ordenamos al inicio
        return new ResultadoMano("CARTA ALTA", 5, 1, cartaAlta);
    }


    private static List<Carta> buscarEscalera(List<Carta> cartas) {
        if (cartas.size() < 5) return null;
        List<Carta> unicos = new ArrayList<>();

        for(Carta c : cartas) {
            boolean repetido = false;
            for(Carta u : unicos) if(u.getValor() == c.getValor()) repetido = true;
            if(!repetido) unicos.add(c);
        }
        if(unicos.size() < 5) return null;

        for (int i = 0; i <= unicos.size() - 5; i++) {
            boolean esEscalera = true;
            for (int j = 0; j < 4; j++) {
                if (unicos.get(i+j).getValor().ordinal() != unicos.get(i+j+1).getValor().ordinal() + 1) {
                    esEscalera = false; 
                    break;
                }
            }
            if (esEscalera) return unicos.subList(i, i+5);
        }

        return null; 
    }
    
    private static boolean contieneAs(List<Carta> l) { for(Carta c:l) if(c.getValor()==Valor.AS) return true; return false; }
    private static boolean contieneRey(List<Carta> l) { for(Carta c:l) if(c.getValor()==Valor.REY) return true; return false; }
}