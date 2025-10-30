package com.controlfrontera.persistencia;

import com.controlfrontera.usuarios.Administrador;
import com.controlfrontera.usuarios.Oficial;
import com.controlfrontera.usuarios.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class PersistenciaGestorUsuarios {
    private static final String OFICIALES_JSON = "oficiales.json";
    private static final String ADMINS_JSON = "admins.json";
    private static final int INDENT_FACTOR = 4;

    public static void guardarUsuarios(ObservableList<Usuario> listaDeUsuarios) {
        List<Oficial> oficiales = new ArrayList<>();
        List<Administrador> administradores = new ArrayList<>();

        for (Usuario u : listaDeUsuarios) {
            if (u instanceof Oficial) {
                oficiales.add((Oficial) u);
            } else if (u instanceof Administrador) {
                administradores.add((Administrador) u);
            }
        }

        // Guardamos los oficiales
        JSONArray jsonOficiales = new JSONArray();
        for (Oficial o : oficiales) {
            JSONObject jsonOficial = new JSONObject();
            jsonOficial.put("puntuacion", o.getPuntuacion());
            jsonOficial.put("erroresConsecutivos", o.getErroresConsecutivos());
            jsonOficial.put("totalAciertos", o.getTotalAciertos());
            jsonOficial.put("totalErrores", o.getTotalErrores());
            jsonOficial.put("nombre", o.getNombre());
            jsonOficial.put("contrasenia", o.getContrasenia());
            jsonOficial.put("rol", o.getRol());
            jsonOficiales.put(jsonOficial);
        }

        try (FileWriter writer = new FileWriter(OFICIALES_JSON)) {
            writer.write(jsonOficiales.toString(INDENT_FACTOR));
        } catch (IOException e) {
            System.err.println("Error al guardar oficiales: " + e.getMessage());
        }

        // Guardamos los administradores
        JSONArray jsonAdmins = new JSONArray();
        for (Administrador a : administradores) {
            JSONObject jsonAdmin = new JSONObject();
            jsonAdmin.put("nombre", a.getNombre());
            jsonAdmin.put("contrasenia", a.getContrasenia());
            jsonAdmin.put("rol", a.getRol());
            jsonAdmins.put(jsonAdmin);
        }

        try (FileWriter writer = new FileWriter(ADMINS_JSON)) {
            writer.write(jsonAdmins.toString(INDENT_FACTOR));
        } catch (IOException e) {
            System.err.println("Error al guardar administradores: " + e.getMessage());
        }
    }

    public static ObservableList<Usuario> cargarUsuarios() {
        ObservableList<Usuario> listaCombinada = FXCollections.observableArrayList();

        // Cargamos los oficiales
        try {
            String contenidoOficiales = new String(Files.readAllBytes(Paths.get(OFICIALES_JSON)));
            JSONArray jsonOficiales = new JSONArray(contenidoOficiales);

            for (int i = 0; i < jsonOficiales.length(); i++) {
                JSONObject jsonOficial = jsonOficiales.getJSONObject(i);
                Oficial oficial = new Oficial();
                oficial.setNombre(jsonOficial.getString("nombre"));
                oficial.setContrasenia(jsonOficial.getString("contrasenia"));
                oficial.setRol(jsonOficial.getString("rol"));
                oficial.setPuntuacion(jsonOficial.getInt("puntuacion"));
                oficial.setTotalAciertos(jsonOficial.getInt("totalAciertos"));
                oficial.setTotalErrores(jsonOficial.getInt("totalErrores"));
                oficial.setErroresConsecutivos(jsonOficial.optInt("erroresConsecutivos", 0));

                listaCombinada.add(oficial);
            }
        } catch (NoSuchFileException e) {
            System.out.println("No se encontro un archivo de oficiales, se creara uno nuevo");
        } catch (IOException e) {
            System.err.println("Error al leer oficiales.json: " + e.getMessage());
        }

        // Cargamos administradores
        try {
            String contenidoAdmins = new String(Files.readAllBytes(Paths.get(ADMINS_JSON)));
            JSONArray jsonAdmins = new JSONArray(contenidoAdmins);

            for (int i = 0; i < jsonAdmins.length(); i++) {
                JSONObject jsonAdmin = jsonAdmins.getJSONObject(i);
                Administrador admin = new Administrador();
                admin.setNombre(jsonAdmin.getString("nombre"));
                admin.setContrasenia(jsonAdmin.getString("contrasenia"));
                admin.setRol(jsonAdmin.getString("rol"));
                listaCombinada.add(admin);
            }
        } catch (NoSuchFileException e) {
            System.out.println("No se encontro un archivo de administradores, se creara uno nuevo");
        } catch (IOException e) {
            System.err.println("Error al leer admins.json: " + e.getMessage());
        }

        return listaCombinada;
    }
}