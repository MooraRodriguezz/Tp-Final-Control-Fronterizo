package com.controlfrontera.modelo;

import com.controlfrontera.persistencia.PersistenciaPaises;
import javafx.collections.ObservableList;

/**
 * Clase Singleton para administrar la lista de países válidos.
 */
public class GestorPaises {

    private static GestorPaises instancia;
    private final ObservableList<String> paisesValidos;

    private GestorPaises() {
        this.paisesValidos = PersistenciaPaises.cargarPaises();
        if (this.paisesValidos.isEmpty()) {
            paisesValidos.add("Argentina");
            paisesValidos.add("Peru");
            paisesValidos.add("Chile");
            PersistenciaPaises.guardarPaises(this.paisesValidos);
        }
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
            PersistenciaPaises.guardarPaises(this.paisesValidos);
        }
    }

    public void eliminarPais(String pais) {
        if(paisesValidos.remove(pais)){
            PersistenciaPaises.guardarPaises(this.paisesValidos);
        }
    }
}