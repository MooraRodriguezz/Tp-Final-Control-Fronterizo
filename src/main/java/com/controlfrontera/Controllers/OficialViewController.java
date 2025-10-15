package com.controlfrontera.Controllers;

import com.controlfrontera.modelo.Decision;
import com.controlfrontera.modelo.Persona;
import com.controlfrontera.modelo.RegistroDecisiones;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Queue;

public class OficialViewController {

    @FXML private Button aprobarButton;
    @FXML private Button rechazarButton;
    @FXML private TextArea documentosTextArea;
    @FXML private ImageView fotoPersonaImageView;
    @FXML private Label motivoLabel;
    @FXML private Label nacionalidadLabel;
    @FXML private Label nombreLabel;

    private Queue<Persona> filaDePersonas;
    private Persona personaActual;
    private RegistroDecisiones registro;

    @FXML
    public void initialize() {
        // Obtenemos la instancia única de nuestro registro
        this.registro = RegistroDecisiones.getInstancia();

        // Creamos una fila de personas de prueba
        this.filaDePersonas = new LinkedList<>();
        filaDePersonas.add(new Persona("Aragorn", "Gondor", null, "1", false));
        filaDePersonas.add(new Persona("Frodo Baggins", "La Comarca", null, "2", true));
        filaDePersonas.add(new Persona("Legolas", "Bosque Negro", null, "3", false));

        cargarSiguientePersona();
    }

    @FXML
    void onAprobarClick(ActionEvent event) {
        registrarDecision(true, "Documentación en regla.");
    }

    @FXML
    void onRechazarClick(ActionEvent event) {
        registrarDecision(false, "Pasaporte expirado.");
    }

    private void registrarDecision(boolean aprobada, String motivo) {
        if (personaActual != null) {
            Decision decision = new Decision(personaActual, null, aprobada, motivo, LocalDateTime.now());
            registro.agregarDecision(decision);
            System.out.println("Decisión registrada: " + personaActual.getNombre() + " -> " + (aprobada ? "Aprobado" : "Rechazado"));
            cargarSiguientePersona();
        }
    }

    private void cargarSiguientePersona() {
        // Sacamos la siguiente persona de la fila
        personaActual = filaDePersonas.poll();

        if (personaActual != null) {
            // Actualizamos la interfaz
            nombreLabel.setText(personaActual.getNombre());
            nacionalidadLabel.setText(personaActual.getNacionalidad());
            motivoLabel.setText(personaActual.isSospechosa() ? "Turismo (con alerta)" : "Turismo");
            documentosTextArea.setText("Pasaporte Vigente\nVisa de Turista");
        } else {
            // Se acabó la fila
            nombreLabel.setText("[NO HAY MÁS PERSONAS]");
            nacionalidadLabel.setText("");
            motivoLabel.setText("");
            documentosTextArea.setText("");
            aprobarButton.setDisable(true);
            rechazarButton.setDisable(true);
        }
    }
}