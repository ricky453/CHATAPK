package com.chatapp.cdliii.mychatapp.Usuarios.Amigos;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chatapp.cdliii.mychatapp.Mensajes.MsgActivity;
import com.chatapp.cdliii.mychatapp.Preferences;
import com.chatapp.cdliii.mychatapp.R;

import java.util.List;

/**
 * Created by ricky on 06-17-18.
 */

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.HolderFriends> {

    private List<FriendsAttributes> atributosList;
    private Context context;
    private FriendsFragment fragment;

    public FriendsAdapter(List<FriendsAttributes> attributesList, Context context, FriendsFragment fragment){
        this.atributosList = attributesList;
        this.context = context;
        this.fragment = fragment;
    }

    @Override
    public HolderFriends onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_amigos, parent,false);
        return new FriendsAdapter.HolderFriends(view);
    }

    @Override
    public void onBindViewHolder(HolderFriends holder, final int position) {
        holder.imageView.setImageResource(R.drawable.ic_account_circle);
        holder.nombre.setText(atributosList.get(position).getNombre());
        holder.mensaje.setText(atributosList.get(position).getMensaje());
        holder.hora.setText(atributosList.get(position).getHora());



        if (atributosList.get(position).getMensaje()=="null"){
            holder.hora.setVisibility(View.GONE);
            holder.mensaje.setText("¡Salúdame!");
        }else {
            holder.hora.setVisibility(View.VISIBLE);
            if(atributosList.get(position).getType_mensaje().equals("1")){
                //holder.mensaje.setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray));
            }else{
                holder.mensaje.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
            }
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, MsgActivity.class);
                i.putExtra("key_receptor", atributosList.get(position).getId());
                Preferences.savePreferenceString(v.getContext(), atributosList.get(position).getNombre(), Preferences.PREFERENCE_USUARIO_CHAT);
                context.startActivity(i);
            }
        });

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(context).
                        setMessage("Estás a punto de eliminar a este amigo, ¿Deseas eliminarlo?").
                        setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                fragment.eliminarAmigo(atributosList.get(position).getId());
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
    }

    @Override
    public int getItemCount() {
        return atributosList.size();
    }

    static class HolderFriends extends RecyclerView.ViewHolder{

        CardView cardView;
        ImageView imageView;
        TextView nombre;
        TextView mensaje;
        TextView hora;

        public HolderFriends(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardViewAmigos);
            imageView = (ImageView) itemView.findViewById(R.id.fotoPerfilAmigos);
            nombre = (TextView) itemView.findViewById(R.id.nombreUsuarioAmigo);
            mensaje = (TextView) itemView.findViewById(R.id.mensajeAmigo);
            hora = (TextView) itemView.findViewById(R.id.horaAmigo);
        }
    }
}
