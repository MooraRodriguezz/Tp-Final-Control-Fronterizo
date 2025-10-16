package com.controlfrontera.usuarios;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Representa un administrador dentro del sistema, extendiendo la clase base Usuario.
 * Esta clase está diseñada para administrar usuarios y gestionar permisos de país para operaciones específicas.
 */
public class Administrador extends Usuario {
    //Atributos
    List<Usuario> listaUsuarios;
    Set<String> paisesValidos;
    //Constructores
    public Administrador() {
    }

    public Administrador(String nombre, String contrasenia, String rol, List<Usuario> listaUsuarios, Set<String> paisesValidos) {
        super(nombre, contrasenia, rol);
        this.listaUsuarios = new ArrayList<>();
        this.paisesValidos = new HashSet<>();
    }
    // Getters and Setters
    public List<Usuario> getListaUsuarios() {
        return listaUsuarios;
    }

    public void setListaUsuarios(List<Usuario> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    public Set<String> getPaisesValidos() {
        return paisesValidos;
    }

    public void setPaisesValidos(Set<String> paisesValidos) {
        this.paisesValidos = paisesValidos;
    }
    // Metodos
    public void agregarUsuario(Usuario u){
        if(u != null){
            this.listaUsuarios.add(u);
        }
    }

    public void eliminarUsuario(Usuario u){
        if(u != null){
            this.listaUsuarios.remove(u);
        }
    }

    public void agregarPais(String pais){
        if(pais != null && !pais.trim().isEmpty()){
            this.paisesValidos.add(pais);
        }

    }
    public void eliminarPais(String pais){
        this.paisesValidos.remove(pais);

    }

    @Override
    public void verMenu() {
        super.verMenu();
    }
}
