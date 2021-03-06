package com.chatapp.cdliii.mychatapp.Usuarios.Buscador;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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
    private Dialog dialog;

    public UserSearchAdapter(List<UserAttributes> attributesList, Context context, UserFragment fragment) {
        this.attributesList = attributesList;
        this.context = context;
        this.fragment = fragment;
    }

    @Override
    public UserSearchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_buscar_usuarios, parent, false);
        dialog = new Dialog(parent.getContext());
        return new UserSearchHolder(view);
    }

    public void mostrarPerfil(View view, String nombre, String telefono, String correo){
        TextView txtCerrar;
        TextView amigoNombre;
        TextView telefonoAmigo;
        TextView correoAmigo;
        dialog.setContentView(R.layout.perfil_amigo);
        txtCerrar = (TextView) dialog.findViewById(R.id.txtCerrarPerfil);
        amigoNombre = (TextView) dialog.findViewById(R.id.txtNombrePerfilAmigo);
        telefonoAmigo = (TextView) dialog.findViewById(R.id.txtTelefonoPerfilAmigo);
        correoAmigo = (TextView) dialog.findViewById(R.id.txtCorreoPerfilAmigo);
        amigoNombre.setText(nombre);
        telefonoAmigo.setText(telefono);
        correoAmigo.setText(correo);
        txtCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    @Override
    public void onBindViewHolder(UserSearchHolder holder, final int position) {
        holder.getFotoPerfil().setImageResource(attributesList.get(position).getFotoPerfil());
        holder.getNameUsuario().setText(attributesList.get(position).getNombre());
        final String nombre = attributesList.get(position).getNombre().toString();
        final String cor = attributesList.get(position).getCorreo().toString();
        final String tel = attributesList.get(position).getTelefono().toString();
        switch (attributesList.get(position).getEstado()){
            case 1: //No son amigos ni solicitud
                holder.getBotonCancelar().setVisibility(View.GONE);
                holder.getBotonAfirmar().setVisibility(View.VISIBLE);
                holder.getBotonAfirmar().setHint("Enviar solicitud");
                holder.getBotonAfirmar().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fragment.enviarSolicitud(attributesList.get(position).getId());
                    }
                });
                holder.getCardView().setOnLongClickListener(null);
                break;
            case 2: //Solicitud pendiente a que el usuario receptor acepte
                holder.getBotonCancelar().setVisibility(View.VISIBLE);
                holder.getBotonCancelar().setHint("Cancelar solicitud");
                holder.getBotonAfirmar().setVisibility(View.GONE);
                holder.getBotonCancelar().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fragment.cancelarSolicitud(attributesList.get(position).getId());
                    }
                });
                holder.getCardView().setOnLongClickListener(null);
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
                    }
                });
                holder.getBotonCancelar().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fragment.cancelarSolicitud(attributesList.get(position).getId());
                    }
                });
                holder.getCardView().setOnLongClickListener(null);
                break;
            case 4: //Son amigos
                holder.getBotonCancelar().setVisibility(View.GONE);
                holder.getBotonAfirmar().setVisibility(View.VISIBLE);
                holder.getBotonAfirmar().setHint("Ver perfil");
                holder.getBotonAfirmar().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mostrarPerfil(v, nombre, tel, cor);
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
                                        Toast.makeText(context, "Se eliminó de tu lista de amigos.", Toast.LENGTH_LONG).show();
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
        if(attributesList.get(position).getEstado()==1){
            holder.getEstadoUsuario().setText("No son amigos.");
        }else if(attributesList.get(position).getEstado()==2){
            holder.getEstadoUsuario().setText("Esperando respuesta.");
        }else if(attributesList.get(position).getEstado()==3){
            holder.getEstadoUsuario().setText("¡Nueva!");
        }else if(attributesList.get(position).getEstado()==4){
            holder.getEstadoUsuario().setText("Amigos.");
        }

        //holder.getEstadoUsuario().setText(""+attributesList.get(position).getEstado());
    }

    @Override
    public int getItemCount() {
        return attributesList.size();
    }
}
