package com.controlfrontera.persistencia;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.JSONArray;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class PersistenciaPaises {
    private static final String RUTA_ARCHIVO = "paises.json";
    private static final int INDENT_FACTOR = 4;

    public static void guardarPaises(ObservableList<String> paises) {
        JSONArray jsonArray = new JSONArray(paises);

        try (FileWriter writer = new FileWriter(RUTA_ARCHIVO)) {
            writer.write(jsonArray.toString(INDENT_FACTOR));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ObservableList<String> cargarPaises() {
        try {
            String contenido = new String(Files.readAllBytes(Paths.get(RUTA_ARCHIVO)));
            JSONArray jsonArray = new JSONArray(contenido);

            List<String> paises = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                paises.add(jsonArray.getString(i));
            }

            return FXCollections.observableArrayList(paises);

        } catch (NoSuchFileException e) {
            System.out.println("No se encontró archivo de países, se creará uno nuevo.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return FXCollections.observableArrayList();
    }
}