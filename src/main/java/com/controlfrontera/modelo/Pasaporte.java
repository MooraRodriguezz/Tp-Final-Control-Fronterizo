package com.controlfrontera.modelo;

import java.time.LocalDate;

public class Pasaporte extends Documento {
    private String tipo; // Ejemplo: "P" para Pasaporte Ordinario

    // **Constructor**
    public Pasaporte(String numeroDocumento, String nombreCompleto, String nacionalidad,
                     LocalDate fechaEmision, LocalDate fechaVencimiento, String ciudadEmision,
                     String rutaFotoDocumento, String tipo) {
        super(numeroDocumento, nombreCompleto, nacionalidad, fechaEmision, fechaVencimiento, ciudadEmision, rutaFotoDocumento);
        this.tipo = tipo;
    }

    @Override
    public boolean esValido() {
        // Un pasaporte es válido si la fecha actual no es posterior a su vencimiento.
        return !LocalDate.now().isAfter(this.fechaExpiracion);
    }

    // --- GETTER específico de Pasaporte ---
    public String getTipo() {
        return tipo;
    }
}