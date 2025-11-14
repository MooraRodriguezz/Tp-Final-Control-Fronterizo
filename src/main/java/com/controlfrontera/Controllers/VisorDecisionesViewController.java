package com.controlfrontera.Controllers;

import com.controlfrontera.modelo.Decision;
import com.controlfrontera.modelo.RegistroDecisiones;
import com.controlfrontera.usuarios.GestorSonido;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class VisorDecisionesViewController {

    @FXML private TableView<Decision> tablaDecisiones;
    @FXML private TableColumn<Decision, String> colOficial;
    @FXML private TableColumn<Decision, String> colPersona;
    @FXML private TableColumn<Decision, Boolean> colDecision;

    // TIPO CORREGIDO: De String a LocalDateTime
    @FXML private TableColumn<Decision, LocalDateTime> colFecha;

    @FXML private TextArea txtDetalleMotivo;

    @FXML private Button btnEliminar;

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

        // --- BLOQUE CORREGIDO ---
        // TIPO CORREGIDO: De String a LocalDateTime
        colFecha.setCellFactory(column -> new javafx.scene.control.TableCell<Decision, LocalDateTime>() {
            @Override

            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {

                    setText(item.format(formatter));
                }
            }
        });


        tablaDecisiones.setItems(registro.getDecisiones());

        tablaDecisiones.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtDetalleMotivo.setText(newSelection.getMotivo());
                btnEliminar.setDisable(false);
            } else {
                txtDetalleMotivo.clear();
                btnEliminar.setDisable(true);
            }
        });

        btnEliminar.setDisable(true);
    }

    @FXML
    void onEliminarClick(ActionEvent event) {
        GestorSonido.reproducirMenuClick();
        Decision seleccionada = tablaDecisiones.getSelectionModel().getSelectedItem();

        if (seleccionada == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Sin selección", "No has seleccionado ninguna decisión para eliminar.");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar Eliminación");
        confirmacion.setHeaderText("¿Seguro que querés eliminar esta decisión?");
        confirmacion.setContentText("Oficial: " + seleccionada.getNombreOficial() + "\nPersona: " + seleccionada.getNombrePersona());

        Optional<ButtonType> resultado = confirmacion.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            registro.eliminarDecision(seleccionada);
            txtDetalleMotivo.clear();
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}