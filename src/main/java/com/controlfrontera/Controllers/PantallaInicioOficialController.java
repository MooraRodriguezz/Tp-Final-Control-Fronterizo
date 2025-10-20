package com.controlfrontera.Controllers;

import com.controlfrontera.usuarios.Oficial;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class PantallaInicioOficialController {

    @FXML
    private Button playButton;
    @FXML
    private Button statsButton;
    @FXML
    private Button ajustesButton;

    private Oficial oficialLogueado;

    public void initData(Oficial oficial) {
        this.oficialLogueado = oficial;
    }

    @FXML
    void iniciarSistema(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/oficial-view.fxml"));
        Parent oficialView = loader.load();

        OficialViewController controller = loader.getController();
        controller.initData(oficialLogueado);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(new Scene(oficialView, 1100, 650));
        window.centerOnScreen();
        window.show();
    }

    /**
     * Abre la ventana de estadísticas.
     */
    @FXML
    void onEstadisticasClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/estadisticas-view.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Estadísticas");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(statsButton.getScene().getWindow()); // Usa una referencia de la escena actual
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Cierra la sesión y vuelve al login.
     */
    @FXML
    void onAjustesClick() {
        try {
            Parent loginView = FXMLLoader.load(getClass().getResource("/com/example/demo/login-view.fxml"));
            Scene loginScene = new Scene(loginView, 550, 400);

            Stage window = (Stage) ajustesButton.getScene().getWindow();

            window.setScene(loginScene);
            window.setTitle("Control Fronterizo - Login");
            window.centerOnScreen();
            window.show();
        } catch (IOException e) {
            System.err.println("Error al cargar la vista de login:");
            e.printStackTrace();
        }
    }
}