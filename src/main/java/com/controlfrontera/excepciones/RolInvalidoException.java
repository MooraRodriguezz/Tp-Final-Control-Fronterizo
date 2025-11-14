package com.controlfrontera.excepciones;

/**
 * Excepción personalizada que se lanza cuando se proporciona un rol de usuario inválido
 * (diferente de "ADMIN" u "OFICIAL").
 */
public class RolInvalidoException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Constructor que acepta un mensaje de error detallado.
     * @param message El mensaje que describe el error.
     */
    public RolInvalidoException(String message) {
        super(message);
    }

    public RolInvalidoException() {
        super("El rol especificado es inválido. Debe ser 'ADMIN' o 'OFICIAL'.");
    }
}