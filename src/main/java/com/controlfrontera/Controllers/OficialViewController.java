package com.controlfrontera.Controllers;

import com.controlfrontera.modelo.*;
import com.controlfrontera.usuarios.Oficial;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
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
    @FXML private Button ajustesButton;
    @FXML private ListView<Documento> documentosListView;
    @FXML private ImageView fotoPersonaImageView;
    @FXML private Label motivoLabel;
    @FXML private Label nacionalidadLabel;
    @FXML private Label nombreLabel;
    @FXML private Label puntuacionLabel;

    private Queue<Persona> filaDePersonas;
    private Persona personaActual;
    private Oficial oficialLogueado;
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
     * Este metodo es llamado por el LoginViewController para pasar el oficial que ha iniciado sesión.
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
        boolean veredictoSistema = (motivoRechazoCorrecto == null);

        if (decisionDelOficial == veredictoSistema) {
            oficialLogueado.registrarAcierto();
            registrarDecision(decisionDelOficial, "Decisión correcta.");
        } else {
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

        Documento doc1 = new Documento("Pasaporte", "GDR-12345", "Gondor", true, "Turismo", new Date(System.currentTimeMillis() + 31536000000L));
        Persona p1 = new Persona("Aragorn", "Gondor", new HashSet<>(List.of(doc1)), "1", false, "Aragorn.jpg", new Date(77, 2, 1), 90.5, 181 );
        filaDePersonas.add(p1);

        Documento doc2 = new Documento("Permiso Especial", "SHR-67890", "La Comarca", false, "Negocios", new Date(System.currentTimeMillis() - 86400000L));
        Persona p2 = new Persona("Frodo Baggins", "La Comarca", new HashSet<>(List.of(doc2)), "2", true, "Frodo.jpg", new Date(98, 8, 22), 40.8, 107);
        filaDePersonas.add(p2);

        Documento doc3 = new Documento("Pasaporte Orco", "MDR-X6Y7", "Mordor", true, "Invasión", new Date(System.currentTimeMillis() + 31536000000L));
        Persona p3 = new Persona("Lurtz", "Mordor", new HashSet<>(List.of(doc3)), "3", true, "Lurtz.jpg", new Date(95, 5, 15), 105.2, 190);
        filaDePersonas.add(p3);
    }

    /**
     * Se ejecuta al hacer clic en el botón [ AJUSTES ].
     * Crea y muestra un panel con las opciones.
     */
    @FXML
    void onAjustesClick(ActionEvent event) {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem estadisticasItem = new MenuItem("Ver Estadísticas");
        estadisticasItem.setOnAction(e -> abrirVentanaEstadisticas());

        MenuItem cerrarSesionItem = new MenuItem("Cerrar Sesión");
        cerrarSesionItem.setOnAction(e -> cerrarSesion());

        contextMenu.getItems().addAll(estadisticasItem, cerrarSesionItem);

        Node source = (Node) event.getSource();
        contextMenu.show(source.getScene().getWindow(), source.localToScreen(0, source.getBoundsInLocal().getHeight()).getX(), source.localToScreen(0, source.getBoundsInLocal().getHeight()).getY());
    }

    /**
     * Abre la ventana modal de estadísticas.
     */
    private void abrirVentanaEstadisticas() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/estadisticas-view.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Estadísticas");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Cierra la sesión del oficial y vuelve a la pantalla de login.
     */
    private void cerrarSesion() {
        try {
            Parent loginView = FXMLLoader.load(getClass().getResource("/com/example/demo/login-view.fxml"));
            Scene loginScene = new Scene(loginView, 500, 350);

            Stage window = (Stage) ajustesButton.getScene().getWindow();

            window.setScene(loginScene);
            window.setTitle("Control Fronterizo - Login");
            window.show();
        } catch (IOException e) {
            System.err.println("Error al cargar la vista de login:");
            e.printStackTrace();
        }
    }
}