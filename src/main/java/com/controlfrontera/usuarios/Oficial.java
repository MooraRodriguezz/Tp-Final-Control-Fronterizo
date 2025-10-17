package com.controlfrontera.usuarios;

import com.controlfrontera.modelo.Decision;
import com.controlfrontera.modelo.Persona;
import com.controlfrontera.modelo.RegistroDecisiones;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa a un oficial del sistema, que tiene la capacidad de revisar personas y registrar decisiones.
 * Ahora también gestiona una puntuación basada en su desempeño.
 */
public class Oficial extends Usuario {
    // Atributos
    // --- MODIFICADO ---
    // Añadimos 'transient' para que Gson ignore este campo al serializar
    private transient List<Usuario> historial;
    // ------------------

    private int puntuacion;
    private int erroresConsecutivos; // Para la penalización creciente

    // Nuevos atributos para estadísticas
    private int totalAciertos;
    private int totalErrores;


    // Constructores
    public Oficial() {
        super();
        this.historial = new ArrayList<>();
        this.puntuacion = 0;
        this.erroresConsecutivos = 0;
        this.totalAciertos = 0;
        this.totalErrores = 0;
    }

    public Oficial(String nombre, String contrasenia, String rol, List<Usuario> historial) {
        super(nombre, contrasenia, rol);
        this.historial = new ArrayList<>();
        this.puntuacion = 0;
        this.erroresConsecutivos = 0;
        this.totalAciertos = 0;
        this.totalErrores = 0;
    }

    // Getters y Setters
    public List<Usuario> getHistorial() {
        return historial;
    }

    public void setHistorial(List<Usuario> historial) {
        this.historial = historial;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    // --- Getters para Estadísticas ---
    public int getTotalAciertos() { return totalAciertos; }
    public int getTotalErrores() { return totalErrores; }

    // --- MÉTODOS PARA LA PUNTUACIÓN ---

    /**
     * Se llama cuando el oficial toma una decisión correcta.
     * Suma 50 puntos y resetea el contador de errores.
     */
    public void registrarAcierto() {
        this.puntuacion += 50;
        this.erroresConsecutivos = 0; // Se resetea el contador de errores
        this.totalAciertos++;
    }

    /**
     * Se llama cuando el oficial se equivoca.
     * La penalización es de 10 puntos y aumenta 5 puntos por cada error consecutivo.
     */
    public void registrarError() {
        int penalizacion = 10 + (this.erroresConsecutivos * 5);
        this.puntuacion -= penalizacion;
        this.erroresConsecutivos++;
        this.totalErrores++;
    }


    public void revisarPersona(Persona p){
    }

    public void registrarDecision(Decision d){
        // Las decisiones se registran a través del singleton RegistroDecisiones.
        RegistroDecisiones.getInstancia().agregarDecision(d);
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

    // Sobrescribimos toString() para que el ComboBox muestre el nombre
    @Override
    public String toString() {
        return this.getNombre() + " (Oficial)";
    }
}