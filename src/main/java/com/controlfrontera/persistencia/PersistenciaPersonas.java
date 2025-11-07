package com.controlfrontera.persistencia;

import com.controlfrontera.modelo.Documento;
import com.controlfrontera.modelo.Pasaporte;
import com.controlfrontera.modelo.PermisoEntrada;
import com.controlfrontera.modelo.PermisoTrabajo;
import com.controlfrontera.modelo.Persona;
import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class PersistenciaPersonas {

    private static final String RUTA_ARCHIVO = "personas.json";
    // Formato YYYY-MM-DD para compatibilidad con JSON
    private static final SimpleDateFormat aJsonFecha = new SimpleDateFormat("yyyy-MM-dd");

    public static Queue<Persona> cargarPersonas() {
        Queue<Persona> filaDePersonas = new LinkedList<>();

        try {
            String contenido = new String(Files.readAllBytes(Paths.get(RUTA_ARCHIVO)));
            JSONArray jsonArray = new JSONArray(contenido);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonPersona = jsonArray.getJSONObject(i);

                String id = jsonPersona.getString("id");
                String nombre = jsonPersona.getString("nombre");
                String nacionalidad = jsonPersona.getString("nacionalidad");
                Date fechaNacimiento = aJsonFecha.parse(jsonPersona.getString("fechaNacimiento"));
                double altura = jsonPersona.getDouble("altura");
                double peso = jsonPersona.getDouble("peso");
                String nombreImagen = jsonPersona.optString("nombreImagen", null);
                boolean sospechosa = jsonPersona.getBoolean("sospechosa");

                // Parsear documentos
                Set<Documento> documentos = new HashSet<>();
                JSONArray jsonDocumentos = jsonPersona.getJSONArray("documentos");
                for (int j = 0; j < jsonDocumentos.length(); j++) {
                    documentos.add(parsearDocumento(jsonDocumentos.getJSONObject(j)));
                }

                Persona p = new Persona(nombre, nacionalidad, documentos, id, sospechosa, nombreImagen, fechaNacimiento, peso, altura);
                filaDePersonas.add(p);
            }

        } catch (NoSuchFileException e) {
            System.err.println("ERROR: No se encontró 'personas.json'. La simulación no tendrá personas.");
        } catch (Exception e) {
            System.err.println("ERROR FATAL al cargar 'personas.json': " + e.getMessage());
            e.printStackTrace();
        }

        return filaDePersonas;
    }

    private static Documento parsearDocumento(JSONObject jsonDoc) throws Exception {
        String tipo = jsonDoc.getString("tipo");
        String numeroIdentificacion = jsonDoc.getString("numeroIdentificacion");
        String paisEmisor = jsonDoc.getString("paisEmisor");
        boolean valido = jsonDoc.getBoolean("valido");
        Date fechaExpiracion = aJsonFecha.parse(jsonDoc.getString("fechaExpiracion"));

        switch (tipo) {
            case "Pasaporte":
                return new Pasaporte(numeroIdentificacion, paisEmisor, valido, fechaExpiracion);
            case "PermisoEntrada":
                String motivoViaje = jsonDoc.getString("motivoViaje");
                return new PermisoEntrada(numeroIdentificacion, paisEmisor, valido, motivoViaje, fechaExpiracion);
            case "PermisoTrabajo":
                String profesion = jsonDoc.getString("profesionAutorizada");
                String empleador = jsonDoc.getString("empleador");
                return new PermisoTrabajo(numeroIdentificacion, paisEmisor, valido, fechaExpiracion, profesion, empleador);
            default:
                throw new Exception("Tipo de documento desconocido: " + tipo);
        }
    }
}