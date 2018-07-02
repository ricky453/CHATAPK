package com.chatapp.cdliii.mychatapp;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ricky on 06-16-18.
 */

public class Preferences {
    public static final String STRING_PREFERENCES = "mychatapp.Mensajes.MsgActivity";
    public static final String PREFERENCE_ESTADO = "estado.button.sesion";
    public static final String PREFERENCE_USUARIO_LOGIN = "usuario.login";
    public static String PREFERENCE_USUARIO_CHAT = "usuario.chat";

    public static boolean obtenerBoolean(Context c, String key){
        SharedPreferences preferences = c.getSharedPreferences(STRING_PREFERENCES, c.MODE_PRIVATE);
        return preferences.getBoolean(key, false);//Si es que nunca se ha guardado nada en esta key, retornara false
    }

    public static String obtenerString(Context c, String key){
        SharedPreferences preferences = c.getSharedPreferences(STRING_PREFERENCES, c.MODE_PRIVATE);
        return preferences.getString(key, "");//Si es que nunca se ha guardado nada en esta key, retornara cadena vacia
    }

    public static void savePreferenceBoolean(Context c, boolean b, String key){
        SharedPreferences preferences = c.getSharedPreferences(STRING_PREFERENCES, c.MODE_PRIVATE);
        preferences.edit().putBoolean(key, b).apply();
    }

    public static void savePreferenceString(Context c, String b, String key){
        SharedPreferences preferences = c.getSharedPreferences(STRING_PREFERENCES, c.MODE_PRIVATE);
        preferences.edit().putString(key, b).apply();
    }

}
