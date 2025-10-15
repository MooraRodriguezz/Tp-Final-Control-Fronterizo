package com.controlfrontera.modelo;

import java.util.HashSet;
import java.util.Set;

public class Persona implements Verificable {
    // Atributos
    private String nombre;
    private String nacionalidad;
    private Set<Documento> documentos;
    private String id;
    private boolean sospechosa;

    public Persona() {
        this.documentos = new HashSet<>();
    }

    public Persona(String nombre, String nacionalidad, Set<Documento> documentos, String id, boolean sospechosa) {
        this.nombre = nombre;
        this.nacionalidad = nacionalidad;
        this.documentos = documentos;
        this.id = id;
        this.sospechosa = sospechosa;
    }

    // --- El resto de la clase se queda igual ---
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getNacionalidad() { return nacionalidad; }
    public void setNacionalidad(String nacionalidad) { this.nacionalidad = nacionalidad; }
    public Set<Documento> getDocumentos() { return documentos; }
    public void setDocumentos(Set<Documento> documentos) { this.documentos = documentos; }
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public boolean isSospechosa() { return sospechosa; }
    public void setSospechosa(boolean sospechosa) { this.sospechosa = sospechosa; }

    @Override
    public boolean validar() {
        return false;
    }
}