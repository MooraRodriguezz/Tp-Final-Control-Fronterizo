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

    // --- INICIO DE LA CORRECCIÓN ---
    // Se marcan como 'transient' para que GSON (la librería JSON) los ignore.
    // Esto evita el crash al guardar, ya que Persona y Oficial son objetos complejos.
    private transient Persona persona;
    private transient Oficial oficial;

    // Se añaden campos simples para guardar la información clave que SÍ queremos en el JSON
    private String nombrePersona;
    private String idPersona;
    private String nombreOficial;
    // --- FIN DE LA CORRECCIÓN ---

    private boolean aprobada;
    private String motivo;
    private LocalDateTime fecha;

    //Constructores
    public Decision() {
    }

    public Decision(Persona persona, Oficial oficial, boolean aprobada, String motivo, LocalDateTime fecha) {
        this.persona = persona; // Se mantiene para uso en memoria
        this.oficial = oficial; // Se mantiene para uso en memoria

        // --- INICIO DE LA CORRECCIÓN ---
        // Asignamos los valores a los nuevos campos simples
        if (persona != null) {
            this.nombrePersona = persona.getNombre();
            this.idPersona = persona.getId();
        }
        if (oficial != null) {
            this.nombreOficial = oficial.getNombre();
        }
        // --- FIN DE LA CORRECCIÓN ---

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

    // --- INICIO DE LA CORRECCIÓN ---
    // Getters para los nuevos campos
    public String getNombrePersona() {
        return nombrePersona;
    }

    public String getIdPersona() {
        return idPersona;
    }

    public String getNombreOficial() {
        return nombreOficial;
    }
    // --- FIN DE LA CORRECCIÓN ---

    // Metodos
    public void mostrarDecision(){
    }
}