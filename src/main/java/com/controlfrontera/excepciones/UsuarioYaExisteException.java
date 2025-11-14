package com.controlfrontera.excepciones;

/**
 * Excepción personalizada que se lanza cuando se intenta agregar un usuario
 * que ya existe en el sistema (basado en el nombre de usuario).
 */
public class UsuarioYaExisteException extends Exception {

    private static final long serialVersionUID = 2L;

    /**
     * Constructor que acepta un mensaje de error detallado.
     * @param message El mensaje que describe el error.
     */
    public UsuarioYaExisteException(String message) {
        super(message);
    }

    public UsuarioYaExisteException() {
        super("El nombre de usuario ya está registrado en el sistema.");
    }
}