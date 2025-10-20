package com.controlfrontera.usuarios;

import javafx.scene.media.AudioClip;

import java.applet.AudioClip;
import java.net.URL;

public class GestorSonido {

    private static AudioClip audioClipClick;

    static {
        try {
            URL resource = GestorSonido.class.getResource("sounds/button-pressed-38129.mp3");
            if (resource != null) {
                audioClipClick = new AudioClip(resource.toExternalForm());
            } else {
                System.err.println("Error: No se encontró el archivo de sonido /sounds/boton-click.mp3");
            }catch(Exception e){
                System.err.println("Error al cargar el sonido de click");
                e.printStackTrace();
            }
        }
        public static void reproducirClick () {
            if (audioClipClick != null) {
                audioClipClick.play
            }
        }

    }

}
