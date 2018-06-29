package com.chatapp.cdliii.mychatapp.Internet;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.chatapp.cdliii.mychatapp.VolleyRP;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by ricky on 06-19-18.
 */

public abstract class SolicitudesJSON {

    public static final String URL_GET_ALL_DATA = "https://cdliii-android.000webhostapp.com/ArchivosPHP/Datos_GETALL.php?id=";
    public static final String URL_GET_ALL_MSG = "https://cdliii-android.000webhostapp.com/ArchivosPHP/Mensajes_GETID.php";
    public static final String URL_SEND_REQUESTS = "https://cdliii-android.000webhostapp.com/ArchivosPHP/Solicitudes_ENVIAR.php";
    public static final String URL_DELETE_A_REQUEST = "https://cdliii-android.000webhostapp.com/ArchivosPHP/Solicitudes_CANCELAR.php";
    public static final String URL_ACCEPT_A_REQUEST = "https://cdliii-android.000webhostapp.com/ArchivosPHP/Solicitudes_ACEPTAR.php";
    public static final String URL_DELETE_A_FRIEND = "https://cdliii-android.000webhostapp.com/ArchivosPHP/Solicitudes_ELIMINAR.php";
    public static final String URL_GET_PROFILE_DATA = "https://cdliii-android.000webhostapp.com/ArchivosPHP/Login_GETUSERDATA.php?id=";
    public static final String IP_TOKEN_UPLOAD = "https://cdliii-android.000webhostapp.com/ArchivosPHP/Token_INSERTandUPDATE.php";

    public abstract void solicitudCompletada(JSONObject object);
    public abstract void solicitudErronea();

    public SolicitudesJSON(){}


    public void GET(Context c, String URL) {
        JsonObjectRequest solicitud = new JsonObjectRequest(URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                solicitudCompletada(response);
            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                solicitudErronea();
            }
        });
        VolleyRP.addToQueue(solicitud, VolleyRP.getInstance(c).getRequestQueue(), c, VolleyRP.getInstance(c));

    }
    public void POST(Context c, String URL, HashMap hashMap) {
        JsonObjectRequest solicitud = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(hashMap), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                solicitudCompletada(response);
            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                solicitudErronea();
            }
        });
        VolleyRP.addToQueue(solicitud, VolleyRP.getInstance(c).getRequestQueue(), c, VolleyRP.getInstance(c));

    }
}
