package com.controlfrontera.modelo;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class PermisoTrabajo extends Documento {

    private String profesionAutorizada;
    private String empleador;

    private static final List<String> PROFESIONES_PERMITIDAS = Arrays.asList(
            "INGENIERO", "MEDICO", "DIPLOMATICO", "TECNICO ESPECIALIZADO"
    );

    public PermisoTrabajo(String numeroIdentificacion, String paisEmisor, boolean valido, Date fechaExpiracion,
                          String profesionAutorizada, String empleador) {
        super("Permiso de Trabajo", numeroIdentificacion, paisEmisor, valido, "Trabajo", fechaExpiracion);
        this.profesionAutorizada = profesionAutorizada;
        this.empleador = empleador;
    }

    // --- Getters específicos ---
    public String getProfesionAutorizada() { return profesionAutorizada; }
    public String getEmpleador() { return empleador; }

    /**
     * Regla de Validación Específica:
     * El Permiso de Trabajo es válido si no está expirado Y si la profesión está autorizada.
     */
    @Override
    public boolean validar() {
        boolean validoBase = super.validar();

        boolean profesionEstaAutorizada = PROFESIONES_PERMITIDAS.contains(
                this.profesionAutorizada.toUpperCase()
        );
        return validoBase && profesionEstaAutorizada;
    }
}