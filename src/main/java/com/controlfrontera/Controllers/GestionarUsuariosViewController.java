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
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class GestionarUsuariosViewController {

    @FXML private TableView<Usuario> tablaUsuarios;
    @FXML private TableColumn<Usuario, String> colNombre;
    @FXML private TableColumn<Usuario, String> colRol;
    @FXML private TableColumn<Usuario, Boolean> colActivo;

    @FXML private TextField txtNombre;
    @FXML private TextField txtContrasenia;
    @FXML private TextField txtRol;
    @FXML private Button btnAgregar;

    @FXML private TextField txtModNombre;
    @FXML private TextField txtModContrasenia;
    @FXML private TextField txtModRol;
    @FXML private CheckBox checkActivo;
    @FXML private Button btnGuardarCambios;


    private GestorUsuarios gestorUsuarios;

    public void initData(GestorUsuarios gestorUsuarios) {
        this.gestorUsuarios = gestorUsuarios;

        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colRol.setCellValueFactory(new PropertyValueFactory<>("rol"));
        colActivo.setCellValueFactory(new PropertyValueFactory<>("activo"));

        tablaUsuarios.setItems(gestorUsuarios.getListaDeUsuarios());

        tablaUsuarios.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                cargarDatosUsuarioSeleccionado(newSelection);
            } else {
                limpiarCamposModificacion();
            }
        });
    }

    @FXML
    void onAgregarUsuarioClick() {
        GestorSonido.reproducirMenuClick();
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
            tablaUsuarios.setItems(gestorUsuarios.getListaDeUsuarios()); // Recargar lista
            tablaUsuarios.refresh();
            limpiarCamposAgregar();
            mostrarAlertaInfo("Éxito", "Usuario '" + nombre + "' agregado correctamente.");

        } catch (RolInvalidoException | UsuarioYaExisteException e) {
            mostrarAlertaError("Error al Agregar Usuario", e.getMessage());
        } catch (Exception e) {
            mostrarAlertaError("Error Inesperado", "Ocurrió un error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void onGuardarCambiosClick() {
        GestorSonido.reproducirMenuClick();
        Usuario seleccionado = tablaUsuarios.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            mostrarAlertaError("Sin selección", "Por favor, seleccione un usuario de la tabla para modificar.");
            return;
        }

        try {
            String nuevaContrasenia = txtModContrasenia.getText();
            String nuevoRol = txtModRol.getText().toUpperCase();
            boolean estaActivo = checkActivo.isSelected();

            if (!"ADMIN".equals(nuevoRol) && !"OFICIAL".equals(nuevoRol)) {
                throw new RolInvalidoException("El rol '" + txtModRol.getText() + "' no es válido. Use ADMIN o OFICIAL.");
            }

            if (nuevaContrasenia != null && !nuevaContrasenia.isEmpty()) {
                seleccionado.setContrasenia(nuevaContrasenia);
            }
            seleccionado.setRol(nuevoRol);
            seleccionado.setActivo(estaActivo);

            gestorUsuarios.guardarUsuarios();

            tablaUsuarios.refresh();
            limpiarCamposModificacion();
            mostrarAlertaInfo("Éxito", "Usuario '" + seleccionado.getNombre() + "' actualizado.");

        } catch (RolInvalidoException e) {
            mostrarAlertaError("Error al Modificar", e.getMessage());
        } catch (Exception e) {
            mostrarAlertaError("Error Inesperado", "Ocurrió un error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void cargarDatosUsuarioSeleccionado(Usuario usuario) {
        txtModNombre.setText(usuario.getNombre());
        txtModContrasenia.setText(""); // No mostramos la contraseña, solo permitimos cambiarla
        txtModContrasenia.setPromptText("Escriba para cambiar contraseña");
        txtModRol.setText(usuario.getRol());
        checkActivo.setSelected(usuario.isActivo());
    }

    private void limpiarCamposAgregar() {
        txtNombre.clear();
        txtContrasenia.clear();
        txtRol.clear();
    }

    private void limpiarCamposModificacion() {
        txtModNombre.clear();
        txtModContrasenia.clear();
        txtModRol.clear();
        checkActivo.setSelected(false);
        txtModNombre.setPromptText("Nombre (No editable)");
        txtModContrasenia.setPromptText("Nueva Contraseña");
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