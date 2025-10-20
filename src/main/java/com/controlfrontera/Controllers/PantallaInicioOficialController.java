package com.controlfrontera.Controllers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class PantallaInicioOficialController {

    @FXML
    private Button playButton;

    // Este método se ejecuta cuando se hace clic en el botón "PLAY"
    @FXML
    void iniciarSistema(ActionEvent event) throws IOException {
        // Carga la vista del menú principal
        Parent menuPrincipalView = FXMLLoader.load(getClass().getResource("/com/tpfinal/controlfronterizo/menu-principal-view.fxml"));

        // Crea la nueva escena con el menú principal
        Scene menuPrincipalScene = new Scene(menuPrincipalView);

        // Obtiene la ventana actual (la del botón "PLAY")
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Cambia la escena de la ventana por la del menú principal
        window.setScene(menuPrincipalScene);
        window.show();
    }
}
