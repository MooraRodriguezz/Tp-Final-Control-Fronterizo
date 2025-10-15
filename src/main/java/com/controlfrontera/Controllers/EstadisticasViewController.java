package com.controlfrontera.Controllers;

import com.controlfrontera.modelo.RegistroDecisiones;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class EstadisticasViewController {

    @FXML private Label lblTotal;
    @FXML private Label lblAprobados;
    @FXML private Label lblRechazados;

    private RegistroDecisiones registro;

    @FXML
    public void initialize() {
        // Obtenemos la instancia única con los datos
        this.registro = RegistroDecisiones.getInstancia();
        actualizarLabels();
    }

    private void actualizarLabels() {
        lblTotal.setText(String.valueOf(registro.getTotalDecisiones()));
        lblAprobados.setText(String.valueOf(registro.getTotalAprobados()));
        lblRechazados.setText(String.valueOf(registro.getTotalRechazados()));
    }
}