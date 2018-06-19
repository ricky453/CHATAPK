package com.chatapp.cdliii.mychatapp.Usuarios.Solicitudes;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.chatapp.cdliii.mychatapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ricky on 06-18-18.
 */

public class RequestsFriendsFragment extends Fragment {

    private RecyclerView rv;
    private RequestsAdapter adapter;
    private List<Requests> requestsList;
    private LinearLayout linearLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_solicitud_amistad, container, false);

        requestsList = new ArrayList<>();

        rv = (RecyclerView) view.findViewById(R.id.recyclerViewSolicitud);
        linearLayout = (LinearLayout) view.findViewById(R.id.layoutVacioSolicitud);
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(lm);
        adapter = new RequestsAdapter(requestsList, getContext());
        rv.setAdapter(adapter);

        //for(int i=0; i<10; i++){
          //  agregarTarjetasSolicitud(R.drawable.ic_account_circle, "Usuario "+i, "00:00");
        //}
        verificarSolicitudes();
        return view;
    }

    public void verificarSolicitudes(){
        if(requestsList.isEmpty()){
            linearLayout.setVisibility(View.VISIBLE);
            rv.setVisibility(View.GONE);
        }else{
            linearLayout.setVisibility(View.GONE);
            rv.setVisibility(View.VISIBLE);
        }
    }

    public void agregarTarjetasSolicitud(int fotoPerfil, String nombre, String hora){
        Requests solicitudes = new Requests();
        solicitudes.setFotoPerfil(fotoPerfil);
        solicitudes.setNombre(nombre);
        solicitudes.setHora(hora);
        requestsList.add(solicitudes);
        actualizarTarjetas();
    }

    public void actualizarTarjetas(){
        adapter.notifyDataSetChanged();
        verificarSolicitudes();
    }

}
