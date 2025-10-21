package com.controlfrontera.modelo;

import java.util.Date;

public class Pasaporte extends Documento {
    // Constructor
    public Pasaporte(String numeroIdentificacion, String paisEmisor, boolean valido, Date fechaExpiracion) {
        super("Pasaporte", numeroIdentificacion, paisEmisor, valido, "Turismo", fechaExpiracion);
    }

    /**
     * Regla de Validación Específica para Pasaporte:
     * Un pasaporte es válido si no está expirado.
     */
    @Override
    public boolean validar() {
        return super.validar() && this.isValido();
    }
}