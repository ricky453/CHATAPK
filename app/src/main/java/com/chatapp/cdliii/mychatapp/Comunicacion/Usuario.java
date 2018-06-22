package com.chatapp.cdliii.mychatapp.Comunicacion;

/**
 * Created by ricky on 06-19-18.
 */

public class Usuario {

    private String id;
    private String nombre;
    private int estado;
    private String mensaje;
    private String hora;
    private int fotoPerfil;

    public Usuario(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora_del_mensaje) {
        this.hora = hora_del_mensaje;
    }

    public int getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(int fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }
}
