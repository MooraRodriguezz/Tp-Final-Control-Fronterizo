package com.controlfrontera.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class NotificacionErrorViewController {

    @FXML
    private Label lblMotivoError;

    /**
     * Este método es llamado desde el OficialViewController para establecer el mensaje de error.
     * @param motivo El texto del error a mostrar.
     */
    public void setMotivo(String motivo) {
        lblMotivoError.setText(motivo);
    }

    @FXML
    void onCerrarClick(ActionEvent event) {
        // Obtenemos la ventana (Stage) actual a partir del botón que se presionó y la cerramos.
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}