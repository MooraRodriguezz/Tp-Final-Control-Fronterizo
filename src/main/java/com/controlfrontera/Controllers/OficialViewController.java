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
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

public class OficialViewController {

    // --- Componentes FXML ---
    @FXML private Button aprobarButton;
    @FXML private Button rechazarButton;
    @FXML private Button ajustesButton;
    @FXML private ListView<Documento> documentosListView;
    @FXML private ImageView fotoPersonaImageView;
    @FXML private Label puntuacionLabel;
    @FXML private Label tituloPrincipal;

    // Labels del panel de aplicante (izquierda)
    @FXML private Label nombreLabel;
    @FXML private Label nacionalidadLabel;
    @FXML private Label fechaNacimientoLabel;
    @FXML private Label alturaLabel;
    @FXML private Label pesoLabel;

    // Labels del panel de detalle de documento (centro)
    @FXML private Label lblTipo;
    @FXML private Label lblNumero;
    @FXML private Label lblPaisEmisor;
    @FXML private Label lblFechaExp;
    @FXML private Label lblEstado;


    // --- Atributos de Lógica ---
    private Queue<Persona> filaDePersonas;
    private Persona personaActual;
    private Oficial oficialLogueado;
    private final RegistroDecisiones registro = RegistroDecisiones.getInstancia();
    private final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

    @FXML
    public void initialize() {
        crearDatosDePrueba();

        // Listener para la lista de documentos
        documentosListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                mostrarDetalleDocumento(newSelection);
            }
        });

        cargarSiguientePersona();
    }

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

    private void cargarSiguientePersona() {
        personaActual = filaDePersonas.poll();
        if (personaActual != null) {
            // Panel izquierdo: Datos de la persona
            nombreLabel.setText(personaActual.getNombre());
            nacionalidadLabel.setText(personaActual.getNacionalidad());
            fechaNacimientoLabel.setText(formatter.format(personaActual.getFechaNacimiento()));
            alturaLabel.setText(personaActual.getAltura() + " cm");
            pesoLabel.setText(personaActual.getPeso() + " kg");

            // Cargar imagen
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

            // Panel derecho: Lista de documentos
            documentosListView.setItems(FXCollections.observableArrayList(personaActual.getDocumentos()));
            if (!documentosListView.getItems().isEmpty()) {
                documentosListView.getSelectionModel().selectFirst();
            }

        } else {
            // No hay más personas
            limpiarPaneles();
            nombreLabel.setText("[NO HAY MÁS PERSONAS]");
            aprobarButton.setDisable(true);
            rechazarButton.setDisable(true);
        }
    }

    /**
     * Muestra los detalles de un documento en el panel central.
     */
    private void mostrarDetalleDocumento(Documento doc) {
        lblTipo.setText(doc.getTipo());
        lblNumero.setText(doc.getNumeroIdentificacion());
        lblPaisEmisor.setText(doc.getPaisEmisor());
        lblFechaExp.setText(formatter.format(doc.getFechaExpiracion()));
        lblEstado.setText(doc.validar() ? "VÁLIDO" : "INVÁLIDO");

        if (!doc.validar()) {
            lblEstado.setStyle("-fx-text-fill: #ff4d4d;"); // Rojo
        } else {
            lblEstado.setStyle("-fx-text-fill: #4CAF50;"); // Verde
        }
    }

    /**
     * Limpia todos los paneles de información.
     */
    private void limpiarPaneles() {
        nombreLabel.setText("");
        nacionalidadLabel.setText("");
        fechaNacimientoLabel.setText("");
        alturaLabel.setText("");
        pesoLabel.setText("");
        fotoPersonaImageView.setImage(null);
        documentosListView.getItems().clear();
        lblTipo.setText("");
        lblNumero.setText("");
        lblPaisEmisor.setText("");
        lblFechaExp.setText("");
        lblEstado.setText("");
    }

    // --- Métodos existentes (sin cambios) ---

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

    private void crearDatosDePrueba() {
        this.filaDePersonas = new LinkedList<>();
        // Fechas de nacimiento (año - 1900)
        Date fechaNacAragorn = new Date(87, 2, 1); // 1 de marzo de 1987
        Date fechaNacFrodo = new Date(88, 8, 22); // 22 de septiembre de 1988
        Date fechaNacLurtz = new Date(95, 4, 15); // 15 de mayo de 1995

        Documento doc1 = new Documento("Pasaporte", "GDR-12345", "Gondor", true, "Turismo", new Date(System.currentTimeMillis() + 31536000000L));
        Persona p1 = new Persona("Aragorn", "Gondor", new HashSet<>(List.of(doc1)), "1", false, "Aragorn.jpg", fechaNacAragorn, 90.5, 181 );
        filaDePersonas.add(p1);

        Documento doc2 = new Documento("Permiso Especial", "SHR-67890", "La Comarca", true, "Negocios", new Date(System.currentTimeMillis() - 86400000L)); // Expirado
        Persona p2 = new Persona("Frodo Baggins", "La Comarca", new HashSet<>(List.of(doc2)), "2", true, "Frodo.jpg", fechaNacFrodo, 40.8, 107);
        filaDePersonas.add(p2);

        Documento doc3 = new Documento("Pasaporte Orco", "MDR-X6Y7", "Mordor", true, "Invasión", new Date(System.currentTimeMillis() + 31536000000L)); // Nacionalidad no permitida
        Persona p3 = new Persona("Lurtz", "Mordor", new HashSet<>(List.of(doc3)), "3", true, "Lurtz.jpg", fechaNacLurtz, 105.2, 190);
        filaDePersonas.add(p3);
    }

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