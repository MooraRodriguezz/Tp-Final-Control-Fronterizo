package com.controlfrontera.usuarios;

import com.example.demo.HelloApplication;
import javafx.scene.media.AudioClip;
import java.net.URL;

public class GestorSonido {

    public static void reproducirDecision()
    { reproducir("/sounds/Aprobar Rechazar.mp3");
    }

    public static void reproducirInspeccion()
    { reproducir("/sounds/Inspeccionar.mp3");
    }

    public static void reproducirReglamento()
    { reproducir("/sounds/Reglamento.mp3");
    }

    public static void reproducirScanner()
    { reproducir("/sounds/Scanner.mp3");
    }

    public static void reproducirArresto()
    { reproducir("/sounds/Arrestar.mp3");
    }

    public static void reproducirMenuClick()
    { reproducir("/sounds/Menu.mp3");
    }

    public static void reproducirLogin()
    { reproducir("/sounds/Login.mp3");
    }


    private static void reproducir(String ruta) {
        try {
            URL resource = HelloApplication.class.getResource(ruta);
            if (resource != null) {
                new AudioClip(resource.toExternalForm()).play();
            } else {
                System.err.println("No se encontró el sonido: " + ruta);
            }
        } catch (Exception e) {
            System.err.println("Error al reproducir el sonido " + ruta);
            e.printStackTrace();
        }
    }
}
