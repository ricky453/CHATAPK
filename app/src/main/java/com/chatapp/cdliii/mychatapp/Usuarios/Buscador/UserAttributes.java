package com.chatapp.cdliii.mychatapp.Usuarios.Buscador;

/**
 * Created by ricky on 06-18-18.
 */

public class UserAttributes {

    private int fotoPerfil;
    private String nombre;
    private String estadoUsuario;
    private String id;

    public UserAttributes(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getEstadoUsuario() {
        return estadoUsuario;
    }

    public void setEstadoUsuario(String estadoUsuario) {
        this.estadoUsuario = estadoUsuario;
    }
}
