package com.controlfrontera.Controllers;

import com.controlfrontera.modelo.Decision;
import com.controlfrontera.modelo.Documento;
import com.controlfrontera.modelo.Persona;
import com.controlfrontera.modelo.RegistroDecisiones;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class OficialViewController {

    @FXML private Button aprobarButton;
    @FXML private Button rechazarButton;
    @FXML private ListView<Documento> documentosListView;
    @FXML private ImageView fotoPersonaImageView;
    @FXML private Label motivoLabel;
    @FXML private Label nacionalidadLabel;
    @FXML private Label nombreLabel;

    private Queue<Persona> filaDePersonas;
    private Persona personaActual;
    private final RegistroDecisiones registro = RegistroDecisiones.getInstancia();

    @FXML
    public void initialize() {
        crearDatosDePrueba();

        documentosListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Documento seleccionado = documentosListView.getSelectionModel().getSelectedItem();
                if (seleccionado != null) {
                    abrirVentanaDetalle(seleccionado);
                }
            }
        });

        cargarSiguientePersona();
    }

    private void abrirVentanaDetalle(Documento documento) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/documento-detalle-view.fxml"));
            Parent root = loader.load();

            DocumentoDetalleViewController controller = loader.getController();
            controller.initData(documento);

            Stage stage = new Stage();
            stage.setTitle("Detalle de Documento");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Dentro de la clase OficialViewController

    private void cargarSiguientePersona() {
        personaActual = filaDePersonas.poll();
        if (personaActual != null) {
            nombreLabel.setText(personaActual.getNombre());
            nacionalidadLabel.setText(personaActual.getNacionalidad());
            motivoLabel.setText(personaActual.isSospechosa() ? "Turismo (con alerta)" : "Turismo");

            // Esta es la línea que rellena la lista.
            // Convierte el Set de documentos en una lista que el ListView puede usar.
            documentosListView.setItems(FXCollections.observableArrayList(personaActual.getDocumentos()));
        } else {
            // ... Lógica para cuando no hay más personas ...
            nombreLabel.setText("[NO HAY MÁS PERSONAS]");
            nacionalidadLabel.setText("");
            motivoLabel.setText("");
            documentosListView.getItems().clear();
            aprobarButton.setDisable(true);
            rechazarButton.setDisable(true);
        }
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

    private void crearDatosDePrueba() {
        this.filaDePersonas = new LinkedList<>();

        // Persona 1
        Documento doc1 = new Documento("Pasaporte", "GDR-12345", "Gondor", true, "Turismo", new Date(System.currentTimeMillis() + 31536000000L));
        Persona p1 = new Persona("Aragorn", "Gondor", new HashSet<>(List.of(doc1)), "1", false);
        filaDePersonas.add(p1);

        // Persona 2
        Documento doc2 = new Documento("Permiso Especial", "SHR-67890", "La Comarca", false, "Negocios", new Date(System.currentTimeMillis() - 86400000L));
        Persona p2 = new Persona("Frodo Baggins", "La Comarca", new HashSet<>(List.of(doc2)), "2", true);
        filaDePersonas.add(p2);

        // Persona 3 con múltiples documentos
        Documento doc3a = new Documento("Pasaporte Elfico", "BW-A1B2", "Bosque Negro", true, "Visita Familiar", new Date(System.currentTimeMillis() + 315360000000L));
        Documento doc3b = new Documento("Visa de Trabajo", "GDR-V987", "Gondor", true, "Trabajo", new Date(System.currentTimeMillis() + 15768000000L));
        Persona p3 = new Persona("Legolas", "Bosque Negro", new HashSet<>(List.of(doc3a, doc3b)), "3", false);
        filaDePersonas.add(p3);
    }
}