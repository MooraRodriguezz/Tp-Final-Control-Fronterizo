package com.controlfrontera.modelo;

import com.controlfrontera.usuarios.Oficial;

import java.time.LocalDateTime;

/**
 * Representa una decisión tomada por un oficial de control fronterizo sobre la entrada de una persona.
 * Esta clase mantiene registro de las decisiones de aprobación o rechazo, incluyendo la persona evaluada,
 * el oficial que tomó la decisión, el motivo y la fecha.
 */
public class Decision {

    // Atributos

    private transient Persona persona;
    private transient Oficial oficial;

    private String nombrePersona;
    private String idPersona;
    private String nombreOficial;

    private boolean aprobada;
    private String motivo;
    private LocalDateTime fecha;

    //Constructores
    public Decision() {
    }

    public Decision(Persona persona, Oficial oficial, boolean aprobada, String motivo, LocalDateTime fecha) {
        this.persona = persona;
        this.oficial = oficial;

        if (persona != null) {
            this.nombrePersona = persona.getNombre();
            this.idPersona = persona.getId();
        }
        if (oficial != null) {
            this.nombreOficial = oficial.getNombre();
        }

        this.aprobada = aprobada;
        this.motivo = motivo;
        this.fecha = fecha;
    }

    // --- NUEVO CONSTRUCTOR REQUERIDO PARA PERSISTENCIA JSON ---
    /**
     * Constructor para deserialización manual desde JSON.
     * Los campos 'persona' y 'oficial' (transient) permanecen null.
     */
    public Decision(String nombrePersona, String idPersona, String nombreOficial, boolean aprobada, String motivo, LocalDateTime fecha) {
        this.nombrePersona = nombrePersona;
        this.idPersona = idPersona;
        this.nombreOficial = nombreOficial;
        this.aprobada = aprobada;
        this.motivo = motivo;
        this.fecha = fecha;
        this.persona = null; // transient
        this.oficial = null; // transient
    }
    // --- FIN DEL NUEVO CONSTRUCTOR ---


    // Getters and Setters
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

    public String getNombrePersona() {
        return nombrePersona;
    }

    public String getIdPersona() {
        return idPersona;
    }

    public String getNombreOficial() {
        return nombreOficial;
    }


}