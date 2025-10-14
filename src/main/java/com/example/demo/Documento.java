package com.example.demo;

import java.util.Date;

public class Documento implements Verificable{
    private String tipo;
    private boolean valido;
    private String motivoViaje;
    private Date fechaExpiracion;

    //constructores

    public Documento() {
    }

    public Documento(String tipo, boolean valido, String motivoViaje, Date fechaExpiracion) {
        this.tipo = tipo;
        this.valido = valido;
        this.motivoViaje = motivoViaje;
        this.fechaExpiracion = fechaExpiracion;
    }

    @Override
    public boolean validar() {
        return false;
    }
}
