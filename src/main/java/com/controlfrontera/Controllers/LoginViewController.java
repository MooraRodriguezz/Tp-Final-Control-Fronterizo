package com.controlfrontera.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

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
    private Label tituloPrincipal;

    @FXML
    public void initialize() {
        errorMessageLabel.setVisible(false);
    }

    @FXML
    protected void onLoginButtonClick(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.equals("oficial") && password.equals("pass123")) {
            System.out.println("ACCESO AUTORIZADO: OFICIAL");
            errorMessageLabel.setVisible(false);
        } else if (username.equals("admin") && password.equals("admin123")) {
            System.out.println("ACCESO AUTORIZADO: ADMINISTRADOR");
            errorMessageLabel.setVisible(false);
        } else {
            System.err.println("ACCESO DENEGADO.");
            errorMessageLabel.setVisible(true);
        }
    }
}