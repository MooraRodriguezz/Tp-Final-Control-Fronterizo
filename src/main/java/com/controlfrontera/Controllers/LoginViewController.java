package com.controlfrontera.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginViewController {

    // --- CONEXIÓN CON EL FXML ---
    // La anotación @FXML vincula estas variables con los componentes
    // que tienen el mismo fx:id en tu archivo FXML.
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Label errorMessageLabel;

    /**
     * Este metodo se llama automáticamente después de que el archivo FXML ha sido cargado.
     * Es el lugar perfecto para configurar el estado inicial de la vista.
     */
    @FXML
    public void initialize() {
        // Nos aseguramos de que el mensaje de error no se vea al iniciar la app.
        errorMessageLabel.setVisible(false);
    }

    /**
     * Este es el metodo que se ejecuta cuando el usuario hace clic en el botón "Iniciar Sesión".
     * La anotación @FXML es crucial para que el FXML lo encuentre.
     * El nombre "onLoginButtonClick" y el parámetro (ActionEvent event) deben ser exactos.
     * @param event El evento de clic que disparó esta acción.
     */
    @FXML
    protected void onLoginButtonClick(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // --- LÓGICA DE PRUEBA (MOCK DATA) ---
        // Cuando tus compañeros terminen la lógica real, esta parte se reemplazará
        // por una llamada a un metodo que verifique los usuarios desde el archivo JSON.

        if (username.equals("oficial") && password.equals("pass123")) {
            // Login exitoso para el oficial
            System.out.println("Login exitoso como OFICIAL!");
            errorMessageLabel.setVisible(false); // Ocultamos cualquier error previo

            // Aquí, en el futuro, se cargará la siguiente pantalla (oficial-view.fxml)

        } else if (username.equals("admin") && password.equals("admin123")) {
            // Login exitoso para el administrador
            System.out.println("Login exitoso como ADMINISTRADOR!");
            errorMessageLabel.setVisible(false); // Ocultamos cualquier error previo

            // Aquí, en el futuro, se cargará la pantalla del administrador

        } else {
            // Si el login falla
            System.err.println("Error: Usuario o contraseña incorrectos.");
            errorMessageLabel.setText("Usuario o contraseña incorrectos.");
            errorMessageLabel.setVisible(true); // Mostramos el mensaje de error
        }
    }
}