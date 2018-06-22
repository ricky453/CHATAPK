package com.chatapp.cdliii.mychatapp.Usuarios.Buscador;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chatapp.cdliii.mychatapp.R;

import java.util.List;

/**
 * Created by ricky on 06-18-18.
 */

public class UserSearchAdapter extends RecyclerView.Adapter<UserSearchHolder> {

    private List<UserAttributes> attributesList;
    private Context context;
    private UserFragment fragment;

    public UserSearchAdapter(List<UserAttributes> attributesList, Context context, UserFragment fragment) {
        this.attributesList = attributesList;
        this.context = context;
        this.fragment = fragment;
    }

    @Override
    public UserSearchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_buscar_usuarios, parent, false);

        return new UserSearchHolder(view);
    }

    @Override
    public void onBindViewHolder(UserSearchHolder holder, final int position) {
        holder.getFotoPerfil().setImageResource(attributesList.get(position).getFotoPerfil());
        holder.getNameUsuario().setText(attributesList.get(position).getNombre());
        switch (attributesList.get(position).getEstado()){
            case 1: //No son amigos ni solicitud
                holder.getBotonCancelar().setVisibility(View.GONE);
                holder.getBotonAfirmar().setVisibility(View.VISIBLE);
                holder.getBotonAfirmar().setHint("Enviar solicitud");
                holder.getBotonAfirmar().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "Enviando solicitud...", Toast.LENGTH_LONG).show();
                        fragment.enviarSolicitud(attributesList.get(position).getId());
                    }
                });
                break;
            case 2: //Solicitud pendiente a que el usuario receptor acepte
                holder.getBotonCancelar().setVisibility(View.VISIBLE);
                holder.getBotonCancelar().setHint("Cancelar solicitud");
                holder.getBotonAfirmar().setVisibility(View.GONE);
                holder.getBotonCancelar().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fragment.cancelarSolicitud(attributesList.get(position).getId());
                        Toast.makeText(context, "Cancelando solicitud...", Toast.LENGTH_LONG).show();
                    }
                });
                break;
            case 3: //Solicitud pendiente a aceptar
                holder.getBotonAfirmar().setVisibility(View.VISIBLE);
                holder.getBotonCancelar().setVisibility(View.VISIBLE);
                holder.getBotonAfirmar().setHint("Aceptar");
                holder.getBotonCancelar().setHint("Declinar");
                holder.getBotonAfirmar().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fragment.aceptarSolicitud(attributesList.get(position).getId());
                        Toast.makeText(context, "Aceptando solicitud...", Toast.LENGTH_LONG).show();
                    }
                });
                holder.getBotonCancelar().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fragment.cancelarSolicitud(attributesList.get(position).getId());
                        Toast.makeText(context, "Declinando solicitud...", Toast.LENGTH_LONG).show();
                    }
                });
                break;
            case 4: //Son amigos
                holder.getBotonCancelar().setVisibility(View.GONE);
                holder.getBotonAfirmar().setVisibility(View.VISIBLE);
                holder.getBotonAfirmar().setHint("Ver perfil");
                holder.getBotonAfirmar().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "Viendo perfil...", Toast.LENGTH_LONG).show();
                    }
                });
                holder.getCardView().setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        new AlertDialog.Builder(context).
                                setMessage("Estás a punto de eliminar a este amigo, ¿Deseas eliminarlo?").
                                setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        fragment.eliminarAmigo(attributesList.get(position).getId());
                                        Toast.makeText(context, "Se eliminó el amigo.", Toast.LENGTH_LONG).show();
                                    }
                                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).show();
                        return true;
                    }
                });
                break;
        }

        holder.getEstadoUsuario().setText(""+attributesList.get(position).getEstado());
    }

    @Override
    public int getItemCount() {
        return attributesList.size();
    }
}
