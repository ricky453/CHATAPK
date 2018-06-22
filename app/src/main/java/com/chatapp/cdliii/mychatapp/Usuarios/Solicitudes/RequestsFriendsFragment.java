package com.chatapp.cdliii.mychatapp.Usuarios.Solicitudes;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.chatapp.cdliii.mychatapp.Preferences;
import com.chatapp.cdliii.mychatapp.R;
import com.chatapp.cdliii.mychatapp.VolleyRP;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    private EventBus bus = EventBus.getDefault();

    //private static final String URL_GET_ALL_USERS = "https://cdliii-android.000webhostapp.com/ArchivosPHP/Amigos_GETALL.php?id=";

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
        //String usuario = Preferences.obtenerString(getContext(), Preferences.PREFERENCE_USUARIO_LOGIN);
        //SolicitudJSON(URL_GET_ALL_USERS+usuario);
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

    //nombres+apellidos, hora, id
    public void agregarTarjetasSolicitud(int fotoPerfil, String nombre, String hora, String id, int estado){
        Requests solicitudes = new Requests();
        solicitudes.setFotoPerfil(fotoPerfil);
        solicitudes.setNombre(nombre);
        solicitudes.setHora(hora);
        solicitudes.setId(id);
        solicitudes.setEstado(estado);
        requestsList.add(solicitudes);
        actualizarTarjetas();
    }

    public void agregarTarjetasSolicitud(Requests requests){
        requestsList.add(requests);
        actualizarTarjetas();
    }

    public void actualizarTarjetas(){
        adapter.notifyDataSetChanged();
        verificarSolicitudes();
    }

    @Override
    public void onPause() {
        super.onPause();
        bus.unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        bus.register(this);
    }

    @Subscribe
    public void ejecutarLlamada(Requests requests){
        agregarTarjetasSolicitud(requests);
    }

    public void SolicitudJSON(String URL){
        /*JsonObjectRequest solicitud = new JsonObjectRequest(URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String todosDatos = response.getString("resultado");
                    JSONArray jsonArray = new JSONArray(todosDatos);
                    for(int i = 0; i<jsonArray.length(); i++){
                        JSONObject jsonObject = new JSONObject(jsonArray.getString(i));
                        agregarTarjetasSolicitud(R.drawable.ic_account_circle, jsonObject.getString("Nombres")
                                + " "+jsonObject.getString("Apellidos"), jsonObject.getString("fecha_amigos"), null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "¡Ocurrió un error al descomponer el JSON!", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "¡Ocurrió un error!, por favor conéctese con el administrador.", Toast.LENGTH_SHORT).show();
            }
        });
        VolleyRP.addToQueue(solicitud, mRequest, getContext(), volley);*/
    }
}
