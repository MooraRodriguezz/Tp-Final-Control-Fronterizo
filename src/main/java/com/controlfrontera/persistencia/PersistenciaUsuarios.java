package com.controlfrontera.persistencia;

import com.controlfrontera.usuarios.Usuario;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Clase que gestiona el guardado y carga de usuarios en formato JSON.
 */
public class PersistenciaUsuarios {
    private static final String RUTA_ARCHIVO = "usuarios.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /// Guarda un usuario en JSON
    public static void guardarUsuario(Usuario usuario) {
        try (FileWriter writer = new FileWriter(RUTA_ARCHIVO)) {
            gson.toJson(usuario, writer);
            System.out.println(" Usuario guardado correctamente en " + RUTA_ARCHIVO);
        } catch (IOException e) {
            System.err.println(" Error al guardar usuario: " + e.getMessage());
        }
    }

    /// Carga un usuario desde JSON
    public static Usuario cargarUsuario(Class<? extends Usuario> tipoUsuario) {
        try (FileReader reader = new FileReader(RUTA_ARCHIVO)) {
            Usuario usuario = gson.fromJson(reader, tipoUsuario);
            System.out.println("Usuario cargado correctamente desde " + RUTA_ARCHIVO);
            return usuario;
        } catch (IOException e) {
            System.err.println("No se encontró archivo de usuario o hubo un error al leerlo.");
            return null;
        }
    }
}
