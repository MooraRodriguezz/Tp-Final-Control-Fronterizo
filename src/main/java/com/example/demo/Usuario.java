package com.example.demo;

abstract class Usuario {
    private String nombre;
    private String contrasenia;
    private String rol;

    //constructores

    public Usuario() {
    }

    public Usuario(String nombre, String contrasenia, String rol) {
        this.nombre = nombre;
        this.contrasenia = contrasenia;
        this.rol = rol;
    }

    public boolean login() {
        return false;
    }

    public void verMenu(){

    }

}
