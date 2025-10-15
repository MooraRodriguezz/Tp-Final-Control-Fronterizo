package com.controlfrontera.Controllers; // Tu paquete original

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
import java.net.URL; // Importante para manejar las rutas de los archivos

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
            cambiarEscena(event, "/com/example/demo/admin-view.fxml", "Panel de Administración");

        } else if (username.equals("oficial") && password.equals("pass123")) {
            System.out.println("ACCESO AUTORIZADO: OFICIAL");
            // --- AQUÍ ESTÁ LA LÓGICA AGREGADA ---
            // Llama al mismo método para cambiar la escena, pero con el archivo del oficial.
            cambiarEscena(event, "/com/example/demo/oficial-view.fxml", "Puesto de Control");

        } else {
            System.err.println("ACCESO DENEGADO.");
            errorMessageLabel.setVisible(true);
        }
    }

    /**
     * Un método reutilizable para cambiar de una pantalla a otra.
     * @param event El evento del botón que inició el cambio.
     * @param fxmlFile La ruta completa al nuevo archivo FXML.
     * @param newTitle El nuevo título para la ventana.
     */
    private void cambiarEscena(ActionEvent event, String fxmlFile, String newTitle) {
        try {
            URL fxmlUrl = getClass().getResource(fxmlFile);
            if (fxmlUrl == null) {
                System.err.println("Error: No se pudo encontrar el archivo FXML: " + fxmlFile);
                errorMessageLabel.setText("Error: No se encontró la vista.");
                errorMessageLabel.setVisible(true);
                return;
            }

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