package com.chatapp.cdliii.mychatapp.Usuarios;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chatapp.cdliii.mychatapp.Inicio.LoginActivity;
import com.chatapp.cdliii.mychatapp.Internet.SolicitudesJSON;
import com.chatapp.cdliii.mychatapp.Preferences;
import com.chatapp.cdliii.mychatapp.R;
import com.chatapp.cdliii.mychatapp.Usuarios.Amigos.FriendsAttributes;
import com.chatapp.cdliii.mychatapp.Usuarios.Buscador.UserAttributes;
import com.chatapp.cdliii.mychatapp.Usuarios.Solicitudes.Requests;

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

    private TextView nombre;
    private Dialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuarios);
        setTitle("Mensajería");
        dialog = new Dialog(this);

        bus = EventBus.getDefault();


        //Menu
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mToogle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.abrir, R.string.cerrar);
        mDrawerLayout.addDrawerListener(mToogle);
        mToogle.syncState();
        NavigationView nvDrawer = (NavigationView) findViewById(R.id.nv);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupDrawerContent(nvDrawer);
        View v = nvDrawer.getHeaderView(0);
        nombre = (TextView) v.findViewById(R.id.txtNombre);
        nombre.setText(Preferences.obtenerString(this, Preferences.PREFERENCE_USUARIO_LOGIN));

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
                            usuario.setCorreo(jsonObject.getString("Correo"));
                            usuario.setTelefono(jsonObject.getString("Telefono"));
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
                                    friendsAttributes.setCorreo(jsonObject.getString("Correo"));
                                    friendsAttributes.setTelefono(jsonObject.getString("Telefono"));
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
        switch (item.getItemId()){
            case R.id.mensajeria:
                break;
            case R.id.perfil:
                obtenerMisDatos();
                break;
            case R.id.salir:
                Preferences.savePreferenceBoolean(UserActivity.this, false, Preferences.PREFERENCE_ESTADO);
                subirToken(Preferences.obtenerString(UserActivity.this, Preferences.PREFERENCE_USUARIO_LOGIN));
                Intent intent = new Intent(UserActivity.this, LoginActivity.class);
                startActivity(intent);
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

    public void obtenerMisDatos(){
        final SolicitudesJSON json = new SolicitudesJSON() {
            @Override
            public void solicitudCompletada(JSONObject object) {
                try {
                    JSONObject jsonDatos = new JSONObject(object.getString("resultado"));
                    String nom = jsonDatos.getString("Nombres");
                    String ape = jsonDatos.getString("Apellidos");
                    int tel = jsonDatos.getInt("Telefono");
                    String cor = jsonDatos.getString("Correo");
                    String pas = jsonDatos.getString("Password");
                    //mostrarPerfil(nom, ape, tel, cor, pas);
                } catch (JSONException e) {
                    Toast.makeText(UserActivity.this, "¡Error al recibir los datos!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void solicitudErronea() {
                Toast.makeText(UserActivity.this, "¡Ocurrió un error al recibir los datos!", Toast.LENGTH_SHORT).show();
            }
        };
        json.GET(UserActivity.this, SolicitudesJSON.URL_GET_PROFILE_DATA+
                Preferences.obtenerString(UserActivity.this, Preferences.PREFERENCE_USUARIO_LOGIN));
    }
    public void mostrarPerfil(String nombre, String apellidos, int telefono, String correo, String contraseña){
        TextView txtCerrar;
        TextView Apellidos;
        TextView Pass;
        TextView Name;
        TextView Cell;
        TextView Correo;
        TextView id;
        dialog.setContentView(R.layout.mi_perfil);
        txtCerrar = (TextView) dialog.findViewById(R.id.miCerrarPerfil);
        id = (TextView) dialog.findViewById(R.id.myID);
        Name = (TextView) dialog.findViewById(R.id.miNombre);
        Apellidos = (TextView) dialog.findViewById(R.id.miApellidos);
        Correo = (TextView) dialog.findViewById(R.id.miCorreo);
        Cell = (TextView) dialog.findViewById(R.id.miTelefono);
        Pass = (TextView) dialog.findViewById(R.id.miPassword);
        id.setText(Preferences.obtenerString(UserActivity.this, Preferences.PREFERENCE_USUARIO_LOGIN));
        Name.setText(nombre);
        Apellidos.setText(apellidos);
        Cell.setText(telefono);
        Correo.setText(correo);
        Pass.setText(contraseña);
        txtCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
}
