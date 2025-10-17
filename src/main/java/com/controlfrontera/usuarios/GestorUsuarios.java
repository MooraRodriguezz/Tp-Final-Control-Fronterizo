package com.controlfrontera.usuarios;

// Importamos la nueva clase de persistencia
import com.controlfrontera.persistencia.PersistenciaGestorUsuarios;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class GestorUsuarios {

    // --- Inicio de Singleton ---
    private static GestorUsuarios instancia;
    private ObservableList<Usuario> listaDeUsuarios;

    // 1. Constructor privado: solo se puede llamar desde esta clase
    private GestorUsuarios() {
        // 2. Carga los usuarios desde el JSON al iniciar
        this.listaDeUsuarios = PersistenciaGestorUsuarios.cargarUsuarios();

        // 3. Si está vacío (primera ejecución), inicializa con datos y guarda
        if (this.listaDeUsuarios.isEmpty()) {
            inicializarUsuarios();
            guardarUsuarios(); // Guarda los usuarios iniciales
        }
    }

    /**
     * 4. Método público estático para obtener la instancia única.
     */
    public static GestorUsuarios getInstancia() {
        if (instancia == null) {
            instancia = new GestorUsuarios();
        }
        return instancia;
    }
    // --- Fin de Singleton ---


    private void inicializarUsuarios() {
        listaDeUsuarios.add(new Administrador("admin", "admin123", "ADMIN", null, null));
        listaDeUsuarios.add(new Oficial("oficial", "pass123", "OFICIAL", null));
    }

    /**
     * Guarda el estado actual de la lista de usuarios en los archivos JSON.
     * Este método es llamado por los controladores cuando los datos cambian.
     */
    public void guardarUsuarios() {
        PersistenciaGestorUsuarios.GuardarUsuarios(this.listaDeUsuarios);
    }

    public Usuario autenticarUsuario(String nombre, String contrasenia) {
        for (Usuario usuario : listaDeUsuarios) {
            if (usuario.getNombre().equals(nombre) && usuario.getContrasenia().equals(contrasenia)) {
                return usuario;
            }
        }
        return null;
    }


    /**
     * Devuelve la lista de usuarios para mostrarla en la tabla.
     */
    public ObservableList<Usuario> getListaDeUsuarios() {
        return listaDeUsuarios;
    }

    /**
     * Agrega un nuevo usuario a la lista y guarda los cambios.
     */
    public void agregarUsuario(Usuario nuevoUsuario) {
        listaDeUsuarios.add(nuevoUsuario);
        guardarUsuarios(); // <-- Guarda al agregar
    }

    /**
     * Elimina un usuario de la lista y guarda los cambios.
     */
    public void eliminarUsuario(Usuario usuarioAEliminar) {
        listaDeUsuarios.remove(usuarioAEliminar);
        guardarUsuarios(); // <-- Guarda al eliminar
    }
}