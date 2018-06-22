package com.chatapp.cdliii.mychatapp.Usuarios.Solicitudes;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chatapp.cdliii.mychatapp.R;

import java.util.List;

/**
 * Created by ricky on 06-18-18.
 */

public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.RequestsHolder> {

    private Context context;
    private List<Requests> requestsList;

    public RequestsAdapter(List<Requests> listSolicitudes, Context context){
        this.context = context;
        this.requestsList = listSolicitudes;
    }

    @Override
    public RequestsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_solicitudes_amistad,parent, false);

        return new RequestsAdapter.RequestsHolder(v);
    }

    @Override
    public void onBindViewHolder(RequestsHolder holder, int position) {
        holder.fotoPerfil.setImageResource(requestsList.get(position).getFotoPerfil());
        holder.nombre.setText(requestsList.get(position).getNombre());
        holder.hora.setText(requestsList.get(position).getHora());

        switch (requestsList.get(position).getEstado()){
            case 2:
                holder.declinar.setHint("Cancelar solicitud");
                holder.aceptar.setVisibility(View.GONE);
                break;
            case 3:
                holder.declinar.setHint("Declinar");
                holder.aceptar.setVisibility(View.VISIBLE);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return requestsList.size();
    }

    static class RequestsHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView fotoPerfil;
        TextView nombre;
        TextView hora;
        Button aceptar;
        Button declinar;

        public RequestsHolder(View itemView) {
            super(itemView);
            fotoPerfil = (ImageView) itemView.findViewById(R.id.fotoPerfilSolicitud);
            nombre = (TextView) itemView.findViewById(R.id.nombreUsuarioSolicitud);
            hora = (TextView) itemView.findViewById(R.id.horaSolicitud);
            aceptar = (Button) itemView.findViewById(R.id.aceptarSolicitud);
            declinar = (Button) itemView.findViewById(R.id.cancelarSolicitud);
        }
    }
}
