package com.controlfrontera.modelo;

import java.util.Date;

public class PermisoEntrada extends Documento {

    // El Permiso de Entrada puede tener una duración máxima (ej. 90 días).
    // Para simplificar, asumiremos que si es válido, ya ha pasado la validación de duración al ser emitido.
    // Usaremos el campo 'motivoViaje' heredado.

    public PermisoEntrada(String numeroIdentificacion, String paisEmisor, boolean valido, String motivoViaje, Date fechaExpiracion) {
        // El tipo siempre es "Permiso de Entrada"
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

        // Verifica que la validez sutil (isValido) y las reglas se cumplan
        return validoBase && motivoValido && this.isValido();
    }
}