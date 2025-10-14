package com.controlfrontera.usuarios;

import com.controlfrontera.modelo.Persona;
import com.controlfrontera.modelo.Decision;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa a un oficial del sistema, que tiene la capacidad de revisar personas y registrar decisiones.
 */
public class Oficial extends Usuario {
    /// Atributos
    List<Usuario> historial;
    ///Constructores
    public Oficial() {
    }

    public Oficial(String nombre, String contrasenia, String rol, List<Usuario> historial) {
        super(nombre, contrasenia, rol);
        this.historial = new ArrayList<>();
    }
    /// Getters and Setters
    public List<Usuario> getHistorial() {
        return historial;
    }

    public void setHistorial(List<Usuario> historial) {
        this.historial = historial;
    }
    /// Metodos
    public void revisarPersona(Persona p){

    }
    public void registrarDecision(Decision d){

    }

    public void generarReporte(){

    }

    @Override
    public boolean login() {
        return super.login();
    }

    @Override
    public void verMenu() {
        super.verMenu();
    }


}
