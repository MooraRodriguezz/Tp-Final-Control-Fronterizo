package com.controlfrontera.Controllers;

import com.controlfrontera.modelo.*;
import com.controlfrontera.usuarios.Oficial;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

public class OficialViewController {

    @FXML private Button aprobarButton;
    @FXML private Button rechazarButton;
    @FXML private ListView<Documento> documentosListView;
    @FXML private ImageView fotoPersonaImageView;
    @FXML private Label motivoLabel;
    @FXML private Label nacionalidadLabel;
    @FXML private Label nombreLabel;
    @FXML private Label puntuacionLabel; // Nuevo Label para la puntuación

    private Queue<Persona> filaDePersonas;
    private Persona personaActual;
    private Oficial oficialLogueado; // Para saber quién es el oficial actual
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

    /**
     * Este método es llamado por el LoginViewController para pasar el oficial que ha iniciado sesión.
     */
    public void initData(Oficial oficial) {
        this.oficialLogueado = oficial;
        actualizarPuntuacionUI();
    }

    private void actualizarPuntuacionUI() {
        if (oficialLogueado != null) {
            puntuacionLabel.setText("Puntos: " + oficialLogueado.getPuntuacion());
        }
    }

    @FXML
    void onAprobarClick(ActionEvent event) {
        procesarDecision(true);
    }

    @FXML
    void onRechazarClick(ActionEvent event) {
        procesarDecision(false);
    }

    private void procesarDecision(boolean decisionDelOficial) {
        if (personaActual == null) return;

        String motivoRechazoCorrecto = personaActual.determinarVeredictoCorrecto();
        boolean veredictoSistema = (motivoRechazoCorrecto == null); // true = APROBAR, false = RECHAZAR

        if (decisionDelOficial == veredictoSistema) {
            // Decisión correcta
            oficialLogueado.registrarAcierto();
            registrarDecision(decisionDelOficial, "Decisión correcta.");
        } else {
            // Decisión incorrecta
            oficialLogueado.registrarError();
            String motivoError = veredictoSistema ? "La persona era válida y fue rechazada." : "La persona era inválida. Motivo real: " + motivoRechazoCorrecto;
            mostrarNotificacionError(motivoError);
            registrarDecision(decisionDelOficial, "ERROR DE PROTOCOLO");
        }

        actualizarPuntuacionUI();
        cargarSiguientePersona();
    }

    private void registrarDecision(boolean aprobada, String motivo) {
        if (personaActual != null) {
            Decision decision = new Decision(personaActual, oficialLogueado, aprobada, motivo, LocalDateTime.now());
            registro.agregarDecision(decision);
        }
    }

    private void mostrarNotificacionError(String motivo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/notificacion-error-view.fxml"));
            Parent root = loader.load();

            NotificacionErrorViewController controller = loader.getController();
            controller.setMotivo(motivo);

            Stage stage = new Stage();
            stage.setTitle("¡Error de Protocolo!");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private void cargarSiguientePersona() {
        personaActual = filaDePersonas.poll();
        if (personaActual != null) {
            nombreLabel.setText(personaActual.getNombre());
            nacionalidadLabel.setText(personaActual.getNacionalidad());
            motivoLabel.setText(personaActual.isSospechosa() ? "Turismo (con alerta)" : "Turismo");
            documentosListView.setItems(FXCollections.observableArrayList(personaActual.getDocumentos()));

            // Lógica para cargar imagen
            if (personaActual.getNombreImagen() != null) {
                try {
                    String imagePath = "/images/" + personaActual.getNombreImagen();
                    Image img = new Image(getClass().getResourceAsStream(imagePath));
                    fotoPersonaImageView.setImage(img);
                } catch (Exception e) {
                    fotoPersonaImageView.setImage(null);
                    System.err.println("No se pudo cargar la imagen: " + personaActual.getNombreImagen());
                }
            } else {
                fotoPersonaImageView.setImage(null);
            }

        } else {
            nombreLabel.setText("[NO HAY MÁS PERSONAS]");
            nacionalidadLabel.setText("");
            motivoLabel.setText("");
            documentosListView.getItems().clear();
            aprobarButton.setDisable(true);
            rechazarButton.setDisable(true);
        }
    }

    private void crearDatosDePrueba() {
        this.filaDePersonas = new LinkedList<>();
        // Persona 1 (Válida)
        Documento doc1 = new Documento("Pasaporte", "GDR-12345", "Gondor", true, "Turismo", new Date(System.currentTimeMillis() + 31536000000L));
        Persona p1 = new Persona("Aragorn", "Gondor", new HashSet<>(List.of(doc1)), "1", false, "Aragorn.jpg");
        filaDePersonas.add(p1);

        // Persona 2 (Inválida por documento)
        Documento doc2 = new Documento("Permiso Especial", "SHR-67890", "La Comarca", false, "Negocios", new Date(System.currentTimeMillis() - 86400000L));
        Persona p2 = new Persona("Frodo Baggins", "La Comarca", new HashSet<>(List.of(doc2)), "2", true, "Frodo.jpg");
        filaDePersonas.add(p2);

        // Persona 3 (Inválida por país)
        Documento doc3 = new Documento("Pasaporte Orco", "MDR-X6Y7", "Mordor", true, "Invasión", new Date(System.currentTimeMillis() + 31536000000L));
        Persona p3 = new Persona("Lurtz", "Mordor", new HashSet<>(List.of(doc3)), "3", true, "Lurtz.jpg");
        filaDePersonas.add(p3);
    }
}