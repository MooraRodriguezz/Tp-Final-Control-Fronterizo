package com.controlfrontera.Controllers;

import com.controlfrontera.modelo.GestorPaises;
import com.controlfrontera.usuarios.GestorSonido;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class ConfigurarPaisesViewController {

    @FXML
    private ListView<String> listaPaises;
    @FXML
    private TextField txtNombrePais;

    private GestorPaises gestorPaises;

    @FXML
    public void initialize() {

        this.gestorPaises = GestorPaises.getInstancia();
        listaPaises.setItems(gestorPaises.getPaisesValidos());
    }

    @FXML
    void onAgregarPaisClick() {
        GestorSonido.reproducirClick();
        String nuevoPais = txtNombrePais.getText();
        gestorPaises.agregarPais(nuevoPais);
        txtNombrePais.clear();
    }

    @FXML
    void onEliminarPaisClick() {
        GestorSonido.reproducirClick();
        String seleccionado = listaPaises.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            gestorPaises.eliminarPais(seleccionado);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, seleccione un país de la lista para eliminar.");
            alert.showAndWait();
        }
    }
}