package com.chatapp.cdliii.mychatapp.Mensajes;

/**
 * Created by ricky on 06-04-18.
 */

public class TextMsg {
    private String id;
    private String msg;
    private int tipoMensaje;
    private String hora;

    public TextMsg(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getTipoMensaje() {
        return tipoMensaje;
    }

    public void setTipoMensaje(int tipoMensaje) {
        this.tipoMensaje = tipoMensaje;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
}
