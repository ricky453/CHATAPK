package com.chatapp.cdliii.mychatapp.Usuarios.Buscador;

import android.support.v7.widget.CardView;
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
    private Button botonCancelar;
    private Button botonAfirmar;
    private CardView cardView;

    public UserSearchHolder(View itemView) {
        super(itemView);
        cardView = (CardView) itemView.findViewById(R.id.cardViewEnviarSolicitudAmistad);
        fotoPerfil = (ImageView) itemView.findViewById(R.id.fotoPerfilEnviarSolicitud);
        nameUsuario = (TextView) itemView.findViewById(R.id.nombreUsuarioEnviarSolicitud);
        estadoUsuario = (TextView) itemView.findViewById(R.id.estadoUsuario);
        botonCancelar = (Button) itemView.findViewById(R.id.botonCancelar);
        botonAfirmar = (Button) itemView.findViewById(R.id.botonEstado3);
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

    public Button getBotonCancelar() {
        return botonCancelar;
    }

    public void setBotonCancelar(Button botonCancelar) {
        this.botonCancelar = botonCancelar;
    }

    public Button getBotonAfirmar() {
        return botonAfirmar;
    }

    public void setBotonAfirmar(Button botonAfirmar) {
        this.botonAfirmar = botonAfirmar;
    }

    public CardView getCardView() {
        return cardView;
    }

    public void setCardView(CardView cardView) {
        this.cardView = cardView;
    }
}
