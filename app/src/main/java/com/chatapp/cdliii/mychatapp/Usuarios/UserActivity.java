package com.chatapp.cdliii.mychatapp.Usuarios;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.chatapp.cdliii.mychatapp.Comunicacion.Usuario;
import com.chatapp.cdliii.mychatapp.Inicio.LoginActivity;
import com.chatapp.cdliii.mychatapp.Internet.SolicitudesJSON;
import com.chatapp.cdliii.mychatapp.Preferences;
import com.chatapp.cdliii.mychatapp.R;
import com.chatapp.cdliii.mychatapp.Usuarios.Amigos.FriendsAttributes;
import com.chatapp.cdliii.mychatapp.Usuarios.Buscador.UserAttributes;
import com.chatapp.cdliii.mychatapp.Usuarios.Solicitudes.Requests;
import com.chatapp.cdliii.mychatapp.VolleyRP;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by ricky on 06-18-18.
 */

public class UserActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private EventBus bus;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToogle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuarios);
        setTitle("Mensajeria");

        bus = EventBus.getDefault();

        //Menu
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mToogle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.abrir, R.string.cerrar);
        mDrawerLayout.addDrawerListener(mToogle);
        mToogle.syncState();
        NavigationView nvDrawer = (NavigationView) findViewById(R.id.nv);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupDrawerContent(nvDrawer);

        tabLayout = (TabLayout) findViewById(R.id.tabUsuarios);
        viewPager = (ViewPager) findViewById(R.id.viewPagerUsuarios);

        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(new UserAdapter(getSupportFragmentManager()));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==0){
                }else if(position==1){
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setOffscreenPageLimit(3);
        String usuario = Preferences.obtenerString(this, Preferences.PREFERENCE_USUARIO_LOGIN);
        SolicitudesJSON sj = new SolicitudesJSON() {
            @Override
            public void solicitudCompletada(JSONObject object) {
                try {
                    JSONArray jsonArray = object.getJSONArray("resultado");
                    for(int i = 0; i<jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String id = jsonObject.getString("idUsuario");
                        if(!id.equals(Preferences.obtenerString(UserActivity.this, Preferences.PREFERENCE_USUARIO_LOGIN))) {
                            String nombreCompleto = jsonObject.getString("Nombres") + " "
                                    + jsonObject.getString("Apellidos");
                            String estado = jsonObject.getString("estado");
                            UserAttributes usuario = new UserAttributes();
                            Requests requests;
                            usuario.setId(id);
                            usuario.setNombre(nombreCompleto);
                            usuario.setEstado(1);
                            usuario.setFotoPerfil(R.drawable.ic_account_circle);
                            switch (estado) {
                                case "2": //Solicitud
                                    usuario.setEstado(2);
                                    requests = new Requests();
                                    requests.setId(id);
                                    requests.setNombre(nombreCompleto);
                                    requests.setFotoPerfil(R.drawable.ic_account_circle);
                                    requests.setHora(jsonObject.getString("fecha_amigos"));
                                    requests.setEstado(2);
                                    bus.post(requests);
                                    break;
                                case "3": //Solicitud
                                    usuario.setEstado(3);
                                    requests = new Requests();
                                    requests.setId(id);
                                    requests.setNombre(nombreCompleto);
                                    requests.setFotoPerfil(R.drawable.ic_account_circle);
                                    requests.setHora(jsonObject.getString("fecha_amigos"));
                                    requests.setEstado(3);
                                    bus.post(requests);
                                    break;
                                case "4": //Son amigos
                                    usuario.setEstado(4);
                                    FriendsAttributes friendsAttributes = new FriendsAttributes();
                                    friendsAttributes.setId(id);
                                    friendsAttributes.setNombre(nombreCompleto);
                                    friendsAttributes.setFotoPerfil(R.drawable.ic_account_circle);
                                    friendsAttributes.setType_mensaje(jsonObject.getString("tipo_mensaje"));
                                    friendsAttributes.setMensaje(jsonObject.getString("mensaje"));
                                    String hora_mensaje = jsonObject.getString("hora_del_mensaje");
                                    String hora_vector[] = hora_mensaje.split(",");
                                    friendsAttributes.setHora(hora_vector[0]);
                                    friendsAttributes.setEstado(4);
                                    bus.post(friendsAttributes);
                                    break;
                            }
                            bus.post(usuario);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void solicitudErronea() {
                Toast.makeText(UserActivity.this, "Ocurrió un error al pedir datos en UserActivity.", Toast.LENGTH_LONG).show();
            }
        };
        sj.GET(this, SolicitudesJSON.URL_GET_ALL_DATA+usuario);
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
                Preferences.savePreferenceBoolean(UserActivity.this, false, Preferences.PREFERENCE_ESTADO);
                Intent intent = new Intent(UserActivity.this, LoginActivity.class);
                startActivity(intent);
                subirToken(Preferences.obtenerString(UserActivity.this, Preferences.PREFERENCE_USUARIO_LOGIN));
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

    private void subirToken(String id) {
        SolicitudesJSON json = new SolicitudesJSON() {
            @Override
            public void solicitudCompletada(JSONObject object) {
                Toast.makeText(UserActivity.this, "Cerró sesión.", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void solicitudErronea() {
                Toast.makeText(UserActivity.this, "No se pudo subir el token.", Toast.LENGTH_SHORT).show();
                finish();
            }
        };
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id", id);
        hashMap.put("token", "0");
        json.POST(this, SolicitudesJSON.IP_TOKEN_UPLOAD, hashMap);

    }

}
