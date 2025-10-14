package com.controlfrontera.modelo;

import com.controlfrontera.usuarios.Oficial;

import java.time.LocalDateTime;
/**
 * Representa una decisión tomada por un oficial de control fronterizo sobre la entrada de una persona.
 * Esta clase mantiene registro de las decisiones de aprobación o rechazo, incluyendo la persona evaluada,
 * el oficial que tomó la decisión, el motivo y la fecha.
 */
public class Decision {
    /// Atributos
    private Persona persona;
    private Oficial oficial;
    private boolean aprobada;
    private String motivo;
    private LocalDateTime fecha;

    ///Constructores
    public Decision() {
    }

    public Decision(Persona persona, Oficial oficial, boolean aprobada, String motivo, LocalDateTime fecha) {
        this.persona = persona;
        this.oficial = oficial;
        this.aprobada = aprobada;
        this.motivo = motivo;
        this.fecha = fecha;
    }
    /// Getters and Setters
    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public boolean isAprobada() {
        return aprobada;
    }

    public void setAprobada(boolean aprobada) {
        this.aprobada = aprobada;
    }

    public Oficial getOficial() {
        return oficial;
    }

    public void setOficial(Oficial oficial) {
        this.oficial = oficial;
    }
    /// Metodos
    public void mostrarDecision(){
    }
}
