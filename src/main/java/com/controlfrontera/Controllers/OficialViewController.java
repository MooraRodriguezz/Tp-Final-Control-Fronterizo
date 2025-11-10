package com.controlfrontera.Controllers;

import com.controlfrontera.modelo.Decision;
import com.controlfrontera.modelo.Documento;
import com.controlfrontera.modelo.GestorPaises;
import com.controlfrontera.modelo.Pasaporte;
import com.controlfrontera.modelo.PermisoEntrada;
import com.controlfrontera.modelo.PermisoTrabajo;
import com.controlfrontera.modelo.Persona;
import com.controlfrontera.modelo.RegistroDecisiones;

import com.controlfrontera.usuarios.GestorUsuarios;
import com.controlfrontera.usuarios.Oficial;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import java.util.concurrent.ThreadLocalRandom;
import com.controlfrontera.usuarios.GestorSonido;
// NUEVO IMPORT
import com.controlfrontera.persistencia.PersistenciaPersonas;

public class OficialViewController {

    @FXML private Button aprobarButton;
    @FXML private Button rechazarButton;
    @FXML private Button ajustesButton;
    @FXML private ListView<Documento> documentosListView;
    @FXML private ImageView fotoPersonaImageView;
    @FXML private Label puntuacionLabel;
    @FXML private Label tituloPrincipal;
    @FXML private Label nombreLabel;
    @FXML private Label nacionalidadLabel;
    @FXML private Label fechaNacimientoLabel;
    @FXML private Label alturaLabel;
    @FXML private Label pesoLabel;
    @FXML private Label lblTipo;
    @FXML private Label lblNumero;
    @FXML private Label lblPaisEmisor;
    @FXML private Label lblFechaExp;
    @FXML private Label lblEstado;
    @FXML private Label fechaLabel;
    @FXML private Button inspeccionarButton;
    @FXML private Label pesoMedidoLabel;
    @FXML private Label radiografiaLabel;
    @FXML private Button arrestarButton;
    @FXML private Button reglamentoButton;
    @FXML private Button rayosXButton;

    private Queue<Persona> filaDePersonas;
    private Persona personaActual;
    private Oficial oficialLogueado;
    private final RegistroDecisiones registro = RegistroDecisiones.getInstancia();
    private final SimpleDateFormat formatterDoc = new SimpleDateFormat("dd/MM/yyyy");
    private final DateTimeFormatter formatterFechaHoy = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private boolean modoInspeccion = false;
    private double pesoMedidoSimulado = 0.0; // Se usa en simularPesaje
    private List<Pair<String, String>> discrepanciasEncontradas = new ArrayList<>();
    private boolean pesoSospechoso = false;

    private static final double LIMITE_DISCREPANCIA_PESO = 3.0;

    @FXML
    public void initialize() {
        fechaLabel.setText("Fecha: " + LocalDate.now().format(formatterFechaHoy));

        // --- MODIFICADO ---
        // Ya no se llama a crearDatosDePrueba()
        // Ahora cargamos las personas desde el JSON
        this.filaDePersonas = PersistenciaPersonas.cargarPersonas();
        if (this.filaDePersonas.isEmpty()) {
            System.err.println("No se cargaron personas desde el JSON. La fila está vacía.");
        }
        // --- FIN DE MODIFICACIÓN ---

        documentosListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                mostrarDetalleDocumento(newSelection);
                if (modoInspeccion) verificarDiscrepancias();
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

    @FXML void onAprobarClick(ActionEvent ignoredEvent) {
        GestorSonido.reproducirDecision();
        procesarDecision(true);
    }
    @FXML void onRechazarClick(ActionEvent ignoredEvent) {
        GestorSonido.reproducirDecision();
        procesarDecision(false);
    }

    @FXML
    void onArrestarClick(ActionEvent ignoredEvent) {
        GestorSonido.reproducirArresto();

        if (personaActual != null && !arrestarButton.isDisabled()) {
            boolean arrestoCorrecto = personaActual.isTieneContrabando();
            if (arrestoCorrecto) {
                oficialLogueado.registrarArrestoCorrecto();
                mostrarAlertaInfo("Arresto Correcto", personaActual.getNombre() + " tenía contrabando.");
                registrarDecision(false, "ARRESTADO (Contrabando)");
            } else {
                oficialLogueado.registrarArrestoIncorrecto();
                mostrarAlertaError("Arresto Incorrecto", personaActual.getNombre() + " era inocente.");
                registrarDecision(false, "ARRESTADO (Incorrecto - Inocente)");
            }
            finalizarTurnoPersona();
        }
    }

    private void procesarDecision(boolean decisionDelOficial) {
        if (personaActual == null) return;
        if (!modoInspeccion) verificarDiscrepancias();

        String motivoRechazoSistema = personaActual.determinarVeredictoCorrecto();
        boolean veredictoSistema = (motivoRechazoSistema == null && discrepanciasEncontradas.isEmpty());

        boolean acierto = (decisionDelOficial == veredictoSistema);
        String resultado;

        if (acierto) {
            oficialLogueado.registrarAcierto();
            resultado = decisionDelOficial ? "APROBADO" : "RECHAZADO";
            registrarDecision(decisionDelOficial, resultado + " (Correcto).");
        } else {
            oficialLogueado.registrarError();
            resultado = decisionDelOficial ? "APROBADO (ERROR)" : "RECHAZADO (ERROR)";
            String motivoError = "";
            if (decisionDelOficial) {
                motivoError = "Error: Persona inválida aprobada.";
                if (motivoRechazoSistema != null) motivoError += " Motivo: " + motivoRechazoSistema;
                else if (!discrepanciasEncontradas.isEmpty()) motivoError += " Motivo: Discrepancias encontradas.";
            } else {
                motivoError = "Error: Persona válida rechazada.";
            }
            mostrarNotificacionError(motivoError);
            registrarDecision(decisionDelOficial, resultado + ". " + motivoError);
        }
        finalizarTurnoPersona();
    }

    private void finalizarTurnoPersona(){
        actualizarPuntuacionUI();
        GestorUsuarios.getInstancia().guardarUsuarios();
        cargarSiguientePersona();
    }

    private void cargarSiguientePersona() {
        modoInspeccion = false;
        pesoSospechoso = false;
        discrepanciasEncontradas.clear();
        pesoMedidoLabel.setText("[ ? ]");
        pesoMedidoLabel.setStyle("-fx-text-fill: #4CAF50;");
        radiografiaLabel.setVisible(false);
        rayosXButton.setVisible(false);
        arrestarButton.setDisable(true);
        inspeccionarButton.setText("INSPECCIONAR / COMPARAR DATOS");
        resetearEstiloDiscrepancias();

        personaActual = filaDePersonas.poll();
        if (personaActual != null) {
            nombreLabel.setText(personaActual.getNombre());
            nacionalidadLabel.setText(personaActual.getNacionalidad());
            fechaNacimientoLabel.setText(formatterDoc.format(personaActual.getFechaNacimiento()));
            alturaLabel.setText(String.format("%.0f cm", personaActual.getAltura()));
            pesoLabel.setText(String.format("%.1f kg", personaActual.getPeso()));
            cargarImagenPersona(personaActual.getNombreImagen());

            documentosListView.setItems(FXCollections.observableArrayList(personaActual.getDocumentos()));
            if (!documentosListView.getItems().isEmpty()) {
                documentosListView.getSelectionModel().selectFirst();
            } else {
                limpiarDetalleDocumento();
            }
            aprobarButton.setDisable(false);
            rechazarButton.setDisable(false);
            inspeccionarButton.setDisable(false);
            reglamentoButton.setDisable(false);

        } else {
            limpiarPanelesCompletos();
            nombreLabel.setText("[NO HAY MÁS PERSONAS]");
            aprobarButton.setDisable(true);
            rechazarButton.setDisable(true);
            inspeccionarButton.setDisable(true);
            arrestarButton.setDisable(true);
            reglamentoButton.setDisable(true);
        }
    }

    private void cargarImagenPersona(String nombreImagen) {
        if (nombreImagen != null && !nombreImagen.isEmpty()) {
            try { String imagePath = "/images/" + nombreImagen; Image img = new Image(getClass().getResourceAsStream(imagePath)); fotoPersonaImageView.setImage(img); }
            catch (Exception e) { fotoPersonaImageView.setImage(null); System.err.println("No se pudo cargar la imagen: " + nombreImagen + " - " + e.getMessage()); }
        } else { fotoPersonaImageView.setImage(null); }
    }

    private void mostrarDetalleDocumento(Documento doc) {
        if (doc == null) { limpiarDetalleDocumento(); return; }
        lblTipo.setText(doc.getTipo()); lblNumero.setText(doc.getNumeroIdentificacion()); lblPaisEmisor.setText(doc.getPaisEmisor());
        lblFechaExp.setText(doc.getFechaExpiracion() != null ? formatterDoc.format(doc.getFechaExpiracion()) : "N/A");
    }

    private void limpiarDetalleDocumento() {
        lblTipo.setText("[...]"); lblNumero.setText("[...]"); lblPaisEmisor.setText("[...]"); lblFechaExp.setText("[...]"); lblEstado.setText("[...]");
        lblEstado.setStyle("-fx-text-fill: #b9b9b9;");
    }

    private void limpiarPanelesCompletos() {
        nombreLabel.setText(""); nacionalidadLabel.setText(""); fechaNacimientoLabel.setText(""); alturaLabel.setText(""); pesoLabel.setText("");
        pesoMedidoLabel.setText("[ ? ]"); fotoPersonaImageView.setImage(null); documentosListView.getItems().clear(); limpiarDetalleDocumento();
        radiografiaLabel.setVisible(false); rayosXButton.setVisible(false); resetearEstiloDiscrepancias();
    }

    @FXML
    void onInspeccionarClick(ActionEvent ignoredEvent) {
        GestorSonido.reproducirInspeccion();

        if (personaActual == null) return;
        modoInspeccion = !modoInspeccion;

        if (modoInspeccion) {
            inspeccionarButton.setText("DEJAR DE INSPECCIONAR");
            simularPesaje();
            verificarDiscrepancias();
            mostrarDiscrepanciasEncontradas();
            rayosXButton.setVisible(pesoSospechoso);
        } else {
            inspeccionarButton.setText("INSPECCIONAR / COMPARAR DATOS");
            pesoMedidoLabel.setText("[ ? ]");
            pesoMedidoLabel.setStyle("-fx-text-fill: #4CAF50;");
            radiografiaLabel.setVisible(false);
            rayosXButton.setVisible(false);
            arrestarButton.setDisable(true);
            resetearEstiloDiscrepancias();
        }
    }

    private void resetearEstiloDiscrepancias() {
        nombreLabel.setStyle("-fx-border-color: transparent;");
        nacionalidadLabel.setStyle("-fx-border-color: transparent;");
        lblNumero.setStyle("-fx-border-color: transparent;");
        lblPaisEmisor.setStyle("-fx-border-color: transparent;");
    }

    private void simularPesaje() {
        if (personaActual == null) return;

        double pesoBase = personaActual.getPesoMedidoSimulado();

        double pesoExtraContrabando = personaActual.isTieneContrabando() ?
                ThreadLocalRandom.current().nextDouble(5.0, 15.0) : 0.0;

        pesoMedidoSimulado = pesoBase + pesoExtraContrabando;

        pesoMedidoLabel.setText(String.format("%.1f kg", pesoMedidoSimulado));

        pesoSospechoso = (pesoMedidoSimulado - personaActual.getPeso()) > LIMITE_DISCREPANCIA_PESO;

        pesoMedidoLabel.setStyle(pesoSospechoso ? "-fx-text-fill: #ff4d4d;" : "-fx-text-fill: #4CAF50;");
    }


    @FXML
    void onRayosXClick(ActionEvent ignoredEvent) {
        GestorSonido.reproducirScanner();

        if (personaActual != null && modoInspeccion && pesoSospechoso) {
            realizarRadiografia();
            radiografiaLabel.setVisible(true);
        }
    }

    private void realizarRadiografia() {
        if (personaActual.isTieneContrabando()) {
            radiografiaLabel.setText("Rayos-X: ¡CONTRABANDO DETECTADO!");
            radiografiaLabel.setStyle("-fx-text-fill: #ff4d4d;");
            arrestarButton.setDisable(false);
        } else {
            radiografiaLabel.setText("Rayos-X: Negativo");
            radiografiaLabel.setStyle("-fx-text-fill: #4CAF50;");
            arrestarButton.setDisable(true);
        }
    }

    private void verificarDiscrepancias() {
        discrepanciasEncontradas.clear();
        resetearEstiloDiscrepancias();
        if (personaActual == null) return;
    }

    private void mostrarDiscrepanciasEncontradas() {
        if (!discrepanciasEncontradas.isEmpty()) {
            StringBuilder sb = new StringBuilder("Se encontraron discrepancias:\n");
            discrepanciasEncontradas.forEach(p -> sb.append("- ").append(p.getKey()).append(": ").append(p.getValue()).append("\n"));
            mostrarAlertaError("Discrepancias Detectadas", sb.toString());
        }
    }

    @FXML
    void onVerReglamentoClick(ActionEvent event) {

        GestorSonido.reproducirReglamento();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/reglamento-view.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Reglamento Fronterizo");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node)event.getSource()).getScene().getWindow());
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlertaError("Error", "No se pudo abrir la ventana del reglamento.");
        }
    }

    @FXML void onAjustesClick(ActionEvent event) {
        GestorSonido.reproducirMenuClick();

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
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void cerrarSesion() {
        try {
            Parent loginView = FXMLLoader.load(getClass().getResource("/com/example/demo/login-view.fxml"));
            Scene loginScene = new Scene(loginView, 500, 350);
            Stage window = (Stage) ajustesButton.getScene().getWindow();
            window.setScene(loginScene);
            window.setTitle("Control Fronterizo - Login");
            window.show();
        } catch (IOException e) { System.err.println("Error al cargar la vista de login:"); e.printStackTrace(); }
    }

    private void registrarDecision(boolean aprobada, String motivo) {
        if (personaActual != null && oficialLogueado != null) {
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
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void mostrarAlertaError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo); alert.setHeaderText(null); alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarAlertaInfo(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo); alert.setHeaderText(null); alert.setContentText(mensaje);
        alert.showAndWait();
    }

}