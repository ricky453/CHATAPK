package com.chatapp.cdliii.mychatapp.Usuarios.Buscador;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chatapp.cdliii.mychatapp.R;

import java.util.List;

/**
 * Created by ricky on 06-18-18.
 */

public class UserSearchAdapter extends RecyclerView.Adapter<UserSearchHolder> {

    private List<UserAttributes> attributesList;
    private Context context;

    public UserSearchAdapter(List<UserAttributes> attributesList, Context context) {
        this.attributesList = attributesList;
        this.context = context;
    }

    @Override
    public UserSearchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_buscar_usuarios, parent, false);

        return new UserSearchHolder(view);
    }

    @Override
    public void onBindViewHolder(UserSearchHolder holder, int position) {
        holder.getFotoPerfil().setImageResource(attributesList.get(position).getFotoPerfil());
        holder.getNameUsuario().setText(attributesList.get(position).getNombre());
        holder.getEstadoUsuario().setText(attributesList.get(position).getEstadoUsuario());
    }

    @Override
    public int getItemCount() {
        return attributesList.size();
    }
}
