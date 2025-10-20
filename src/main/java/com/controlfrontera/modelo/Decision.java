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

    // MARCA ESTOS OBJETOS COMO 'transient' para que Gson (JSON) los ignore al guardar
    // Esta es la corrección para que el programa no crashee y pase a la siguiente persona
    private transient Persona persona;
    private transient Oficial oficial;

    // Campos simples que SÍ se guardan en el JSON
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

        // Asigna los nuevos campos simples en el constructor
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

    // --- ¡ACÁ ESTÁ EL MÉTODO QUE BUSCABAS! ---
    public boolean isAprobada() {
        return aprobada;
    }
    // --- --- --- --- --- --- --- --- --- ---

    public void setAprobada(boolean aprobada) {
        this.aprobada = aprobada;
    }

    public Oficial getOficial() {
        return oficial;
    }

    public void setOficial(Oficial oficial) {
        this.oficial = oficial;
    }

    // Getters para los nuevos campos (los que se guardan en JSON)
    public String getNombrePersona() {
        return nombrePersona;
    }

    public String getIdPersona() {
        return idPersona;
    }

    public String getNombreOficial() {
        return nombreOficial;
    }

    // Metodos
    public void mostrarDecision(){
    }
}