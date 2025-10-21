package com.controlfrontera.Controllers;

import com.controlfrontera.usuarios.Administrador;
import com.controlfrontera.usuarios.GestorSonido;
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

    private GestorUsuarios gestorUsuarios;

    @FXML
    public void initialize() {
        errorMessageLabel.setVisible(false);
        gestorUsuarios = GestorUsuarios.getInstancia();
    }

    @FXML
    protected void onLoginButtonClick(ActionEvent event) {
        GestorSonido.reproducirClick();
        String username = usernameField.getText();
        String password = passwordField.getText();

        Usuario usuarioAutenticado = gestorUsuarios.autenticarUsuario(username, password);

        if (usuarioAutenticado != null) {
            if (usuarioAutenticado instanceof Administrador) {
                System.out.println("ACCESO AUTORIZADO: ADMINISTRADOR");
                cambiarEscena(event, "/com/example/demo/admin-view.fxml", "Panel de Administración", usuarioAutenticado, 835, 533);

            } else if (usuarioAutenticado instanceof Oficial) {
                System.out.println("ACCESO AUTORIZADO: OFICIAL");
                cambiarEscena(event, "/com/example/demo/pantalla-inicio-oficial.fxml", "Bienvenido Oficial", usuarioAutenticado, 800, 600);
            }
        } else {
            System.err.println("ACCESO DENEGADO.");
            errorMessageLabel.setVisible(true);
        }
    }

    private void cambiarEscena(ActionEvent event, String fxmlFile, String newTitle, Usuario usuario, double width, double height) {
        try {
            URL fxmlUrl = getClass().getResource(fxmlFile);
            if (fxmlUrl == null) {
                System.err.println("Error: No se pudo encontrar el archivo FXML: " + fxmlFile);
                errorMessageLabel.setText("Error: No se encontró la vista.");
                errorMessageLabel.setVisible(true);
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent nuevaVista = loader.load();

            if (usuario instanceof Oficial) {
                // Si estamos cargando la pantalla de inicio, le pasamos los datos a su controlador
                if (fxmlFile.contains("pantalla-inicio-oficial.fxml")) {
                    PantallaInicioOficialController controller = loader.getController();
                    controller.initData((Oficial) usuario);

                } else if (fxmlFile.contains("oficial-view.fxml")) {
                    OficialViewController controller = loader.getController();
                    controller.initData((Oficial) usuario);
                }
            }

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

            window.setScene(new Scene(nuevaVista, width, height));
            window.setTitle(newTitle);
            window.centerOnScreen();
            window.show();

        } catch (IOException e) {
            System.err.println("Error al cargar la nueva vista FXML:");
            e.printStackTrace();
            errorMessageLabel.setText("Error al cargar la vista.");
            errorMessageLabel.setVisible(true);
        }
    }
}