package com.controlfrontera.usuarios;

import com.controlfrontera.modelo.Decision;
import com.controlfrontera.modelo.RegistroDecisiones;


public class Oficial extends Usuario {

    private int puntuacion;
    private int erroresConsecutivos;
    private int totalAciertos;
    private int totalErrores;

    public Oficial() {
        super();
    }

    public Oficial(String nombre, String contrasenia, String rol) {
        super(nombre, contrasenia, rol);
    }


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

    public void registrarDecision(Decision decision){
        RegistroDecisiones.getInstancia().agregarDecision(decision);
    }


    @Override
    public String toString() {
        return this.getNombre() + " (Oficial)";
    }
}