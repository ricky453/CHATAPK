package com.chatapp.cdliii.mychatapp.Usuarios.Amigos;

import com.chatapp.cdliii.mychatapp.Comunicacion.Usuario;

/**
 * Created by ricky on 06-17-18.
 */

public class FriendsAttributes extends Usuario {

    private String type_mensaje;

    public String getType_mensaje() {
        return type_mensaje;
    }

    public void setType_mensaje(String type_mensaje) {
        this.type_mensaje = type_mensaje;
    }

    public FriendsAttributes(String id, String nombre, String mensaje, String hora, int fotoPerfil, String type_mensaje) {
        super(id, nombre, mensaje, hora, fotoPerfil);
        this.type_mensaje = type_mensaje;
    }

    public FriendsAttributes() {
    }
}
