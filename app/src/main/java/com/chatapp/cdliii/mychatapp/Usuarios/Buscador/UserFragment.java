package com.chatapp.cdliii.mychatapp.Usuarios.Buscador;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.chatapp.cdliii.mychatapp.Preferences;
import com.chatapp.cdliii.mychatapp.R;
import com.chatapp.cdliii.mychatapp.VolleyRP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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

    private VolleyRP volley;
    private RequestQueue mRequest;

    private static final String URL_GET_ALL_USERS = "https://cdliii-android.000webhostapp.com/ArchivosPHP/Usuarios_GETALL.php";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_buscar_usuarios, container, false);

        volley = VolleyRP.getInstance(getContext());
        mRequest = volley.getRequestQueue();

        attributesList = new ArrayList<>();
        listAuxiliar = new ArrayList<>();

        rv = (RecyclerView) v.findViewById(R.id.recyclerViewUsuarios);
        search = (EditText) v.findViewById(R.id.searchUsuarios);
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(lm);
        adapter = new UserSearchAdapter(attributesList, getContext());
        rv.setAdapter(adapter);

        SolicitudJSON();

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

        return v;
    }

    public void insertarUsuarios(int fotoPerfil, String nombre, String estado, String id){
        UserAttributes userAttributes = new UserAttributes();
        userAttributes.setFotoPerfil(fotoPerfil);
        userAttributes.setNombre(nombre);
        userAttributes.setEstadoUsuario(estado);
        userAttributes.setId(id);
        attributesList.add(userAttributes);
        listAuxiliar.add(userAttributes);
        adapter.notifyDataSetChanged();
    }

    public void buscador(String texto){
        attributesList.clear();
        for(int i=0; i<listAuxiliar.size(); i++){
            if(listAuxiliar.get(i).getNombre().toLowerCase().contains(texto.toLowerCase())){
                attributesList.add(listAuxiliar.get(i));
            }
        }
        adapter.notifyDataSetChanged();
    }
    public void SolicitudJSON(){
        JsonObjectRequest solicitud = new JsonObjectRequest(URL_GET_ALL_USERS, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String todosDatos = response.getString("resultado");
                    JSONArray jsonArray = new JSONArray(todosDatos);
                    String miUsuario = Preferences.obtenerString(getContext(), Preferences.PREFERENCE_USUARIO_LOGIN);
                    for(int i=0; i<jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        if(!miUsuario.equals(jsonObject.getString("idUsuario"))){
                            insertarUsuarios(R.drawable.ic_account_circle, jsonObject.getString("Nombres")+" "+jsonObject.getString("Apellidos"), "No son amigos.", jsonObject.getString("idUsuario"));
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
        VolleyRP.addToQueue(solicitud, mRequest, getContext(), volley);
    }

}
