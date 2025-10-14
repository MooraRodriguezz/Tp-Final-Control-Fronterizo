package com.controlfrontera.modelo;

import com.controlfrontera.usuarios.Oficial;

import java.time.LocalDateTime;

public class Decision {
    private Persona persona;
    private Oficial oficial;
    private boolean aprobada;
    private String motivo;
    private LocalDateTime fecha;

    //constructores


    public Decision() {
    }

    public Decision(Persona persona, Oficial oficial, boolean aprobada, String motivo, LocalDateTime fecha) {
        this.persona = persona;
        this.oficial = oficial;
        this.aprobada = aprobada;
        this.motivo = motivo;
        this.fecha = fecha;
    }

    public void mostrarDecision(){

    }
}
