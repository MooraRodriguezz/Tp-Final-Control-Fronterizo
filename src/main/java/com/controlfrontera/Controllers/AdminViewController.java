package com.controlfrontera.Controllers;

import com.controlfrontera.usuarios.GestorUsuarios;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminViewController {
    @FXML
    private Button cerrarSesionButton;
    @FXML
    private Button configurarPaisesButton;
    @FXML
    private Button gestionarUsuariosButton;
    @FXML
    private Label tituloPrincipal;
    @FXML
    private Button verEstadisticasButton;

    private GestorUsuarios gestorUsuarios = new GestorUsuarios();

    @FXML
    void onGestionarUsuariosClick(ActionEvent event) {
        // ... (este método ya lo tienes implementado)
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/gestionar-usuarios-view.fxml"));
            Parent root = loader.load();

            GestionarUsuariosViewController controller = loader.getController();
            controller.initData(gestorUsuarios);

            Stage stage = new Stage();
            stage.setTitle("Gestionar Usuarios");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // --- MÉTODO ACTUALIZADO ---
    @FXML
    void onCerrarSesionClick(ActionEvent event) {
        try {
            // 1. Cargamos el FXML de la vista de login
            Parent loginView = FXMLLoader.load(getClass().getResource("/com/example/demo/login-view.fxml"));

            // 2. Creamos una nueva escena con esa vista y le damos el tamaño original
            Scene loginScene = new Scene(loginView, 500, 350);

            // 3. Obtenemos la ventana actual (Stage) a partir del evento del botón
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // 4. Establecemos la nueva escena en la ventana, reseteamos el título y la mostramos
            window.setScene(loginScene);
            window.setTitle("Control Fronterizo - Login");
            window.show();

        } catch (IOException e) {
            System.err.println("Error al cargar la vista de login:");
            e.printStackTrace();
        }
    }

    @FXML
    void onConfigurarPaisesClick(ActionEvent event) {
        System.out.println("ACCION: Configurar países");
    }

    @FXML
    void onVerEstadisticasClick(ActionEvent event) {
        System.out.println("ACCION: Ver estadisticas");
    }
}