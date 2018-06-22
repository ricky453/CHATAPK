package com.chatapp.cdliii.mychatapp.Usuarios.Amigos;

import android.os.Bundle;
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
 * Created by ricky on 06-02-18.
 */

public class FriendsFragment extends android.support.v4.app.Fragment {

    private RecyclerView rv;
    private List<FriendsAttributes> attributesList;
    private FriendsAdapter adapter;
    private LinearLayout linearLayout;

    private EventBus bus = EventBus.getDefault();

    //private static final String URL_GET_ALL_USERS = "https://cdliii-android.000webhostapp.com/ArchivosPHP/Usuarios_GETALL.php";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_amigos, container, false);
        attributesList = new ArrayList<>();

        //Friends
        rv = (RecyclerView) v.findViewById(R.id.amigosRecycler);
        linearLayout = (LinearLayout) v.findViewById(R.id.layoutVacioAmigos);
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        //lm.setStackFromEnd(true);
        rv.setLayoutManager(lm);

        adapter = new FriendsAdapter(attributesList, getContext());
        rv.setAdapter(adapter);

        verificarAmigos();
        //SolicitudJSON();
        return v;
    }
    public void verificarAmigos(){
        if(attributesList.isEmpty()){
            linearLayout.setVisibility(View.VISIBLE);
            rv.setVisibility(View.GONE);
        }else{
            linearLayout.setVisibility(View.GONE);
            rv.setVisibility(View.VISIBLE);
        }
    }

    //id, Nombres+Apellidos, ultimo_mensaje, hora
    public void agregarAmigos(int fotoPerfil, String nombre, String ultimoMensaje, String hora, String id){
        FriendsAttributes friendsAttributes = new FriendsAttributes();
        friendsAttributes.setFotoPerfil(fotoPerfil);
        friendsAttributes.setNombre(nombre);
        friendsAttributes.setMensaje(ultimoMensaje);
        friendsAttributes.setHora(hora);
        friendsAttributes.setId(id);
        attributesList.add(friendsAttributes);
        adapter.notifyDataSetChanged();
        verificarAmigos();
    }

    public void agregarAmigos(FriendsAttributes attributes){
        attributesList.add(attributes);
        adapter.notifyDataSetChanged();
        verificarAmigos();
    }

    public void SolicitudJSON(){
        /*JsonObjectRequest solicitud = new JsonObjectRequest(URL_GET_ALL_USERS, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String todosDatos = response.getString("resultado");
                    JSONArray jsonArray = new JSONArray(todosDatos);
                    String miUsuario = Preferences.obtenerString(getContext(), Preferences.PREFERENCE_USUARIO_LOGIN);
                    for(int i=0; i<jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        if(!miUsuario.equals(jsonObject.getString("idUsuario"))){
                            agregarAmigos(R.drawable.ic_account_circle, jsonObject.getString("Nombres")+" "+jsonObject.getString("Apellidos"), "Te amo pendejo x"+i, "00:00", jsonObject.getString("idUsuario"));
                        }
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

    @Subscribe
    public void ejecutarLlamada(FriendsAttributes attributes){
        agregarAmigos(attributes);
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
}
