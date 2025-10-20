package com.controlfrontera.Controllers;

import com.controlfrontera.modelo.*;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import javafx.scene.media.AudioClip;

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
    private AudioClip sonidoClick;

    private Queue<Persona> filaDePersonas;
    private Persona personaActual;
    private Oficial oficialLogueado;
    private final RegistroDecisiones registro = RegistroDecisiones.getInstancia();
    private final SimpleDateFormat formatterDoc = new SimpleDateFormat("dd/MM/yyyy");
    private final DateTimeFormatter formatterFechaHoy = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private boolean modoInspeccion = false;
    private double pesoMedidoSimulado = 0.0;
    private List<Pair<String, String>> discrepanciasEncontradas = new ArrayList<>();
    private boolean pesoSospechoso = false;

    private static final double LIMITE_DISCREPANCIA_PESO = 3.0;

    @FXML
    public void initialize() {
        fechaLabel.setText("Fecha: " + LocalDate.now().format(formatterFechaHoy));
        crearDatosDePrueba();
        documentosListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                mostrarDetalleDocumento(newSelection);
                if (modoInspeccion) verificarDiscrepancias();
            }
        });
        cargarSiguientePersona();
        try {

            String soundFile = "/sounds/button-pressed-38129.mp3";
            sonidoClick = new AudioClip(getClass().getResource(soundFile).toExternalForm());
        } catch (Exception e) {
            System.err.println("Error al cargar el sonido 'button-pressed': " + e.getMessage());
        }
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

    @FXML void onAprobarClick(ActionEvent event) {
        if(sonidoClick != null){
            sonidoClick.play();
        }
        procesarDecision(true);
    }
    @FXML void onRechazarClick(ActionEvent event) { procesarDecision(false); }

    @FXML
    void onArrestarClick(ActionEvent event) {
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
            reglamentoButton.setDisable(false); // Habilitar reglamento

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
        if (nombreImagen != null) {
            try { String imagePath = "/images/" + nombreImagen; Image img = new Image(getClass().getResourceAsStream(imagePath)); fotoPersonaImageView.setImage(img); }
            catch (Exception e) { fotoPersonaImageView.setImage(null); System.err.println("No se pudo cargar la imagen: " + nombreImagen + " - " + e.getMessage()); }
        } else { fotoPersonaImageView.setImage(null); }
    }

    private void mostrarDetalleDocumento(Documento doc) {
        if (doc == null) { limpiarDetalleDocumento(); return; }
        lblTipo.setText(doc.getTipo()); lblNumero.setText(doc.getNumeroIdentificacion()); lblPaisEmisor.setText(doc.getPaisEmisor());
        lblFechaExp.setText(doc.getFechaExpiracion() != null ? formatterDoc.format(doc.getFechaExpiracion()) : "N/A");
        boolean esValido = doc.validar();
        lblEstado.setText(esValido ? "VÁLIDO" : "INVÁLIDO"); lblEstado.setStyle(esValido ? "-fx-text-fill: #4CAF50;" : "-fx-text-fill: #ff4d4d;");
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
    void onInspeccionarClick(ActionEvent event) {
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

        // Peso base fijo de la persona
        double pesoBase = personaActual.getPesoMedidoSimulado();

        // Extra de contrabando (si tiene)
        double pesoExtraContrabando = personaActual.isTieneContrabando() ?
                ThreadLocalRandom.current().nextDouble(5.0, 15.0) : 0.0;

        // Peso medido real
        pesoMedidoSimulado = pesoBase + pesoExtraContrabando;

        // Mostrar en la UI
        pesoMedidoLabel.setText(String.format("%.1f kg", pesoMedidoSimulado));

        // Determinar si es sospechoso
        pesoSospechoso = (pesoMedidoSimulado - personaActual.getPeso()) > LIMITE_DISCREPANCIA_PESO;

        // Cambiar color del label según sospecha
        pesoMedidoLabel.setStyle(pesoSospechoso ? "-fx-text-fill: #ff4d4d;" : "-fx-text-fill: #4CAF50;");
    }


    @FXML
    void onRayosXClick(ActionEvent event) {
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
        // Lógica de comparación futura
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

    private void crearDatosDePrueba() {
        this.filaDePersonas = new LinkedList<>();
        long d = 86400000L; // un día en ms
        long a = 31536000000L; // un año en ms
        Date hoy = new Date();

        Date fnA = new Date(87, 2, 1);
        Date fnF = new Date(88, 8, 22);
        Date fnL = new Date(95, 4, 15);
        Date fnG = new Date(79, 10, 5);
        Date fnLe = new Date(100, 5, 20);
        Date fnB = new Date(85, 0, 10);
        Date fnE = new Date(90, 7, 1);

        // Persona 1
        Pasaporte p1d1 = new Pasaporte("GDR-12345", "Argentina", true, new Date(hoy.getTime() + a), "P");
        Persona p1 = new Persona("Aragorn", "Argentina", Set.of(p1d1), "1", false, "Aragorn.jpg", fnA, 90.5, 181);
        filaDePersonas.add(p1);

        // Persona 2
        PermisoEntrada p2d1 = new PermisoEntrada("SHR-67890", "Chile", true, "Negocios", new Date(hoy.getTime() - d));
        Persona p2 = new Persona("Frodo Baggins", "Chile", Set.of(p2d1), "2", true, "Frodo.jpg", fnF, 40.8, 107);
        filaDePersonas.add(p2);

        // Persona 3
        Pasaporte p3d1 = new Pasaporte("MDR-X6Y7", "Mordor", true, new Date(hoy.getTime() + a * 2), "O");
        Persona p3 = new Persona("Lurtz", "Mordor", Set.of(p3d1), "3", true, "Lurtz.jpg", fnL, 105.2, 190);
        filaDePersonas.add(p3);

        // Persona 4
        PermisoTrabajo p4d1 = new PermisoTrabajo("CL-ENG-987", "Chile", true, new Date(hoy.getTime() + a / 2), "INGENIERO", "Minera Andina");
        Persona p4 = new Persona("Gimli", "Chile", Set.of(p4d1), "4", false, null, fnG, 85.0, 140);
        filaDePersonas.add(p4);

        // Persona 5
        PermisoTrabajo p5d1 = new PermisoTrabajo("PE-ART-111", "Peru", true, new Date(hoy.getTime() + a), "ARTESANO", "Mercado Local");
        Persona p5 = new Persona("Legolas", "Peru", Set.of(p5d1), "5", false, null, fnLe, 70.0, 185);
        filaDePersonas.add(p5);

        // Persona 6
        Pasaporte p6d1 = new Pasaporte("ARG-P-555", "Argentina", true, new Date(hoy.getTime() + a * 3), "P");
        PermisoEntrada p6d2 = new PermisoEntrada("ARG-T-TEMP", "Argentina", true, "Turismo", new Date(hoy.getTime() - d * 10));
        Persona p6 = new Persona("Boromir", "Argentina", Set.of(p6d1, p6d2), "6", true, null, fnB, 95.0, 183);
        filaDePersonas.add(p6);

        // Persona 7
        PermisoEntrada p7d1 = new PermisoEntrada("ARG-TUR-XYZ", "Argentina", true, "Turismo", new Date(hoy.getTime() + d * 80));
        Persona p7 = new Persona("Éowyn", "Argentina", Set.of(p7d1), "7", false, null, fnE, 65.0, 170);
        filaDePersonas.add(p7);

        // Persona 8
        PermisoEntrada p8d1 = new PermisoEntrada("PE-INV-456", "Peru", true, "Invasión", new Date(hoy.getTime() + d * 150));
        Persona p8 = new Persona("Grima", "Peru", Set.of(p8d1), "8", true, null, new Date(70, 0, 1), 60.0, 175);
        filaDePersonas.add(p8);

        // Persona 9
        Pasaporte p9d1 = new Pasaporte("CHL-P-999", "Chile", true, new Date(hoy.getTime() + a * 5), "P");
        Persona p9 = new Persona("Samwise", "Chile", Set.of(p9d1), "9", false, null, new Date(89, 3, 6), 75.0, 160);
        filaDePersonas.add(p9);
    }


}