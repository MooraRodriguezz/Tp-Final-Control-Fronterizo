package com.controlfrontera.Controllers;

import com.controlfrontera.usuarios.Administrador;
import com.controlfrontera.usuarios.GestorUsuarios;
import com.controlfrontera.usuarios.Oficial;
import com.controlfrontera.usuarios.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class GestionarUsuariosViewController {

    @FXML
    private TableView<Usuario> tablaUsuarios;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtContrasenia;
    @FXML
    private TextField txtRol;

    private GestorUsuarios gestorUsuarios;

    // Este metodo es llamado por AdminViewController para pasarnos el gestor
    public void initData(GestorUsuarios gestorUsuarios) {
        this.gestorUsuarios = gestorUsuarios;
        tablaUsuarios.setItems(gestorUsuarios.getListaDeUsuarios());
    }

    @FXML
    void onAgregarUsuarioClick() {
        String nombre = txtNombre.getText();
        String contrasenia = txtContrasenia.getText();
        String rol = txtRol.getText().toUpperCase();

        if (nombre.isEmpty() || contrasenia.isEmpty() || rol.isEmpty()) {
            mostrarAlerta("Error", "Todos los campos son obligatorios.");
            return;
        }

        Usuario nuevoUsuario;
        if ("ADMIN".equals(rol)) {
            nuevoUsuario = new Administrador(nombre, contrasenia, rol, null, null);
        } else if ("OFICIAL".equals(rol)) {
            nuevoUsuario = new Oficial(nombre, contrasenia, rol, null);
        } else {
            mostrarAlerta("Error", "El rol debe ser 'ADMIN' o 'OFICIAL'.");
            return;
        }

        gestorUsuarios.agregarUsuario(nuevoUsuario);
        limpiarCampos();
    }

    @FXML
    void onEliminarUsuarioClick() {
        Usuario seleccionado = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            gestorUsuarios.eliminarUsuario(seleccionado);
        } else {
            mostrarAlerta("Error", "Debe seleccionar un usuario para eliminar.");
        }
    }

    private void limpiarCampos() {
        txtNombre.clear();
        txtContrasenia.clear();
        txtRol.clear();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}