package com.controlfrontera.usuarios;

import com.controlfrontera.modelo.Persona;
import com.controlfrontera.modelo.Decision;

import java.util.ArrayList;
import java.util.List;

public class Oficial extends Usuario {
    List<Usuario> historial;

    //constructores


    public Oficial() {
    }

    public Oficial(String nombre, String contrasenia, String rol, List<Usuario> historial) {
        super(nombre, contrasenia, rol);
        this.historial = new ArrayList<>();
    }

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
