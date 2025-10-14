package com.controlfrontera.modelo;

import java.util.Date;

/**
 * Representa un documento de identidad.
 */
public class Documento implements Verificable {
    /// Atributos
    private String tipo;
    private boolean valido;
    private String motivoViaje;
    private Date fechaExpiracion;

    public Documento() {
    }

    public Documento(String tipo, boolean valido, String motivoViaje, Date fechaExpiracion) {
        this.tipo = tipo;
        this.valido = valido;
        this.motivoViaje = motivoViaje;
        this.fechaExpiracion = fechaExpiracion;
    }
    /// Getters and Setters
    public boolean isValido() {
        return valido;
    }

    public void setValido(boolean valido) {
        this.valido = valido;
    }

    public Date getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(Date fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    public String getMotivoViaje() {
        return motivoViaje;
    }

    public void setMotivoViaje(String motivoViaje) {
        this.motivoViaje = motivoViaje;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    /// Metodos
    @Override
    public boolean validar() {
        return false;
    }
}
