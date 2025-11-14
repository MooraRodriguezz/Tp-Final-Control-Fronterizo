package com.controlfrontera.Controllers;

import com.controlfrontera.modelo.*;
import com.controlfrontera.persistencia.PersistenciaPersonas;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;

public class GestionarPersonasViewController {

    @FXML private TableView<Persona> tablaPersonas;
    @FXML private TableColumn<Persona, String> colId;
    @FXML private TableColumn<Persona, String> colNombre;
    @FXML private VBox panelDetalle;
    @FXML private TextField txtId;
    @FXML private TextField txtNombre;
    @FXML private TextField txtNacionalidad;
    @FXML private DatePicker dateNacimiento;
    @FXML private TextField txtAltura;
    @FXML private TextField txtPeso;
    @FXML private TextField txtNombreImagen;
    @FXML private CheckBox checkSospechosa;
    @FXML private ListView<Documento> listaDocumentos;
    @FXML private Button btnGuardar;

    private ObservableList<Persona> personasList;
    private Persona personaSeleccionada;

    @FXML
    public void initialize() {
        // Cargar personas y convertirlas a ObservableList
        Queue<Persona> filaDePersonas = PersistenciaPersonas.cargarPersonas();
        personasList = FXCollections.observableArrayList(filaDePersonas);

        // Configurar tabla
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        tablaPersonas.setItems(personasList);

        // Listener para cargar detalles
        tablaPersonas.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                cargarDetallePersona(newSelection);
            } else {
                limpiarDetalle();
            }
        });

        // Formatear ListView de documentos
        listaDocumentos.setCellFactory(param -> new ListCell<Documento>() {
            @Override
            protected void updateItem(Documento item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getTipo() + " - " + item.getNumeroIdentificacion());
                }
            }
        });

        limpiarDetalle();
    }

    private void cargarDetallePersona(Persona persona) {
        personaSeleccionada = persona;
        panelDetalle.setDisable(false);
        txtId.setText(persona.getId());
        txtNombre.setText(persona.getNombre());
        txtNacionalidad.setText(persona.getNacionalidad());
        dateNacimiento.setValue(persona.getFechaNacimiento().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        txtAltura.setText(String.valueOf(persona.getAltura()));
        txtPeso.setText(String.valueOf(persona.getPeso()));
        txtNombreImagen.setText(persona.getNombreImagen());
        checkSospechosa.setSelected(persona.isTieneContrabando()); // De nuevo, esto es ambiguo en tu clase
        listaDocumentos.setItems(FXCollections.observableArrayList(persona.getDocumentos()));
    }

    private void limpiarDetalle() {
        personaSeleccionada = null;
        panelDetalle.setDisable(true);
        txtId.clear();
        txtNombre.clear();
        txtNacionalidad.clear();
        dateNacimiento.setValue(null);
        txtAltura.clear();
        txtPeso.clear();
        txtNombreImagen.clear();
        checkSospechosa.setSelected(false);
        listaDocumentos.setItems(FXCollections.observableArrayList());
    }

    @FXML
    void onNuevoClick(ActionEvent event) {
        // Lógica para crear una nueva persona
        // Primero, creamos una persona "temporal"
        String nuevoId = String.valueOf(personasList.size() + 1); // ID simple
        Set<Documento> docsVacios = new HashSet<>();
        Persona nuevaPersona = new Persona("Nueva Persona", "N/A", docsVacios, nuevoId, false, "default.jpg", new Date(), 0.0, 0.0);

        // La añadimos a la lista y la seleccionamos
        personasList.add(nuevaPersona);
        tablaPersonas.getSelectionModel().select(nuevaPersona);
        tablaPersonas.scrollTo(nuevaPersona);
    }

    @FXML
    void onEliminarClick(ActionEvent event) {
        Persona seleccionada = tablaPersonas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta("Error", "No hay ninguna persona seleccionada.");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar Eliminación");
        confirmacion.setHeaderText("¿Seguro que querés eliminar a " + seleccionada.getNombre() + "?");
        Optional<ButtonType> resultado = confirmacion.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            personasList.remove(seleccionada);
            PersistenciaPersonas.guardarPersonas(personasList);
            limpiarDetalle();
        }
    }

    @FXML
    void onGuardarClick(ActionEvent event) {
        if (personaSeleccionada == null) {
            mostrarAlerta("Error", "No hay ninguna persona seleccionada para guardar.");
            return;
        }

        try {

            personaSeleccionada.setId(txtId.getText());
            personaSeleccionada.setNombre(txtNombre.getText());
            personaSeleccionada.setNacionalidad(txtNacionalidad.getText());
            personaSeleccionada.setFechaNacimiento(Date.from(dateNacimiento.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            personaSeleccionada.setAltura(Double.parseDouble(txtAltura.getText()));
            personaSeleccionada.setPeso(Double.parseDouble(txtPeso.getText()));
            personaSeleccionada.setNombreImagen(txtNombreImagen.getText());
            //personaSeleccionada.setSospechosa(checkSospechosa.isSelected());


            PersistenciaPersonas.guardarPersonas(personasList);

            tablaPersonas.refresh();
            mostrarAlerta("Éxito", "Persona guardada correctamente.");

        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo guardar. Revisa los campos.\n" + e.getMessage());
        }
    }

    @FXML
    void onAgregarDocClick(ActionEvent event) {

        mostrarAlerta("Info", "La funcionalidad de 'Agregar Documento' es compleja y no está implementada en este ABM de ejemplo.");
    }

    @FXML
    void onQuitarDocClick(ActionEvent event) {
        Documento docSeleccionado = listaDocumentos.getSelectionModel().getSelectedItem();
        if (docSeleccionado == null) {
            mostrarAlerta("Error", "Seleccioná un documento de la lista para quitar.");
            return;
        }

        personaSeleccionada.getDocumentos().remove(docSeleccionado);
        listaDocumentos.setItems(FXCollections.observableArrayList(personaSeleccionada.getDocumentos()));

    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}