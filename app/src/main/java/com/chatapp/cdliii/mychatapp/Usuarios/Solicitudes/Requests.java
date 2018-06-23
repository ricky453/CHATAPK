package com.chatapp.cdliii.mychatapp.Usuarios.Solicitudes;

import com.chatapp.cdliii.mychatapp.Comunicacion.Usuario;

/**
 * Created by ricky on 06-18-18.
 */

public class Requests extends Usuario{

    public Requests() {}

    public Requests(String id, String nombre, int estado, String hora, int fotoPerfil) {
        super(id, nombre, estado, hora, fotoPerfil);
    }
}
