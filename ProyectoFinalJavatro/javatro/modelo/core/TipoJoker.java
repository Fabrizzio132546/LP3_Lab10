package javatro.modelo.core;

public enum TipoJoker {
    CHIPS, // +N fichas
    MULT, // +N multiplicador
    MULTIPLICADOR, // XN multiplicador
    ESPECIAL, // Efectos especiales

    // Nuevos tipos
    MULT_CONDICIONAL_CARTAS, // +N mult por cartas específicas (Fibonacci)
    MULT_CONDICIONAL_MANO, // +N mult si tipo de mano (Monologuista)
    MULTIPLICADOR_CONDICIONAL, // XN mult condicional (Baron)
    DESTRUCTIBLE_MULT, // +N mult con probabilidad destruccion (Gros Michel)
    DESTRUCTIBLE_MULTIPLICADOR // XN mult con probabilidad destruccion (Cavendish)
}
