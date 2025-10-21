package com.controlfrontera.Controllers;

import com.controlfrontera.usuarios.GestorSonido;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class NotificacionErrorViewController {

    @FXML
    private Label lblMotivoError;

    /**
     * Este metodo es llamado desde el OficialViewController para establecer el mensaje de error.
     * @param motivo El texto del error a mostrar.
     */
    public void setMotivo(String motivo) {
        lblMotivoError.setText(motivo);
    }

    @FXML
    void onCerrarClick(ActionEvent ignoredEvent) {
        GestorSonido.reproducirClick();
        Stage stage = (Stage) ((Node) ignoredEvent.getSource()).getScene().getWindow();
        stage.close();
    }
}