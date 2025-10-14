package com.controlfrontera.modelo;

import java.util.HashSet;
import java.util.Set;

public class Persona implements Verificable {
    private String nombre;
    private String nacionalidad;
    private Set<Documento> documentos;
    private String id;
    private boolean sospechosa;


    //constructores


    public Persona() {
    }

    public Persona(String nombre, String nacionalidad, Set<Documento> documentos, String id, boolean sospechosa) {
        this.nombre = nombre;
        this.nacionalidad = nacionalidad;
        this.documentos = new HashSet<>();
        this.id = id;
        this.sospechosa = sospechosa;
    }

    @Override
    public boolean validar() {
        return false;
    }

    public void mostrarInfo(){

    }
}
