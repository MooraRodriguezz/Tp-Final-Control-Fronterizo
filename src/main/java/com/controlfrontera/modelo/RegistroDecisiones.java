package com.controlfrontera.modelo;

import javafx.collections.ObservableList;
import com.controlfrontera.persistencia.PersistenciaDecisiones;

/**
 * Clase Singleton para almacenar y gestionar el historial de decisiones.
 * Esto asegura que solo haya una lista de decisiones para toda la aplicación.
 */
public class RegistroDecisiones {

    private static RegistroDecisiones instancia;
    private final ObservableList<Decision> decisiones;


    private RegistroDecisiones() {
        this.decisiones = PersistenciaDecisiones.cargarDecisiones();
    }

    /**
     * Devuelve la única instancia de la clase. Si no existe, la crea.
     */
    public static RegistroDecisiones getInstancia() {
        if (instancia == null) {
            instancia = new RegistroDecisiones();
        }
        return instancia;
    }

    public void agregarDecision(Decision decision) {
        decisiones.add(decision);
        PersistenciaDecisiones.guardarDecisiones(decisiones);
    }

    public ObservableList<Decision> getDecisiones() {
        return decisiones;
    }

    public long getTotalAprobados() {
        return decisiones.stream().filter(d -> d.isAprobada()).count();
    }

    public long getTotalRechazados() {
        return decisiones.stream().filter(d -> !d.isAprobada()).count();
    }

    public long getTotalDecisiones() {
        return decisiones.size();
    }
}