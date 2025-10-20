package com.controlfrontera.usuarios; // O el paquete correcto

import com.example.demo.HelloApplication; // <-- *** AÑADE ESTE IMPORT ***
import javafx.scene.media.AudioClip;
import java.net.URL;

public class GestorSonido {

    private static AudioClip audioClipClick;

    static {
        // ... (el código de depuración del classpath puede quedarse si quieres) ...

        try {
            // --- MODIFICACIÓN: Usar HelloApplication.class ---
            // Asegúrate de que la ruta SIGA SIN la barra '/' al inicio
            URL resource = HelloApplication.class.getResource("/sounds/sonido.boton.mp3");
            // Nota: Hemos vuelto a poner la barra '/' inicial aquí, ya que al usar
            // getResource desde una clase en un paquete específico (com.example.demo),
            // la barra inicial indica buscar desde la raíz de los recursos.
            // --- FIN MODIFICACIÓN ---

            if (resource != null) {
                audioClipClick = new AudioClip(resource.toExternalForm());
                System.out.println(">>> Sonido cargado exitosamente (desde HelloApplication): " + resource.toExternalForm());
            } else {
                System.err.println("Error CRÍTICO Definitivo: No se pudo encontrar 'sounds/sonido.boton.mp3' ni con ClassLoader ni con HelloApplication.class.");
                // Puedes intentar la otra forma como respaldo final si quieres:
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