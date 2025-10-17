package com.controlfrontera.modelo;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class PermisoTrabajo extends Documento {

    private String profesionAutorizada;
    private String empleador;

    // Lista de profesiones válidas para la entrada (ej. solo puestos de alta demanda)
    private static final List<String> PROFESIONES_PERMITIDAS = Arrays.asList(
            "INGENIERO", "MEDICO", "DIPLOMATICO", "TECNICO ESPECIALIZADO"
    );

    public PermisoTrabajo(String numeroIdentificacion, String paisEmisor, boolean valido, Date fechaExpiracion,
                          String profesionAutorizada, String empleador) {
        // El tipo es "Permiso de Trabajo" y el motivo de viaje es implícito/fijo.
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
        //  Llama a la validación base (que revisa la fecha de expiración)
        boolean validoBase = super.validar();

        //  Regla específica: La profesión debe estar en la lista autorizada.
        boolean profesionEstaAutorizada = PROFESIONES_PERMITIDAS.contains(
                this.profesionAutorizada.toUpperCase()
        );

        // --- CORRECCIÓN ---
        // Se elimina la comprobación redundante de this.isValido()
        return validoBase && profesionEstaAutorizada;
    }
}