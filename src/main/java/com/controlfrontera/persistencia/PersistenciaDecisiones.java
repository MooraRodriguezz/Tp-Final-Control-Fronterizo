package com.controlfrontera.persistencia;

import com.controlfrontera.modelo.Decision;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PersistenciaDecisiones {
    private static final String RUTA_ARCHIVO = "decisiones.json";
    private static final int INDENT_FACTOR = 4;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public static void guardarDecisiones(ObservableList<Decision> decisiones) {
        JSONArray jsonArray = new JSONArray();

        for (Decision d : decisiones) {
            JSONObject jsonDecision = new JSONObject();
            jsonDecision.put("nombrePersona", d.getNombrePersona());
            jsonDecision.put("idPersona", d.getIdPersona());
            jsonDecision.put("nombreOficial", d.getNombreOficial());
            jsonDecision.put("aprobada", d.isAprobada());
            jsonDecision.put("motivo", d.getMotivo());
            jsonDecision.put("fecha", d.getFecha().format(formatter));

            jsonArray.put(jsonDecision);
        }

        try (FileWriter writer = new FileWriter(RUTA_ARCHIVO)) {
            writer.write(jsonArray.toString(INDENT_FACTOR));
        } catch (IOException e) {
            System.err.println("Error al guardar las decisiones: " + e.getMessage());
        }
    }

    public static ObservableList<Decision> cargarDecisiones() {
        List<Decision> decisiones = new ArrayList<>();

        try {
            String contenido = new String(Files.readAllBytes(Paths.get(RUTA_ARCHIVO)));
            JSONArray jsonArray = new JSONArray(contenido);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonDecision = jsonArray.getJSONObject(i);

                String fechaString = jsonDecision.getString("fecha");
                LocalDateTime fecha = LocalDateTime.parse(fechaString, formatter);

                // Usa el nuevo constructor agregado en Decision.java
                Decision d = new Decision(
                        jsonDecision.getString("nombrePersona"),
                        jsonDecision.getString("idPersona"),
                        jsonDecision.getString("nombreOficial"),
                        jsonDecision.getBoolean("aprobada"),
                        jsonDecision.getString("motivo"),
                        fecha
                );
                decisiones.add(d);
            }

        } catch (NoSuchFileException e) {
            System.out.println("No se encontró archivo de decisiones, se creará uno nuevo.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return FXCollections.observableArrayList(decisiones);
    }
}