package com.controlfrontera.modelo;

import java.util.Date;

public class Pasaporte extends Documento {
    private String tipo; // Ejemplo: "P" para Pasaporte Ordinario

    // Constructor
    public Pasaporte(String numeroIdentificacion, String paisEmisor, boolean valido, Date fechaExpiracion, String tipo) {
        super("Pasaporte", numeroIdentificacion, paisEmisor, valido, "Turismo", fechaExpiracion);
        this.tipo = tipo;
    }

    /**
     * Regla de Validación Específica para Pasaporte:
     * Un pasaporte es válido si no está expirado.
     */
    @Override
    public boolean validar() {
        return super.validar() && this.isValido();
    }

    public String getTipo() {
        return tipo;
    }
}