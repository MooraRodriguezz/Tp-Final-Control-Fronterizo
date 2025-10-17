package com.controlfrontera.usuarios;

import com.controlfrontera.persistencia.PersistenciaGestorUsuarios; // IMPORTAR
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function; // IMPORTAR
import java.util.stream.Collectors; // IMPORTAR

public class GestorUsuarios {

    private static GestorUsuarios instancia;
    private Map<String, Usuario> mapaDeUsuarios;

    private GestorUsuarios() {
        cargarUsuarios();

        if (this.mapaDeUsuarios == null || this.mapaDeUsuarios.isEmpty()) {
            this.mapaDeUsuarios = new HashMap<>();
            inicializarUsuarios();
            guardarUsuarios();
        }
    }

    public static GestorUsuarios getInstancia() {
        if (instancia == null) {
            instancia = new GestorUsuarios();
        }
        return instancia;
    }


    private void inicializarUsuarios() {
        Usuario admin = new Administrador("admin", "admin123", "ADMIN", null, null);
        Usuario oficial = new Oficial("oficial", "pass123", "OFICIAL", null);

        mapaDeUsuarios.put(admin.getNombre(), admin);
        mapaDeUsuarios.put(oficial.getNombre(), oficial);
    }


    public Usuario autenticarUsuario(String nombre, String contrasenia) {
        Usuario usuario = mapaDeUsuarios.get(nombre);
        if (usuario != null && usuario.getContrasenia().equals(contrasenia)) {
            return usuario;
        }
        return null;
    }

    public ObservableList<Usuario> getListaDeUsuarios() {
        return FXCollections.observableArrayList(mapaDeUsuarios.values());
    }

    public void agregarUsuario(Usuario nuevoUsuario) {
        if (nuevoUsuario != null) {
            mapaDeUsuarios.put(nuevoUsuario.getNombre(), nuevoUsuario);
            guardarUsuarios();
        }
    }

    public void eliminarUsuario(Usuario usuarioAEliminar) {
        if (usuarioAEliminar != null) {
            mapaDeUsuarios.remove(usuarioAEliminar.getNombre());
            guardarUsuarios();
        }
    }

    // --- MÉTODOS DE PERSISTENCIA AÑADIDOS ---

    /**
     * Llama a la persistencia para guardar el estado actual de los usuarios.
     * Esto es llamado por OficialViewController.
     */
    public void guardarUsuarios() {
        PersistenciaGestorUsuarios.guardarUsuarios(getListaDeUsuarios());
        System.out.println("Usuarios guardados en JSON.");
    }

    /**
     * Carga los usuarios desde los archivos JSON y los pone en el mapa.
     */
    private void cargarUsuarios() {
        ObservableList<Usuario> listaCargada = PersistenciaGestorUsuarios.cargarUsuarios();
        if (listaCargada != null && !listaCargada.isEmpty()) {
            this.mapaDeUsuarios = listaCargada.stream()
                    .collect(Collectors.toMap(Usuario::getNombre, Function.identity()));
            System.out.println("Usuarios cargados desde JSON.");
        } else {
            System.out.println("No se encontró archivo de usuarios, se crearán nuevos.");
        }
    }
}