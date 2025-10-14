package com.controlfrontera.modelo;

import java.util.HashSet;
import java.util.Set;
/**
 * Representa a una persona dentro del sistema.
 */
public class Persona implements Verificable {
    /// Atributos
    private String nombre;
    private String nacionalidad;
    private Set<Documento> documentos;
    private String id;
    private boolean sospechosa;
    ///Constructores
    public Persona() {
    }

    public Persona(String nombre, String nacionalidad, Set<Documento> documentos, String id, boolean sospechosa) {
        this.nombre = nombre;
        this.nacionalidad = nacionalidad;
        this.documentos = new HashSet<>();
        this.id = id;
        this.sospechosa = sospechosa;
    }
    /// Getters and Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isSospechosa() {
        return sospechosa;
    }

    public void setSospechosa(boolean sospechosa) {
        this.sospechosa = sospechosa;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Set<Documento> getDocumentos() {
        return documentos;
    }

    public void setDocumentos(Set<Documento> documentos) {
        this.documentos = documentos;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    /// Metodos
    @Override
    public boolean validar() {
        return false;
    }

    public void mostrarInfo(){

    }
}
