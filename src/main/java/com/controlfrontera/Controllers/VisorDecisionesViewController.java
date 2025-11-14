package com.controlfrontera.Controllers;

import com.controlfrontera.modelo.Decision;
import com.controlfrontera.modelo.RegistroDecisiones;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import java.time.format.DateTimeFormatter;

public class VisorDecisionesViewController {

    @FXML private TableView<Decision> tablaDecisiones;
    @FXML private TableColumn<Decision, String> colOficial;
    @FXML private TableColumn<Decision, String> colPersona;
    @FXML private TableColumn<Decision, Boolean> colDecision;
    @FXML private TableColumn<Decision, String> colFecha;
    @FXML private TextArea txtDetalleMotivo;

    private RegistroDecisiones registro;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'a las' HH:mm:ss");

    @FXML
    public void initialize() {
        this.registro = RegistroDecisiones.getInstancia();


        colOficial.setCellValueFactory(new PropertyValueFactory<>("nombreOficial"));
        colPersona.setCellValueFactory(new PropertyValueFactory<>("nombrePersona"));
        colDecision.setCellValueFactory(new PropertyValueFactory<>("aprobada"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));


        colDecision.setCellFactory(column -> new javafx.scene.control.TableCell<Decision, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item ? "APROBADA" : "RECHAZADA");
                    setStyle(item ? "-fx-text-fill: #4CAF50;" : "-fx-text-fill: #ff4d4d;");
                }
            }
        });


        colFecha.setCellFactory(column -> new javafx.scene.control.TableCell<Decision, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    Decision dec = getTableView().getItems().get(getIndex());
                    setText(dec.getFecha().format(formatter));
                }
            }
        });



        tablaDecisiones.setItems(registro.getDecisiones());


        tablaDecisiones.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtDetalleMotivo.setText(newSelection.getMotivo());
            } else {
                txtDetalleMotivo.clear();
            }
        });
    }
}