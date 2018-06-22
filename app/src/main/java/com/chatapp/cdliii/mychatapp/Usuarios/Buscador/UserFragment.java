package com.chatapp.cdliii.mychatapp.Usuarios.Buscador;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.chatapp.cdliii.mychatapp.Comunicacion.Usuario;
import com.chatapp.cdliii.mychatapp.Internet.SolicitudesJSON;
import com.chatapp.cdliii.mychatapp.Preferences;
import com.chatapp.cdliii.mychatapp.R;
import com.chatapp.cdliii.mychatapp.Usuarios.Amigos.FriendsAttributes;
import com.chatapp.cdliii.mychatapp.Usuarios.Solicitudes.DeleteRequestFromRequests;
import com.chatapp.cdliii.mychatapp.Usuarios.Solicitudes.Requests;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ricky on 06-18-18.
 */

public class UserFragment extends android.support.v4.app.Fragment{

    private List<UserAttributes> attributesList; //Conectada con nuestro adaptador
    private List<UserAttributes> listAuxiliar; //Lista con todos los elementos estaticos
    private RecyclerView rv;
    private UserSearchAdapter adapter;
    private EditText search;
    private LinearLayout linearLayout;

    //private static final String URL_GET_ALL_USERS = "https://cdliii-android.000webhostapp.com/ArchivosPHP/Usuarios_GETALL.php";

    private EventBus bus = EventBus.getDefault();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_buscar_usuarios, container, false);

        attributesList = new ArrayList<>();
        listAuxiliar = new ArrayList<>();

        rv = (RecyclerView) v.findViewById(R.id.recyclerViewUsuarios);
        search = (EditText) v.findViewById(R.id.searchUsuarios);
        linearLayout = (LinearLayout) v.findViewById(R.id.layoutVacioBuscarUsuarios);
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(lm);
        adapter = new UserSearchAdapter(attributesList, getContext(), this);
        rv.setAdapter(adapter);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                buscador(""+s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        verificarUsuarios();

        return v;
    }
    public void verificarUsuarios(){
        if(attributesList.isEmpty()){
            linearLayout.setVisibility(View.VISIBLE);
            rv.setVisibility(View.GONE);
        }else{
            linearLayout.setVisibility(View.GONE);
            rv.setVisibility(View.VISIBLE);
        }
    }

    //id, estado, nombre
    public void insertarUsuarios(int fotoPerfil, String nombre, int estado, String id){
        UserAttributes userAttributes = new UserAttributes();
        userAttributes.setFotoPerfil(fotoPerfil);
        userAttributes.setNombre(nombre);
        userAttributes.setEstado(estado);
        userAttributes.setId(id);
        attributesList.add(userAttributes);
        listAuxiliar.add(userAttributes);
        adapter.notifyDataSetChanged();
        verificarUsuarios();
    }

    public void insertarUsuarios(UserAttributes attributes){
        attributesList.add(attributes);
        listAuxiliar.add(attributes);
        adapter.notifyDataSetChanged();
        verificarUsuarios();
    }

    public void buscador(String texto){
        attributesList.clear();
        for(int i=0; i<listAuxiliar.size(); i++){
            if(listAuxiliar.get(i).getNombre().toLowerCase().contains(texto.toLowerCase())){
                attributesList.add(listAuxiliar.get(i));
            }
        }
        adapter.notifyDataSetChanged();
        verificarUsuarios();
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
                            insertarUsuarios(R.drawable.ic_account_circle, jsonObject.getString("Nombres")+" "+jsonObject.getString("Apellidos"), 1, jsonObject.getString("idUsuario"));
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
    public void ejecutarLlamada(UserAttributes attributes){
        insertarUsuarios(attributes);
    }

    @Subscribe
    public void cancelarSolicitud(DeleteRequestFromSearcher deleteRequestFromSearcher){
        cambiarEstado(deleteRequestFromSearcher.getId(), 1);
    }

    @Subscribe
    public void aceptarSolicitud(AcceptRequestFromSearcher acceptRequestFromSearcher){
        cambiarEstado(acceptRequestFromSearcher.getId(), 4);
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

    public void enviarSolicitud(final String id){

        String usuarioEmisor = Preferences.obtenerString(getContext(), Preferences.PREFERENCE_USUARIO_LOGIN);
        SolicitudesJSON json = new SolicitudesJSON() {
            @Override
            public void solicitudCompletada(JSONObject object) {
                String respuesta = null;
                try {
                    respuesta = object.getString("respuesta");
                    if(respuesta.equals("200")){
                        //correcto
                        Toast.makeText(getContext(), "Se envió correctamente.", Toast.LENGTH_LONG).show();
                        int estado = object.getInt("estado");
                        String nombreCompleto = object.getString("nombreCompleto");
                        String hora = object.getString("hora");

                        Requests requests = new Requests();
                        requests.setId(id);
                        requests.setEstado(estado);
                        requests.setNombre(nombreCompleto);
                        requests.setHora(hora);
                        requests.setFotoPerfil(R.drawable.ic_account_circle);

                        bus.post(requests);
                        cambiarEstado(id, 2);
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
        json.POST(getContext(), SolicitudesJSON.URL_SEND_REQUESTS, hashMap);
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
                        bus.post(new DeleteRequestFromRequests(id));
                        cambiarEstado(id, 1);
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
                        cambiarEstado(id, 4);
                        bus.post(new DeleteRequestFromRequests(id));
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

    public void eliminarAmigo(String id){
        cambiarEstado(id, 1);
    }

    private void cambiarEstado(String id, int estado){
        for(int i = 0; i< listAuxiliar.size(); i++){
            if(listAuxiliar.get(i).getId().equals(id)){
                listAuxiliar.get(i).setEstado(estado);
            }
        }
        int posicionUsuario = -1;
        for(int i = 0; i< attributesList.size(); i++){
            if(attributesList.get(i).getId().equals(id)){
                attributesList.get(i).setEstado(estado);
                posicionUsuario = i;
                break;
            }
        }
        if(posicionUsuario!=-1){
            adapter.notifyItemChanged(posicionUsuario);
        }else {
            Toast.makeText(getContext(), "No se pudo cambiar el usuario", Toast.LENGTH_LONG).show();
        }
    }

    private Usuario obtenerUsuarioPorId(String id){
        for(int i = 0; i<attributesList.size(); i++){
            if(attributesList.get(i).getId().equals(id)){
                return attributesList.get(i);
            }
        }
        return null;
    }
}
