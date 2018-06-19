package com.chatapp.cdliii.mychatapp.Usuarios.Solicitudes;

import android.widget.Button;

/**
 * Created by ricky on 06-18-18.
 */

public class Requests {

    private int fotoPerfil;
    private String nombre;
    private String hora;

    public Requests(){}

    public int getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(int fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
}
