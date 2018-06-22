package com.chatapp.cdliii.mychatapp.Usuarios.Amigos;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chatapp.cdliii.mychatapp.Mensajes.MsgActivity;
import com.chatapp.cdliii.mychatapp.R;

import java.util.List;

/**
 * Created by ricky on 06-17-18.
 */

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.HolderFriends> {

    private List<FriendsAttributes> atributosList;
    private Context context;
    public FriendsAdapter(List<FriendsAttributes> attributesList, Context context){
        this.atributosList = attributesList;
        this.context = context;
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

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, MsgActivity.class);
                i.putExtra("key_receptor", atributosList.get(position).getId());
                context.startActivity(i);
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
