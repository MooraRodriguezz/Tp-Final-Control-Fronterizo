package com.controlfrontera.usuarios;

/**
 * Representa un administrador dentro del sistema, extendiendo la clase base Usuario.
 * Esta clase está diseñada para administrar usuarios y gestionar permisos de país para operaciones específicas.
 */
public class Administrador extends Usuario {
    //Constructores
    public Administrador() {
        super();
    }
    public Administrador(String nombre, String contrasenia, String rol) {
        super(nombre, contrasenia, rol);

    }
}