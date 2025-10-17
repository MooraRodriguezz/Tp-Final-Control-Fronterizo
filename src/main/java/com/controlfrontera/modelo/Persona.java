package com.controlfrontera.modelo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class Persona implements Verificable {
    private String nombre;
    private String nacionalidad;
    private Set<Documento> documentos;
    private String id;
    private boolean sospechosa;
    private String nombreImagen;
    private Date fechaNacimiento;
    private double altura;
    private double peso;
    private boolean tieneContrabando;

    public Persona() {
        this.documentos = new HashSet<>();
        this.tieneContrabando = ThreadLocalRandom.current().nextBoolean();
    }

    public Persona(String nombre, String nacionalidad, Set<Documento> documentos, String id,
                   boolean sospechosa, String nombreImagen, Date fechaNacimiento,
                   double peso, double altura) {
        this.nombre = nombre;
        this.nacionalidad = nacionalidad;
        this.documentos = documentos;
        this.id = id;
        this.sospechosa = sospechosa;
        this.nombreImagen = nombreImagen;
        this.fechaNacimiento = fechaNacimiento;
        this.peso = peso;
        this.altura = altura;
        double chance = sospechosa ? 0.7 : 0.3;
        this.tieneContrabando = ThreadLocalRandom.current().nextDouble() < chance;
    }

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
    public Date getFechaNacimiento() {return fechaNacimiento;}
    public void setFechaNacimiento(Date fechaNacimiento) {this.fechaNacimiento = fechaNacimiento;}
    public double getAltura() {return altura;}
    public void setAltura(double altura) {this.altura = altura;}
    public double getPeso() {return peso;}
    public void setPeso(double peso) {this.peso = peso;}
    public boolean isTieneContrabando() { return tieneContrabando; }

    public String determinarVeredictoCorrecto() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        for (Documento doc : this.getDocumentos()) {
            if (!doc.validar()) {
                String fechaExpStr = doc.getFechaExpiracion() != null ?
                        sdf.format(doc.getFechaExpiracion()) : "N/A";
                return "Documento '" + doc.getTipo() + "' ("+ doc.getNumeroIdentificacion()+") caducado el " + fechaExpStr;
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