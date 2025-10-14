package com.example.demo;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Administrador extends Usuario{
    List<Usuario> listaUsuarios;
    Set<String> paisesValidos;



  //constructores


    public Administrador() {
    }


    public Administrador(String nombre, String contrasenia, String rol, List<Usuario> listaUsuarios, Set<String> paisesValidos) {
        super(nombre, contrasenia, rol);
        this.listaUsuarios = new ArrayList<>();
        this.paisesValidos = new HashSet<>();
    }

    public void agregarUsuario(Usuario u){

    }

    public void eliminarUsuario(Usuario u){

    }

    public void agregarPais(String pais){

    }
    public void eliminarPais(String pais){

    }

    @Override
    public void verMenu() {
        super.verMenu();
    }
}
