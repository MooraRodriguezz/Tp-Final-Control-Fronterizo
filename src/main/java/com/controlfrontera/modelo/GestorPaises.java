package com.controlfrontera.modelo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Clase Singleton para administrar la lista de países válidos.
 */
public class GestorPaises {

    private static GestorPaises instancia;
    private final ObservableList<String> paisesValidos;

    private GestorPaises() {
        this.paisesValidos = FXCollections.observableArrayList();
        // Agregamos algunos países de ejemplo
        paisesValidos.add("Gondor");
        paisesValidos.add("La Comarca");
        paisesValidos.add("Mordor");
    }

    public static GestorPaises getInstancia() {
        if (instancia == null) {
            instancia = new GestorPaises();
        }
        return instancia;
    }

    public ObservableList<String> getPaisesValidos() {
        return paisesValidos;
    }

    public void agregarPais(String pais) {
        if (pais != null && !pais.trim().isEmpty() && !paisesValidos.contains(pais)) {
            paisesValidos.add(pais);
        }
    }

    public void eliminarPais(String pais) {
        paisesValidos.remove(pais);
    }
}