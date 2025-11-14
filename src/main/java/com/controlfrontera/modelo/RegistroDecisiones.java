package com.controlfrontera.modelo;

import javafx.collections.ObservableList;
import com.controlfrontera.persistencia.PersistenciaDecisiones;

public class RegistroDecisiones {

    private static RegistroDecisiones instancia;
    private final ObservableList<Decision> decisiones;

    private RegistroDecisiones() {
        this.decisiones = PersistenciaDecisiones.cargarDecisiones();
    }

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

    public void eliminarDecision(Decision decision) {
        if (decisiones.remove(decision)) {
            PersistenciaDecisiones.guardarDecisiones(decisiones);
        }
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