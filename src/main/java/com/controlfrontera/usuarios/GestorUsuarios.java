package com.controlfrontera.usuarios;

import com.controlfrontera.excepciones.UsuarioYaExisteException;
import com.controlfrontera.persistencia.PersistenciaGestorUsuarios;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GestorUsuarios {

    private static GestorUsuarios instancia;
    private Map<String, Usuario> mapaDeUsuarios;

    private GestorUsuarios() {
        cargarUsuarios();
        if (this.mapaDeUsuarios == null || this.mapaDeUsuarios.isEmpty()) {
            System.out.println("No se encontraron usuarios, inicializando datos por defecto.");
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
        Usuario admin = new Administrador("admin", "admin123", "ADMIN");
        Usuario oficial = new Oficial("oficial", "pass123", "OFICIAL");
        mapaDeUsuarios.put(admin.getNombre(), admin);
        mapaDeUsuarios.put(oficial.getNombre(), oficial);
    }

    public Usuario autenticarUsuario(String nombre, String contrasenia) {
        Usuario usuario = mapaDeUsuarios.get(nombre);
        // MODIFICADO: Ahora comprueba si el usuario está activo
        if (usuario != null && usuario.getContrasenia().equals(contrasenia) && usuario.isActivo()) {
            return usuario;
        }
        return null;
    }

    public ObservableList<Usuario> getListaDeUsuarios() {
        // Devuelve todos los usuarios (activos e inactivos) para el panel de admin
        return FXCollections.observableArrayList(mapaDeUsuarios.values());
    }


    /**
     * Agrega un nuevo usuario al mapa, verificando si ya existe.
     * @param nuevoUsuario El usuario a agregar.
     * @throws UsuarioYaExisteException Si ya existe un usuario con ese nombre.
     */
    public void agregarUsuario(Usuario nuevoUsuario) throws UsuarioYaExisteException {
        if (nuevoUsuario != null) {
            if (mapaDeUsuarios.containsKey(nuevoUsuario.getNombre())) {
                throw new UsuarioYaExisteException("El usuario '" + nuevoUsuario.getNombre() + "' ya existe.");
            }
            mapaDeUsuarios.put(nuevoUsuario.getNombre(), nuevoUsuario);
            guardarUsuarios();
        }
    }

    // ELIMINADO: eliminarUsuario(Usuario usuarioAEliminar)
    // La baja se maneja modificando el objeto y llamando a guardarUsuarios()
    // desde el controlador.

    public void guardarUsuarios() {
        PersistenciaGestorUsuarios.guardarUsuarios(getListaDeUsuarios());
        System.out.println("Usuarios guardados en JSON.");
    }

    private void cargarUsuarios() {
        ObservableList<Usuario> listaCargada = PersistenciaGestorUsuarios.cargarUsuarios();
        if (listaCargada != null && !listaCargada.isEmpty()) {
            this.mapaDeUsuarios = listaCargada.stream()
                    .collect(Collectors.toMap(Usuario::getNombre, Function.identity()));
            System.out.println("Usuarios cargados desde JSON.");
        } else {
            // No hacemos nada si no carga, el constructor se encarga de inicializar
        }
    }
}