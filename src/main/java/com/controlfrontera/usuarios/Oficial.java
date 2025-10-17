package com.controlfrontera.usuarios;

import com.controlfrontera.modelo.Decision;
import com.controlfrontera.modelo.Persona;
import com.controlfrontera.modelo.RegistroDecisiones;

import java.util.ArrayList;
import java.util.List;

public class Oficial extends Usuario {
    private transient List<Usuario> historial;
    private int puntuacion;
    private int erroresConsecutivos;
    private int totalAciertos;
    private int totalErrores;

    public Oficial() {
        super();
        this.historial = new ArrayList<>();
    }

    public Oficial(String nombre, String contrasenia, String rol, List<Usuario> historial) {
        super(nombre, contrasenia, rol);
        this.historial = new ArrayList<>();
    }

    public List<Usuario> getHistorial() {
        if (this.historial == null) {
            this.historial = new ArrayList<>();
        }
        return historial;
    }
    public void setHistorial(List<Usuario> historial) { this.historial = historial; }
    public int getPuntuacion() { return puntuacion; }
    public void setPuntuacion(int puntuacion) { this.puntuacion = puntuacion; }
    public int getTotalAciertos() { return totalAciertos; }
    public int getTotalErrores() { return totalErrores; }

    public void registrarAcierto() {
        this.puntuacion += 50;
        this.erroresConsecutivos = 0;
        this.totalAciertos++;
    }

    public void registrarError() {
        int penalizacion = 10 + (this.erroresConsecutivos * 5);
        this.puntuacion -= penalizacion;
        this.erroresConsecutivos++;
        this.totalErrores++;
    }

    public void registrarArrestoCorrecto() {
        this.puntuacion += 100;
        this.erroresConsecutivos = 0;
        this.totalAciertos++;
    }

    public void registrarArrestoIncorrecto() {
        int penalizacion = 50 + (this.erroresConsecutivos * 10);
        this.puntuacion -= penalizacion;
        this.erroresConsecutivos++;
        this.totalErrores++;
    }

    public void revisarPersona(Persona p){}
    public void registrarDecision(Decision d){ RegistroDecisiones.getInstancia().agregarDecision(d); }
    public void generarReporte(){}
    @Override public boolean login() { return super.login(); }
    @Override public void verMenu() { super.verMenu(); }

    @Override
    public String toString() {
        return this.getNombre() + " (Oficial)";
    }
}