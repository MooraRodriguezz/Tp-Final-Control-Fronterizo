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
import com.controlfrontera.usuarios.GestorSonido;

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


    private GestorUsuarios gestorUsuarios = GestorUsuarios.getInstancia();


    @FXML
    void onGestionarUsuariosClick(ActionEvent ignoredEvent) {
        GestorSonido.reproducirMenuClick();
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

    @FXML
    void onCerrarSesionClick(ActionEvent event) {
        GestorSonido.reproducirMenuClick();
        try {
            Parent loginView = FXMLLoader.load(getClass().getResource("/com/example/demo/login-view.fxml"));

            Scene loginScene = new Scene(loginView, 500, 350);

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

            window.setScene(loginScene);
            window.setTitle("Control Fronterizo - Login");
            window.show();

        } catch (IOException e) {
            System.err.println("Error al cargar la vista de login:");
            e.printStackTrace();
        }
    }
    @FXML
    void onVerDecisionesClick(ActionEvent ignoredEvent) {
        GestorSonido.reproducirMenuClick();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/visor-decisiones-view.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Visor de Decisiones");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void onGestionarPersonasClick(ActionEvent ignoredEvent) {
        GestorSonido.reproducirMenuClick();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/gestionar-personas-view.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Gestionar Personas");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onConfigurarPaisesClick(ActionEvent ignoredEvent) {
        GestorSonido.reproducirMenuClick();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/configurar-paises-view.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Configurar Países Válidos");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onVerEstadisticasClick(ActionEvent ignoredEvent) {
        GestorSonido.reproducirMenuClick();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/estadisticas-view.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Estadísticas");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}