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
    private String nombreImagen; // Atributo para el nombre del archivo de imagen

    public Persona() {
        this.documentos = new HashSet<>();
    }

    public Persona(String nombre, String nacionalidad, Set<Documento> documentos, String id, boolean sospechosa, String nombreImagen) {
        this.nombre = nombre;
        this.nacionalidad = nacionalidad;
        this.documentos = documentos;
        this.id = id;
        this.sospechosa = sospechosa;
        this.nombreImagen = nombreImagen;
    }

    // Getters y Setters (incluyendo el de la nueva propiedad)
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
    public String getNombreImagen() { return nombreImagen; }
    public void setNombreImagen(String nombreImagen) { this.nombreImagen = nombreImagen; }

    /**
     * Revisa todas las reglas para determinar si la persona debe ser aprobada o rechazada.
     * @return Un String con el motivo del rechazo, o null si la persona es válida.
     */
    public String determinarVeredictoCorrecto() {
        for (Documento doc : this.getDocumentos()) {
            if (!doc.validar()) {
                return "Documento caducado: " + doc.getTipo();
            }
        }

        GestorPaises gestorPaises = GestorPaises.getInstancia();
        if (!gestorPaises.getPaisesValidos().contains(this.getNacionalidad())) {
            return "Nacionalidad no permitida: " + this.getNacionalidad();
        }

        return null;
    }

    @Override
    public boolean validar() {
        return determinarVeredictoCorrecto() == null;
    }
}