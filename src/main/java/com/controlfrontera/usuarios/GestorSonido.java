package com.controlfrontera.usuarios;

import com.example.demo.HelloApplication;
import javafx.scene.media.AudioClip;
import java.net.URL;

public class GestorSonido {

    private static AudioClip audioClipClick;

    static {

        try {

            URL resource = HelloApplication.class.getResource("/sounds/sonido.boton.mp3");

            if (resource != null) {
                audioClipClick = new AudioClip(resource.toExternalForm());
            } else {
                System.err.println("Error CRÍTICO Definitivo: No se pudo encontrar 'sounds/sonido.boton.mp3' ni con ClassLoader ni con HelloApplication.class.");
                URL resourceAlt = GestorSonido.class.getResource("sounds/sonido.boton.mp3"); // Sin barra
                if (resourceAlt != null) {
                    System.err.println("... pero SÍ se encontró con GestorSonido.class.getResource() SIN barra inicial.");
                    audioClipClick = new AudioClip(resourceAlt.toExternalForm());
                    System.out.println(">>> Sonido cargado exitosamente (Alternativa GestorSonido sin /): " + resourceAlt.toExternalForm());
                } else {
                    System.err.println("... y TAMPOCO con GestorSonido.class.getResource() SIN barra inicial.");
                }
            }
        } catch (Exception e) {
            System.err.println("Error EXCEPCIÓN al cargar el sonido de click:");
            e.printStackTrace();
        }
    }

    public static void reproducirClick() {
        if (audioClipClick != null) {
            audioClipClick.play();
        } else {
            System.err.println("Advertencia: Se intentó reproducir sonido, pero audioClipClick sigue siendo null.");
        }
    }
}