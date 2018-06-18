package com.chatapp.cdliii.mychatapp.Amigos;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.chatapp.cdliii.mychatapp.Preferences;
import com.chatapp.cdliii.mychatapp.R;
import com.chatapp.cdliii.mychatapp.Usuarios.LoginActivity;
import com.chatapp.cdliii.mychatapp.VolleyRP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by ricky on 06-02-18.
 */

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToogle;

    private RecyclerView rv;
    private List<FriendsAttributes> attributesList;
    private FriendsAdapter adapter;

    private VolleyRP volley;
    private RequestQueue mRequest;

    private static final String URL_GET_ALL_USERS = "https://cdliii-android.000webhostapp.com/ArchivosPHP/Amigos_GETALL.php";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_amigos);

        attributesList = new ArrayList<>();

        volley = VolleyRP.getInstance(this);
        mRequest = volley.getRequestQueue();

        //Menu
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mToogle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.abrir, R.string.cerrar);
        mDrawerLayout.addDrawerListener(mToogle);
        mToogle.syncState();
        NavigationView nvDrawer = (NavigationView) findViewById(R.id.nv);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupDrawerContent(nvDrawer);

        //Friends
        rv = (RecyclerView) findViewById(R.id.amigosRecycler);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        //lm.setStackFromEnd(true);
        rv.setLayoutManager(lm);

        adapter = new FriendsAdapter(attributesList, this);
        rv.setAdapter(adapter);

        SolicitudJSON();
    }

    public void agregarAmigos(int fotoPerfil, String nombre, String ultimoMensaje, String hora, String id){
        FriendsAttributes friendsAttributes = new FriendsAttributes();
        friendsAttributes.setFotoPerfil(fotoPerfil);
        friendsAttributes.setNombre(nombre);
        friendsAttributes.setUltimoMensaje(ultimoMensaje);
        friendsAttributes.setHora(hora);
        friendsAttributes.setId(id);
        attributesList.add(friendsAttributes);
        adapter.notifyDataSetChanged();
    }

    public boolean onOptionsItemSelected(MenuItem item){
        if(mToogle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void selectIterDrawer(MenuItem item){
        Fragment fragment = null;
        Class fragmentClass;
        switch (item.getItemId()){
            case R.id.salir:
                Preferences.savePreferenceBoolean(HomeActivity.this, false, Preferences.PREFERENCE_ESTADO);
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    private void setupDrawerContent(NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectIterDrawer(item);
                return true;
            }
        });
    }
    public void SolicitudJSON(){
        JsonObjectRequest solicitud = new JsonObjectRequest(URL_GET_ALL_USERS, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String todosDatos = response.getString("resultado");
                    JSONArray jsonArray = new JSONArray(todosDatos);
                    String miUsuario = Preferences.obtenerString(HomeActivity.this, Preferences.PREFERENCE_USUARIO_LOGIN);
                    for(int i=0; i<jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        if(!miUsuario.equals(jsonObject.getString("idUsuario"))){
                            agregarAmigos(R.drawable.ic_account_circle, jsonObject.getString("Nombres")+" "+jsonObject.getString("Apellidos"), "Te amo pendejo x"+i, "00:00", jsonObject.getString("idUsuario"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(HomeActivity.this, "¡Ocurrió un error al descomponer el JSON!", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HomeActivity.this, "¡Ocurrió un error!, por favor conéctese con el administrador.", Toast.LENGTH_SHORT).show();
            }
        });
        VolleyRP.addToQueue(solicitud, mRequest, this, volley);
    }


}
