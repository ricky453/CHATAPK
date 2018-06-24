package com.chatapp.cdliii.mychatapp.Mensajes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.chatapp.cdliii.mychatapp.Internet.SolicitudesJSON;
import com.chatapp.cdliii.mychatapp.Preferences;
import com.chatapp.cdliii.mychatapp.R;
import com.chatapp.cdliii.mychatapp.VolleyRP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ricky on 06-04-18.
 */

public class MsgActivity extends AppCompatActivity {

    public static final String MENSAJE = "MENSAJE";

    private BroadcastReceiver broadcastReceiver;

    private RecyclerView rv;
    private Button btnEnviar;
    private EditText etEscribirMensaje;
    private List<TextMsg> mensajeTexto;
    private MsgAdapter adapter;
    private int TEXT_LINES=1;

    private String EMISOR = "";
    private String MENSAJE_ENVIAR = "";
    private String RECEPTOR;

    private Calendar calendar = Calendar.getInstance();

    private static final String IP_MENSAJE = "https://cdliii-android.000webhostapp.com/ArchivosPHP/Enviar_Mensajes.php";

    private VolleyRP volley;
    private RequestQueue mRequest;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mensajeria);
        mensajeTexto = new ArrayList<>();

        volley = VolleyRP.getInstance(this);
        mRequest = volley.getRequestQueue();

        EMISOR = Preferences.obtenerString(this, Preferences.PREFERENCE_USUARIO_LOGIN);
        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        if(bundle!=null){
            RECEPTOR = bundle.getString("key_receptor");
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        btnEnviar = (Button) findViewById(R.id.btnEnviar);
        etEscribirMensaje = (EditText) findViewById(R.id.txtNuevoMensaje);

        rv = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setStackFromEnd(true);
        rv.setLayoutManager(lm);

        adapter = new MsgAdapter(mensajeTexto, this);
        rv.setAdapter(adapter);


        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mensaje = etEscribirMensaje.getText().toString().trim(); //Quita espacios del inicio y final
                if(!mensaje.isEmpty() && !RECEPTOR.isEmpty()){
                    MENSAJE_ENVIAR = mensaje;
                    sendMsg();
                    int hora = calendar.get(Calendar.HOUR_OF_DAY);
                    int minutos = calendar.get(Calendar.MINUTE);

                    createMsg(mensaje,""+hora+":"+minutos, 1);
                    etEscribirMensaje.setText("");
                }
;            }
        });
        etEscribirMensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rv.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).isActive()){
                            setScrollbarChat();
                        }else{
                            rv.postDelayed(this, 100);
                        }
                    }
                }, 100);
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    finish();
                }
        });
        setScrollbarChat();
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String mensaje = intent.getStringExtra("key_mensaje");
                String hora = intent.getStringExtra("key_hora");
                String horaParametros[] = hora.split(",");
                String emisor = intent.getStringExtra("key_emisor_PHP");
                if(emisor.equals(RECEPTOR)){
                    createMsg(mensaje, horaParametros[0], 2);
                }
            }
        };
        SolicitudesJSON json = new SolicitudesJSON() {
            @Override
            public void solicitudCompletada(JSONObject object) {
                try {
                    JSONArray jsonArray = object.getJSONArray("resultado");
                    for(int i = 0; i<jsonArray.length(); i++){
                        JSONObject jo = jsonArray.getJSONObject(i);
                        String mensaje = jo.getString("mensaje");
                        String hora = jo.getString("hora_del_mensaje").split(",")[0];
                        int tipo = jo.getInt("tipo_mensaje");
                        createMsg(mensaje, hora, tipo);
                    }

                } catch (JSONException e) {
                    Toast.makeText(MsgActivity.this, "Ocurrió un error al descomprimir los mensajes.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void solicitudErronea() {

            }
        };
        HashMap <String, String> hashMap = new HashMap<>();
        hashMap.put("emisor", EMISOR);
        hashMap.put("receptor", RECEPTOR);
        json.POST(this, SolicitudesJSON.URL_GET_ALL_MSG,hashMap);
    }

    private void sendMsg(){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("emisor", EMISOR);
        hashMap.put("receptor", RECEPTOR);
        hashMap.put("mensaje", MENSAJE_ENVIAR);
        JsonObjectRequest solicitud = new JsonObjectRequest(Request.Method.POST, IP_MENSAJE, new JSONObject(hashMap), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Toast.makeText(MsgActivity.this, response.getString("resultado"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {}
            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MsgActivity.this, "Ocurrió un error.", Toast.LENGTH_SHORT).show();
            }
        });
        VolleyRP.addToQueue(solicitud, mRequest, this, volley);
    }

    public void createMsg(String msg, String hora, int tipo){
        TextMsg msjAuxiliar = new TextMsg();
        msjAuxiliar.setId("0");
        msjAuxiliar.setMsg(msg);
        msjAuxiliar.setHora(hora);
        msjAuxiliar.setTipoMensaje(tipo);
        mensajeTexto.add(msjAuxiliar);
        adapter.notifyDataSetChanged();
        setScrollbarChat();
    }

    @Override
    protected void onPause(){
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter(MENSAJE));
    }

    public void setScrollbarChat(){
        rv.scrollToPosition(adapter.getItemCount()-1);
    }
}
