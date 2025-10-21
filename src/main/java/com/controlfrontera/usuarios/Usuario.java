package com.controlfrontera.usuarios;
/**
 * Representa a un usuario del sistema.
 */
public abstract class Usuario {
    // Atributos
    private String nombre;
    private String contrasenia;
    private String rol;

    // Constructores
    public Usuario() {
    }

    public Usuario(String nombre, String contrasenia, String rol) {
        this.nombre = nombre;
        this.contrasenia = contrasenia;
        this.rol = rol;
    }
    // Getters and Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
