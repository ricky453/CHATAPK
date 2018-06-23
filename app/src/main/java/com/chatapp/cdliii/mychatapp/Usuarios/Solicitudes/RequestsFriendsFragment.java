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

import com.chatapp.cdliii.mychatapp.Internet.SolicitudesJSON;
import com.chatapp.cdliii.mychatapp.Preferences;
import com.chatapp.cdliii.mychatapp.R;
import com.chatapp.cdliii.mychatapp.Usuarios.Amigos.FriendsAttributes;
import com.chatapp.cdliii.mychatapp.Usuarios.Buscador.AcceptRequestFromSearcher;
import com.chatapp.cdliii.mychatapp.Usuarios.Buscador.DeleteRequestFromSearcher;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
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
        adapter = new RequestsAdapter(requestsList, getContext(), this);
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
        requestsList.add(0, solicitudes);
        actualizarTarjetas();
    }

    public void agregarTarjetasSolicitud(Requests requests){
        requestsList.add(0, requests);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ejecutarLlamada(Requests requests){
        agregarTarjetasSolicitud(requests);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void cancelarSolicitud(DeleteRequestFromRequests d){
        eliminarTarjeta(d.getId());
    }

    public void cancelarSolicitud(final String id){
        String usuarioEmisor = Preferences.obtenerString(getContext(), Preferences.PREFERENCE_USUARIO_LOGIN);
        final SolicitudesJSON json = new SolicitudesJSON() {
            @Override
            public void solicitudCompletada(JSONObject object) {
                try {
                    String respuesta = object.getString("respuesta");
                    if(respuesta.equals("200")){
                        //Se canceló correctamente
                        eliminarTarjeta(id);
                        bus.post(new DeleteRequestFromSearcher(id));
                        Toast.makeText(getContext(), object.getString("resultado"), Toast.LENGTH_SHORT).show();
                    }else if (respuesta.equals("-1")){
                        Toast.makeText(getContext(), "No se pudo cancelar la solicitud.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getContext(), "No se pudo cancelar la solicitud.", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void solicitudErronea() {

            }
        };
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("emisor", usuarioEmisor);
        hashMap.put("receptor", id);
        json.POST(getContext(), SolicitudesJSON.URL_DELETE_A_REQUEST, hashMap);
    }


    public void eliminarTarjeta(String id){
        for(int i=0; i<requestsList.size(); i++){
            if(requestsList.get(i).getId().equals(id)){
                requestsList.remove(i);
                actualizarTarjetas();
            }
        }
    }

    public void aceptarSolicitud(final String id){
        String usuarioEmisor = Preferences.obtenerString(getContext(), Preferences.PREFERENCE_USUARIO_LOGIN);
        SolicitudesJSON json = new SolicitudesJSON() {
            @Override
            public void solicitudCompletada(JSONObject object) {
                String respuesta = null;
                try {
                    respuesta = object.getString("respuesta");
                    if(respuesta.equals("200")){
                        //
                        bus.post(new AcceptRequestFromSearcher(id));
                        eliminarTarjeta(id);
                        FriendsAttributes friendsAttributes = new FriendsAttributes();
                        friendsAttributes.setId(id);
                        friendsAttributes.setNombre(object.getString("nombreCompleto"));
                        friendsAttributes.setMensaje(object.getString("UltimoMensaje"));
                        friendsAttributes.setFotoPerfil(R.drawable.ic_account_circle);
                        friendsAttributes.setHora(object.getString("hora"));
                        friendsAttributes.setType_mensaje(object.getString("type_mensaje"));
                        bus.post(friendsAttributes);
                        Toast.makeText(getContext(), "¡Ahora son amigos!", Toast.LENGTH_LONG).show();

                    }else if(respuesta.equals("-1")){
                        Toast.makeText(getContext(), "Error al enviar solicitud.", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getContext(), "Error al enviar solicitud.", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void solicitudErronea() {
                Toast.makeText(getContext(), "Ocurrió un error al enviar la solicitud de amistad.", Toast.LENGTH_LONG).show();
            }
        };
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("emisor", usuarioEmisor);
        hashMap.put("receptor", id);
        json.POST(getContext(), SolicitudesJSON.URL_ACCEPT_A_REQUEST, hashMap);
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
