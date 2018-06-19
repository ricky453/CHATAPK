package com.chatapp.cdliii.mychatapp.Usuarios.Buscador;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chatapp.cdliii.mychatapp.R;

/**
 * Created by ricky on 06-18-18.
 */

public class UserSearchHolder extends RecyclerView.ViewHolder{

    private ImageView fotoPerfil;
    private TextView nameUsuario;
    private TextView estadoUsuario;
    private Button enviarSolicitud;

    public UserSearchHolder(View itemView) {
        super(itemView);
        fotoPerfil = (ImageView) itemView.findViewById(R.id.fotoPerfilEnviarSolicitud);
        nameUsuario = (TextView) itemView.findViewById(R.id.nombreUsuarioEnviarSolicitud);
        estadoUsuario = (TextView) itemView.findViewById(R.id.estadoUsuario);
        enviarSolicitud = (Button) itemView.findViewById(R.id.enviarSolicitud);
    }

    public ImageView getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(ImageView fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public TextView getNameUsuario() {
        return nameUsuario;
    }

    public void setNameUsuario(TextView nameUsuario) {
        this.nameUsuario = nameUsuario;
    }

    public TextView getEstadoUsuario() {
        return estadoUsuario;
    }

    public void setEstadoUsuario(TextView estadoUsuario) {
        this.estadoUsuario = estadoUsuario;
    }

    public Button getEnviarSolicitud() {
        return enviarSolicitud;
    }

    public void setEnviarSolicitud(Button enviarSolicitud) {
        this.enviarSolicitud = enviarSolicitud;
    }
}
