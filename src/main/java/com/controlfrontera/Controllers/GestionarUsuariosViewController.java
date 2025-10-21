package com.controlfrontera.Controllers;

import com.controlfrontera.excepciones.RolInvalidoException;
import com.controlfrontera.excepciones.UsuarioYaExisteException;

import com.controlfrontera.usuarios.Administrador;
import com.controlfrontera.usuarios.GestorSonido;
import com.controlfrontera.usuarios.GestorUsuarios;
import com.controlfrontera.usuarios.Oficial;
import com.controlfrontera.usuarios.Usuario;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class GestionarUsuariosViewController {

    @FXML private TableView<Usuario> tablaUsuarios;
    @FXML private TextField txtNombre;
    @FXML private TextField txtContrasenia;
    @FXML private TextField txtRol;

    private GestorUsuarios gestorUsuarios;

    public void initData(GestorUsuarios gestorUsuarios) {
        this.gestorUsuarios = gestorUsuarios;
        tablaUsuarios.setItems(gestorUsuarios.getListaDeUsuarios());
    }

    @FXML
    void onAgregarUsuarioClick() {
        GestorSonido.reproducirClick();
        String nombre = txtNombre.getText();
        String contrasenia = txtContrasenia.getText();
        String rol = txtRol.getText().toUpperCase();

        if (nombre.isEmpty() || contrasenia.isEmpty() || rol.isEmpty()) {
            mostrarAlertaError("Campos incompletos", "Todos los campos (nombre, contraseña y rol) son obligatorios.");
            return;
        }

        try {
            Usuario nuevoUsuario;

            if ("ADMIN".equals(rol)) {
                nuevoUsuario = new Administrador(nombre, contrasenia, rol);
            } else if ("OFICIAL".equals(rol)) {
                nuevoUsuario = new Oficial(nombre, contrasenia, rol);
            } else {
                throw new RolInvalidoException("El rol '" + txtRol.getText() + "' no es válido. Use ADMIN o OFICIAL.");
            }

            gestorUsuarios.agregarUsuario(nuevoUsuario);

            limpiarCampos();
            mostrarAlertaInfo("Éxito", "Usuario '" + nombre + "' agregado correctamente.");

        } catch (RolInvalidoException | UsuarioYaExisteException e) {
            mostrarAlertaError("Error al Agregar Usuario", e.getMessage());
        } catch (Exception e) {
            mostrarAlertaError("Error Inesperado", "Ocurrió un error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void onEliminarUsuarioClick() {
        GestorSonido.reproducirClick();
        Usuario seleccionado = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            gestorUsuarios.eliminarUsuario(seleccionado);
            mostrarAlertaInfo("Usuario Eliminado", "El usuario '" + seleccionado.getNombre() + "' ha sido eliminado.");
        } else {
            mostrarAlertaError("Selección Requerida", "Debe seleccionar un usuario de la tabla para eliminar.");
        }
    }

    private void limpiarCampos() {
        txtNombre.clear();
        txtContrasenia.clear();
        txtRol.clear();
    }

    private void mostrarAlertaError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarAlertaInfo(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}