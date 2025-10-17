package com.controlfrontera.modelo;

import java.util.Date;

public class PermisoEntrada extends Documento {

    public PermisoEntrada(String numeroIdentificacion, String paisEmisor, boolean valido, String motivoViaje, Date fechaExpiracion) {
        super("Permiso de Entrada", numeroIdentificacion, paisEmisor, valido, motivoViaje, fechaExpiracion);
    }

    /**
     * Regla de Validación Específica:
     * El Permiso de Entrada es válido si no está expirado Y si el motivo es "Turismo" o "Negocios"
     */
    @Override
    public boolean validar() {
        // Llama a la validación base (que revisa la fecha de expiración)
        boolean validoBase = super.validar();

        // Regla específica: El motivo debe ser uno de los permitidos para esta frontera
        String motivo = this.getMotivoViaje();
        boolean motivoValido = motivo != null &&
                (motivo.equalsIgnoreCase("Turismo") ||
                        motivo.equalsIgnoreCase("Negocios") ||
                        motivo.equalsIgnoreCase("Transito"));
        return validoBase && motivoValido;
    }
}