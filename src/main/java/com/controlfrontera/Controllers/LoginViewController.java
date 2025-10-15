package com.controlfrontera.Controllers;
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

public class LoginViewController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Label errorMessageLabel;

    @FXML
    public void initialize() {
        errorMessageLabel.setVisible(false);
    }

    @FXML
    protected void onLoginButtonClick(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.equals("admin") && password.equals("admin123")) {
            System.out.println("ACCESO AUTORIZADO: ADMINISTRADOR");
            try {
                // Carga el nuevo FXML del administrador
                // LÍNEA CORRECTA (con la ruta completa)
                Parent adminView = FXMLLoader.load(getClass().getResource("/com/example/demo/admin-view.fxml"));
                Scene adminScene = new Scene(adminView);

                // Obtiene la ventana actual desde el evento del botón
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

                // Cambia la escena en la ventana actual
                window.setScene(adminScene);
                window.setTitle("Panel de Administración");
                window.show();
            } catch (IOException e) {
                e.printStackTrace();
                errorMessageLabel.setText("Error al cargar la vista de admin.");
                errorMessageLabel.setVisible(true);
            }

        } else if (username.equals("oficial") && password.equals("pass123")) {
            System.out.println("ACCESO AUTORIZADO: OFICIAL");
            // Aquí irá la lógica para cargar la vista del oficial en el futuro
            errorMessageLabel.setText("Vista de oficial no implementada.");
            errorMessageLabel.setVisible(true);

        } else {
            System.err.println("ACCESO DENEGADO.");
            errorMessageLabel.setVisible(true);
        }
    }
}