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
    private static final SimpleDateFormat aJsonFecha = new SimpleDateFormat("yyyy-MM-dd");
    private static final int INDENT_FACTOR = 4;

    /**
     * Guarda la lista completa de personas en el archivo personas.json.
     * @param personas La colección de personas a guardar.
     */
    public static void guardarPersonas(java.util.Collection<Persona> personas) {
        JSONArray jsonArray = new JSONArray();
        for (Persona p : personas) {
            jsonArray.put(personaToJSON(p));
        }

        try (java.io.FileWriter writer = new java.io.FileWriter(RUTA_ARCHIVO)) {
            writer.write(jsonArray.toString(INDENT_FACTOR));
        } catch (java.io.IOException e) {
            System.err.println("Error al guardar 'personas.json': " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Convierte un objeto Persona en un JSONObject.
     */
    private static JSONObject personaToJSON(Persona p) {
        JSONObject jsonPersona = new JSONObject();
        jsonPersona.put("id", p.getId());
        jsonPersona.put("nombre", p.getNombre());
        jsonPersona.put("nacionalidad", p.getNacionalidad());
        jsonPersona.put("fechaNacimiento", aJsonFecha.format(p.getFechaNacimiento()));
        jsonPersona.put("altura", p.getAltura());
        jsonPersona.put("peso", p.getPeso());
        jsonPersona.put("nombreImagen", p.getNombreImagen());
        jsonPersona.put("sospechosa", p.isTieneContrabando()); // Ojo: esto guarda el estado actual, no el 'sospechosa' original

        JSONArray jsonDocumentos = new JSONArray();
        for (Documento d : p.getDocumentos()) {
            jsonDocumentos.put(documentoToJSON(d));
        }
        jsonPersona.put("documentos", jsonDocumentos);

        return jsonPersona;
    }

    /**
     * Convierte un objeto Documento (y sus subclases) en un JSONObject.
     */
    private static JSONObject documentoToJSON(Documento doc) {
        JSONObject jsonDoc = new JSONObject();
        jsonDoc.put("tipo", doc.getTipo());
        jsonDoc.put("numeroIdentificacion", doc.getNumeroIdentificacion());
        jsonDoc.put("paisEmisor", doc.getPaisEmisor());
        jsonDoc.put("valido", doc.isValido());
        jsonDoc.put("fechaExpiracion", aJsonFecha.format(doc.getFechaExpiracion()));


        if (doc instanceof PermisoEntrada) {
            jsonDoc.put("motivoViaje", doc.getMotivoViaje());
        } else if (doc instanceof PermisoTrabajo) {
            PermisoTrabajo pt = (PermisoTrabajo) doc;
            jsonDoc.put("profesionAutorizada", pt.getProfesionAutorizada());
            jsonDoc.put("empleador", pt.getEmpleador());
        }


        return jsonDoc;
    }
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