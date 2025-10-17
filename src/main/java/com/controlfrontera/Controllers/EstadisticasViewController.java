package com.controlfrontera.Controllers;

import com.controlfrontera.usuarios.GestorUsuarios;
import com.controlfrontera.usuarios.Oficial;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import java.util.stream.Collectors;

public class EstadisticasViewController {

    @FXML private Label lblTotal;
    @FXML private Label lblAprobados;
    @FXML private Label lblRechazados;
    @FXML private Label lblPuntuacion; // <-- Añadido
    @FXML private ComboBox<Oficial> cmbOficiales; // <-- Añadido

    private GestorUsuarios gestorUsuarios;

    @FXML
    public void initialize() {
        this.gestorUsuarios = GestorUsuarios.getInstancia();

        cargarOficiales();

        cmbOficiales.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                actualizarStats(newSelection);
            }
        });

        limpiarLabels();
    }

    private void cargarOficiales() {
        cmbOficiales.getItems().addAll(
                gestorUsuarios.getListaDeUsuarios().stream()
                        .filter(u -> u instanceof Oficial)
                        .map(u -> (Oficial) u)
                        .collect(Collectors.toList())
        );
    }

    /**
     * Actualiza los labels con las estadísticas del oficial seleccionado.
     */
    private void actualizarStats(Oficial oficial) {
        int total = oficial.getTotalAciertos() + oficial.getTotalErrores();
        lblTotal.setText(String.valueOf(total));
        lblAprobados.setText(String.valueOf(oficial.getTotalAciertos()));
        lblRechazados.setText(String.valueOf(oficial.getTotalErrores()));
        lblPuntuacion.setText(String.valueOf(oficial.getPuntuacion()));
    }

    /**
     * Pone los labels en su estado inicial.
     */
    private void limpiarLabels() {
        lblTotal.setText("-");
        lblAprobados.setText("-");
        lblRechazados.setText("-");
        lblPuntuacion.setText("-");
    }
}