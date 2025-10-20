package com.controlfrontera.persistencia;

import com.controlfrontera.modelo.Decision;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PersistenciaDecisiones {
    private static final String RUTA_ARCHIVO = "decisiones.json";

    // Adaptador interno para manejar la serialización y deserialización de LocalDateTime
    private static class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {
        private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        @Override
        public JsonElement serialize(LocalDateTime src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(formatter.format(src));
        }

        @Override
        public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            return LocalDateTime.parse(json.getAsString(), formatter);
        }
    }

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .setPrettyPrinting()
            .create();

    /**
     * Guarda la lista completa de decisiones en un archivo JSON.
     * @param decisiones La lista observable de decisiones a guardar.
     */
    public static void guardarDecisiones(ObservableList<Decision> decisiones) {
        try (FileWriter writer = new FileWriter(RUTA_ARCHIVO)) {
            gson.toJson(decisiones, writer);
        } catch (IOException e) {
            System.err.println("Error al guardar las decisiones: " + e.getMessage());
        }
    }

    /**
     * Carga la lista de decisiones desde un archivo JSON.
     * @return Una lista observable con las decisiones cargadas, o una lista vacía si el archivo no existe.
     */
    public static ObservableList<Decision> cargarDecisiones() {
        try (FileReader reader = new FileReader(RUTA_ARCHIVO)) {
            Type type = new TypeToken<ArrayList<Decision>>(){}.getType();
            List<Decision> decisiones = gson.fromJson(reader, type);
            if (decisiones != null) {
                return FXCollections.observableArrayList(decisiones);
            }
        } catch (IOException e) {
            // Es normal si el archivo no existe la primera vez, se creará al guardar.
            System.out.println("No se encontró archivo de decisiones, se creará uno nuevo.");
        }
        return FXCollections.observableArrayList();
    }
}

