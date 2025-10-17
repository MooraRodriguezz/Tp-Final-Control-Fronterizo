package com.controlfrontera.persistencia;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class PersistenciaPaises {
    private static final String RUTA_ARCHIVO = "paises.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void guardarPaises(ObservableList<String> paises) {
        try (FileWriter writer = new FileWriter(RUTA_ARCHIVO)) {
            gson.toJson(paises, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ObservableList<String> cargarPaises() {
        try (FileReader reader = new FileReader(RUTA_ARCHIVO)) {
            Type type = new TypeToken<List<String>>(){}.getType();
            List<String> paises = gson.fromJson(reader, type);
            if (paises != null) {
                return FXCollections.observableArrayList(paises);
            }
        } catch (IOException e) {
            // Archivo no encontrado, se creará uno nuevo al guardar.
        }
        return FXCollections.observableArrayList();
    }
}