package com.controlfrontera.usuarios;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.HashMap;
import java.util.Map;

public class GestorUsuarios {

    // Cambiamos la lista por un Map para búsquedas eficientes
    private Map<String, Usuario> mapaDeUsuarios;

    public GestorUsuarios() {
        this.mapaDeUsuarios = new HashMap<>();
        inicializarUsuarios();
    }

    private void inicializarUsuarios() {
        // Creamos los usuarios y los agregamos al mapa
        Usuario admin = new Administrador("admin", "admin123", "ADMIN", null, null);
        Usuario oficial = new Oficial("oficial", "pass123", "OFICIAL", null);

        mapaDeUsuarios.put(admin.getNombre(), admin);
        mapaDeUsuarios.put(oficial.getNombre(), oficial);
    }

    public Usuario autenticarUsuario(String nombre, String contrasenia) {
        // Búsqueda directa y mucho más rápida con get()
        Usuario usuario = mapaDeUsuarios.get(nombre);

        // Si el usuario existe y la contraseña coincide, lo retornamos
        if (usuario != null && usuario.getContrasenia().equals(contrasenia)) {
            return usuario;
        }
        return null; // Si no, retornamos null
    }

    /**
     * Devuelve la lista de usuarios para mostrarla en la tabla.
     * La TableView necesita una ObservableList, así que la creamos a partir de los valores del mapa.
     */
    public ObservableList<Usuario> getListaDeUsuarios() {
        return FXCollections.observableArrayList(mapaDeUsuarios.values());
    }

    /**
     * Agrega un nuevo usuario al mapa.
     */
    public void agregarUsuario(Usuario nuevoUsuario) {
        if (nuevoUsuario != null) {
            mapaDeUsuarios.put(nuevoUsuario.getNombre(), nuevoUsuario);
        }
    }

    /**
     * Elimina un usuario del mapa.
     */
    public void eliminarUsuario(Usuario usuarioAEliminar) {
        if (usuarioAEliminar != null) {
            mapaDeUsuarios.remove(usuarioAEliminar.getNombre());
        }
    }
}