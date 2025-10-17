package com.controlfrontera.persistencia;

import com.controlfrontera.usuarios.Administrador;
import com.controlfrontera.usuarios.Oficial;
import com.controlfrontera.usuarios.Usuario;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *Clase que gestiona el guardado y carga de la LISTA COMPLETA de usuarios.
 * Separa a los usuarios por rol para poder serializarlos y desserializarlos correctamente
 * con Gson, ya que 'Usuario' es una clase abstracta.
 */
public class PersistenciaGestorUsuarios {
    private static final String OFICIALES_JSON = "oficiales.json";
    private static final String ADMINS_JSON = "admins.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Guarda la lista de usuarios en archivos JSON separados por rol.
     */
    // --- CORRECCIÓN: Nombre del método en minúscula ---
    public static void guardarUsuarios(ObservableList<Usuario>listaDeUsuarios){
        // -----------------------------------------------
        List<Oficial> oficiales = listaDeUsuarios.stream()
                .filter(u -> u instanceof Oficial)
                .map(u ->(Oficial) u)
                .collect(Collectors.toList());
        List<Administrador> administradores = listaDeUsuarios.stream()
                .filter(u -> u instanceof Administrador)
                .map(u ->(Administrador) u)
                .collect(Collectors.toList());

        // Guardamos los oficiales
        try(FileWriter writer = new FileWriter(OFICIALES_JSON)){
            gson.toJson(oficiales, writer);
        }catch(IOException e){
            System.err.println("Error al guardar oficiales: "+e.getMessage());
        }

        // Guardamos los administradores
        try(FileWriter writer = new FileWriter(ADMINS_JSON)){
            gson.toJson(administradores, writer);
        }catch(IOException e){
            System.err.println("Error al guardar administradores: "+e.getMessage());
        }
    }

    /**
     * Carga las listas de usuarios desde los JSON, las combina y las devuelve.
     */
    public static ObservableList<Usuario>cargarUsuarios(){
        ObservableList<Usuario>listaCombinada = FXCollections.observableArrayList();
        //Cargamos los oficiales
        try(FileReader reader = new FileReader(OFICIALES_JSON)){
            Type tipoListaOficial = new TypeToken <ArrayList<Oficial>>(){}.getType();
            List<Oficial> oficiales = gson.fromJson(reader, tipoListaOficial);
            if(oficiales != null){
                listaCombinada.addAll(oficiales);
            }
        }catch (IOException e){
            System.out.println("No se encontro un archivo de oficiales, se creara uno nuevo");
        }
        // Cargamos administradores
        try(FileReader reader = new FileReader(ADMINS_JSON)){
            Type tipoListaAdmin = new TypeToken <ArrayList<Administrador>>(){}.getType();
            List<Administrador> administradores = gson.fromJson(reader, tipoListaAdmin);
            if(administradores != null){
                listaCombinada.addAll(administradores);
            }
            // --- CORRECCIÓN: Mensaje de error para admins ---
        }catch (IOException e){
            System.out.println("No se encontro un archivo de administradores, se creara uno nuevo");
            // ---------------------------------------------
        }
        return listaCombinada;
    }
}