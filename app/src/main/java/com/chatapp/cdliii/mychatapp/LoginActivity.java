package com.chatapp.cdliii.mychatapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.chatapp.cdliii.mychatapp.Mensajes.MsgActivity;
import com.chatapp.cdliii.mychatapp.Usuarios.RegistroActivity;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    private EditText txtUsuario;
    private EditText txtPassword;
    private Button btnIngresar;
    private Button btnRegistrar;

    private VolleyRP volley;
    private RequestQueue mRequest;

    private static final String IP = "https://cdliii-android.000webhostapp.com/ArchivosPHP/Login_GETID.php?id=";
    private static final String IP_TOKEN = "https://cdliii-android.000webhostapp.com/ArchivosPHP/Token_INSERTandUPDATE.php";

    private String USER = "";
    private String PASSWORD = "";

    private boolean entrar = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setBackgroundDrawableResource(R.drawable.background);

        if(Preferences.obtenerBoolean(LoginActivity.this, Preferences.PREFERENCE_ESTADO)==true){
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }


        volley = VolleyRP.getInstance(this);
        mRequest = volley.getRequestQueue();

        txtUsuario = (EditText) findViewById(R.id.username);
        txtPassword = (EditText) findViewById(R.id.password);

        btnIngresar = (Button) findViewById(R.id.btnIniciar);
        btnRegistrar = (Button) findViewById(R.id.btnRegistrarse);

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VerificarLogin(txtUsuario.getText().toString(), txtPassword.getText().toString());
            }
        });
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegistroActivity.class);
                startActivity(i);
            }
        });

    }

    public void VerificarLogin(String username, String password){
        USER = username.toUpperCase().trim();
        PASSWORD = password;
        SolicitudJSON(IP+username);
    }

    public void SolicitudJSON(String URL){
        JsonObjectRequest solicitud = new JsonObjectRequest(URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                  VerificarPassword(response);
            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, "Ocurrió un error al conectarse con el administrador", Toast.LENGTH_SHORT).show();
            }
        });
        VolleyRP.addToQueue(solicitud, mRequest, this, volley);
    }

    public void VerificarPassword(JSONObject datos){
        //Controlar JSON
        try {
            String estado = datos.getString("resultado");
            if(estado.equals("CC")){
                JSONObject jsonDatos = new JSONObject(datos.getString("datos"));
                String usuario = jsonDatos.getString("idUsuario");
                String pass = jsonDatos.getString("Password");
                if(usuario.equals(USER) && pass.equals(PASSWORD)){
                    String token = FirebaseInstanceId.getInstance().getToken();
                    if(token!=null) subirToken(token);
                    else Toast.makeText(this, "El token es nulo", Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(this, "Contra incorrecta", Toast.LENGTH_SHORT).show();

                }

            }else{
                Toast.makeText(this, estado, Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
        }
    }

    private void subirToken(String token){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id", USER);
        hashMap.put("token", token);
        JsonObjectRequest solicitud = new JsonObjectRequest(Request.Method.POST, IP_TOKEN, new JSONObject(hashMap), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                entrar = true;
                Preferences.savePreferenceString(LoginActivity.this, USER, Preferences.PREFERENCE_USUARIO_LOGIN);
                Preferences.savePreferenceBoolean(LoginActivity.this, entrar, Preferences.PREFERENCE_ESTADO);
                try {
                    Toast.makeText(LoginActivity.this, response.getString("resultado"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {}
                Intent intent = new Intent(LoginActivity.this, MsgActivity.class);
                startActivity(intent);
                finish();
            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, "El token no pudo ser subido a la base de datos.", Toast.LENGTH_SHORT).show();
            }
        });
        VolleyRP.addToQueue(solicitud, mRequest, this, volley);
    }

}
