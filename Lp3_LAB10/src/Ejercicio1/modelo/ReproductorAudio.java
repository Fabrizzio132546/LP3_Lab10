/*************************************************************************************
ARCHIVO : ReproductorAudio.java
FECHA   : 25/11/2025
*************************************************************************************/
package Ejercicio1.modelo;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReproductorAudio {
    private static final Logger LOGGER = Logger.getLogger(ReproductorAudio.class.getName());
    private static void reproducir(String nombreArchivo) {
        try {
            File archivo = new File(nombreArchivo);
            
            if (archivo.exists()) {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(archivo);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                clip.start();
            } else {

                LOGGER.warning("Archivo de audio no encontrado: " + nombreArchivo);
            }
            
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            LOGGER.log(Level.WARNING, "Error interno al reproducir sonido", e);
        }
    }

    public static void exito() {
        reproducir("exito.wav");
    }

    public static void error() {
        reproducir("error.wav");
    }

    public static void destruir() {
        reproducir("destruir.wav");
    }
}