package com.controlfrontera.modelo;

import java.util.Date;

public class Documento implements Verificable {
    // --- Atributos ---
    private String tipo;
    private String numeroIdentificacion;
    private String paisEmisor;
    private boolean valido;
    private String motivoViaje;
    private Date fechaExpiracion;

    // --- Constructores ---
    public Documento() {}

    public Documento(String tipo, String numeroIdentificacion, String paisEmisor, boolean valido, String motivoViaje, Date fechaExpiracion) {
        this.tipo = tipo;
        this.numeroIdentificacion = numeroIdentificacion;
        this.paisEmisor = paisEmisor;
        this.valido = valido;
        this.motivoViaje = motivoViaje;
        this.fechaExpiracion = fechaExpiracion;
    }

    // --- Getters y Setters ---
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getNumeroIdentificacion() { return numeroIdentificacion; }
    public void setNumeroIdentificacion(String numeroIdentificacion) { this.numeroIdentificacion = numeroIdentificacion; }
    public String getPaisEmisor() { return paisEmisor; }
    public void setPaisEmisor(String paisEmisor) { this.paisEmisor = paisEmisor; }
    public boolean isValido() { return valido; }
    public void setValido(boolean valido) { this.valido = valido; }
    public String getMotivoViaje() { return motivoViaje; }
    public void setMotivoViaje(String motivoViaje) { this.motivoViaje = motivoViaje; }
    public Date getFechaExpiracion() { return fechaExpiracion; }
    public void setFechaExpiracion(Date fechaExpiracion) { this.fechaExpiracion = fechaExpiracion; }

    @Override
    public boolean validar() {
        // Un documento es válido si su fecha de expiración es en el futuro.
        return fechaExpiracion.after(new Date());
    }

    @Override
    public String toString() {
        return tipo + " (" + (valido ? "Válido" : "Inválido") + ")";
    }
}