package com.controlfrontera.Controllers;

import com.controlfrontera.usuarios.Administrador;
import com.controlfrontera.usuarios.GestorUsuarios;
import com.controlfrontera.usuarios.Oficial;
import com.controlfrontera.usuarios.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class LoginViewController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Label errorMessageLabel;

    // 1. Creamos una instancia de nuestro gestor de usuarios.
    private GestorUsuarios gestorUsuarios;

    @FXML
    public void initialize() {
        errorMessageLabel.setVisible(false);
        // 2. Inicializamos el gestor cuando el controlador se carga.
        gestorUsuarios = new GestorUsuarios();
    }

    @FXML
    protected void onLoginButtonClick(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // 3. Usamos nuestro nuevo método de autenticación.
        Usuario usuarioAutenticado = gestorUsuarios.autenticarUsuario(username, password);

        // 4. Verificamos el resultado.
        if (usuarioAutenticado != null) {
            // Verificamos el tipo de usuario usando 'instanceof'
            if (usuarioAutenticado instanceof Administrador) {
                System.out.println("ACCESO AUTORIZADO: ADMINISTRADOR");
                cambiarEscena(event, "/com/example/demo/admin-view.fxml", "Panel de Administración");
            } else if (usuarioAutenticado instanceof Oficial) {
                System.out.println("ACCESO AUTORIZADO: OFICIAL");
                cambiarEscena(event, "/com/example/demo/oficial-view.fxml", "Puesto de Control");
            }
        } else {
            // Si el usuario es null, el acceso es denegado.
            System.err.println("ACCESO DENEGADO.");
            errorMessageLabel.setVisible(true);
        }
    }

    private void cambiarEscena(ActionEvent event, String fxmlFile, String newTitle) {
        try {
            URL fxmlUrl = getClass().getResource(fxmlFile);
            if (fxmlUrl == null) {
                System.err.println("Error: No se pudo encontrar el archivo FXML: " + fxmlFile);
                errorMessageLabel.setText("Error: No se encontró la vista.");
                errorMessageLabel.setVisible(true);
                return;
            }
            //A partir de aca cambia ventana
            Parent nuevaVista = FXMLLoader.load(fxmlUrl);
            Scene nuevaEscena = new Scene(nuevaVista);

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

            window.setScene(nuevaEscena);
            window.setTitle(newTitle);
            window.show();

        } catch (IOException e) {
            System.err.println("Error al cargar la nueva vista FXML:");
            e.printStackTrace();
            errorMessageLabel.setText("Error al cargar la vista.");
            errorMessageLabel.setVisible(true);
        }
    }
}