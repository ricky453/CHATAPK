package com.chatapp.cdliii.mychatapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private EditText txtUsuario;
    private EditText txtPassword;
    private Button btnIngresar;
    private Button btnRegistrar;

    private VolleyRP volley;
    private RequestQueue mRequest;

    private static String IP = "https://cdliii-android.000webhostapp.com/ArchivosPHP/Login_GETID.php?id=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setBackgroundDrawableResource(R.drawable.background);

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

    }

    public void VerificarLogin(String username, String password){
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
                Toast.makeText(MainActivity.this, "Ocurrió un error al conectarse con el administrador", Toast.LENGTH_SHORT).show();
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
                if(pass.equals(txtPassword.getText().toString())){
                    Toast.makeText(this, "Contra correcta", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "Contra incorrecta", Toast.LENGTH_SHORT).show();

                }

            }else{
                Toast.makeText(this, estado, Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
        }
    }
}
