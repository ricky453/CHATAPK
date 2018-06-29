package com.chatapp.cdliii.mychatapp.Inicio;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.chatapp.cdliii.mychatapp.Internet.SolicitudesJSON;
import com.chatapp.cdliii.mychatapp.R;
import com.chatapp.cdliii.mychatapp.VolleyRP;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by ricky on 06-16-18.
 */

public class RegistroActivity extends AppCompatActivity {
    private EditText user;
    private EditText password;
    private EditText nombre;
    private EditText apellido;
    private EditText correo;
    private EditText telefono;
    private Button registro;
    private Button atras;

    private VolleyRP volley;
    private RequestQueue mRequest;


    private static final String IP_REGISTRO = "https://cdliii-android.000webhostapp.com/ArchivosPHP/Login_INSERT.php";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);
        getWindow().setBackgroundDrawableResource(R.drawable.background);

        user =(EditText) findViewById(R.id.usuarioRegistro);
        password =(EditText) findViewById(R.id.contrasenaRegistro);
        nombre =(EditText) findViewById(R.id.nombreRegistro);
        apellido =(EditText) findViewById(R.id.apellidoRegistro);
        correo =(EditText) findViewById(R.id.correoRegistro);
        telefono =(EditText) findViewById(R.id.telefonoRegistro);
        registro =(Button) findViewById(R.id.btnRegistrarRegistro);
        atras = (Button)findViewById(R.id.btnAtrasRegistro);

        volley = VolleyRP.getInstance(this);
        mRequest = volley.getRequestQueue();

        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!getStringET(user).isEmpty()&&
                        !getStringET(nombre).isEmpty()&&
                        !getStringET(apellido).isEmpty()&&
                        !getStringET(correo).isEmpty()&&
                        !getStringET(password).isEmpty()){
                    registrarWebService(getStringET(user).trim(),
                    getStringET(password).trim(),
                            getStringET(nombre).trim(),
                            getStringET(apellido).trim(),
                            getStringET(correo).trim(),
                            getStringET(telefono).trim());
                }else{
                    Toast.makeText(RegistroActivity.this, "Rellene todos los campos obligatorios.", Toast.LENGTH_SHORT).show();
                }
            }

        });
        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private String getStringET (EditText e){
        return e.getText().toString();
    }

    private void registrarWebService(final String usuario, String contrasena, String nombre, String apellido, String correo, String telefono){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id", usuario.toLowerCase());
        hashMap.put("password", contrasena);
        hashMap.put("nombres", nombre);
        hashMap.put("apellidos", apellido);
        hashMap.put("correo", correo.toLowerCase());
        hashMap.put("telefono", telefono);
        JsonObjectRequest solicitud = new JsonObjectRequest(Request.Method.POST, IP_REGISTRO, new JSONObject(hashMap), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String estado = response.getString("resultado");
                    if(estado.equalsIgnoreCase("Se ha registrado correctamente.")){
                        String token = FirebaseInstanceId.getInstance().getToken();
                        if(token!=null) subirToken(usuario);
                        else Toast.makeText(RegistroActivity.this, "El token es nulo.", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(RegistroActivity.this, estado, Toast.LENGTH_SHORT).show();
                        user.selectAll();
                        user.requestFocus();
                    }
                } catch (JSONException e) {}
            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegistroActivity.this, "ERROR: no se pudo registrar.", Toast.LENGTH_SHORT).show();
            }
        });
        VolleyRP.addToQueue(solicitud, mRequest, this, volley);
    }

    private void subirToken(String id) {
        SolicitudesJSON json = new SolicitudesJSON() {
            @Override
            public void solicitudCompletada(JSONObject object) {
                Toast.makeText(RegistroActivity.this, "Se registró correctamente.", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void solicitudErronea() {
                Toast.makeText(RegistroActivity.this, "No se pudo subir el token.", Toast.LENGTH_SHORT).show();
                finish();
            }
        };
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id", id);
        hashMap.put("token", "0");
        json.POST(this, SolicitudesJSON.IP_TOKEN_UPLOAD, hashMap);

    }
}
