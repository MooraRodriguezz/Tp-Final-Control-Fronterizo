package com.controlfrontera.usuarios;

import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class GestorUsuarios {

    // Usamos una ObservableList para que la tabla en la UI se actualice automáticamente
    private ObservableList<Usuario> listaDeUsuarios;

    public GestorUsuarios() {
        this.listaDeUsuarios = FXCollections.observableArrayList();
        inicializarUsuarios();
    }

    private void inicializarUsuarios() {
        listaDeUsuarios.add(new Administrador("admin", "admin123", "ADMIN", null, null));
        listaDeUsuarios.add(new Oficial("oficial", "pass123", "OFICIAL", null));
    }

    public Usuario autenticarUsuario(String nombre, String contrasenia) {
        for (Usuario usuario : listaDeUsuarios) {
            if (usuario.getNombre().equals(nombre) && usuario.getContrasenia().equals(contrasenia)) {
                return usuario;
            }
        }
        return null;
    }

    // --- MÉTODOS NUEVOS ---

    /**
     * Devuelve la lista de usuarios para mostrarla en la tabla.
     */
    public ObservableList<Usuario> getListaDeUsuarios() {
        return listaDeUsuarios;
    }

    /**
     * Agrega un nuevo usuario a la lista.
     */
    public void agregarUsuario(Usuario nuevoUsuario) {
        listaDeUsuarios.add(nuevoUsuario);
    }

    /**
     * Elimina un usuario de la lista.
     */
    public void eliminarUsuario(Usuario usuarioAEliminar) {
        listaDeUsuarios.remove(usuarioAEliminar);
    }
}