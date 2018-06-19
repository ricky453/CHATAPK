package com.chatapp.cdliii.mychatapp.Services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.annotation.VisibleForTesting;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.app.NotificationCompat.Builder;
import com.chatapp.cdliii.mychatapp.Mensajes.MsgActivity;
import com.chatapp.cdliii.mychatapp.Preferences;
import com.chatapp.cdliii.mychatapp.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

/**
 * Created by ricky on 06-07-18.
 */

public class FireBaseServiceMensajes extends FirebaseMessagingService{
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String type = remoteMessage.getData().get("type");
        String cabecera = remoteMessage.getData().get("cabezera");
        String cuerpo = remoteMessage.getData().get("cuerpo");
        switch (type){
            case "mensaje":
                String mensaje = remoteMessage.getData().get("mensaje");
                String hora = remoteMessage.getData().get("hora");
                String receptor = remoteMessage.getData().get("receptor");
                String emisorPHP = remoteMessage.getData().get("emisor");
                String emisor = Preferences.obtenerString(this, Preferences.PREFERENCE_USUARIO_LOGIN);
                if(emisor.equals(receptor)){
                    showNotification(cabecera, cuerpo);
                    mensaje(mensaje, hora, emisorPHP);
                }
                break;
            case "solicitud":
                String usuario_envio_solicitud = remoteMessage.getData().get("user_envio_solicitud");
                showNotification(cabecera, cuerpo);
                break;
        }

    }

    private void mensaje(String mensaje, String hora, String emisor){
        Intent i = new Intent(MsgActivity.MENSAJE);
        i.putExtra("key_mensaje", mensaje);
        i.putExtra("key_hora", hora);
        i.putExtra("key_emisor_PHP", emisor);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(i);
    }

    private void showNotification(String cabecera, String cuerpo){
        Intent i = new Intent(this, MsgActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_ONE_SHOT);

        Uri soundNotificacion = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Builder builder = new Builder(this);
        builder.setAutoCancel(true);
        builder.setContentTitle(cabecera);
        builder.setContentText(cuerpo);
        builder.setSound(soundNotificacion);
        builder.setSmallIcon(R.drawable.ic_stat_textsms);
        builder.setTicker(cuerpo);
        builder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        Random random = new Random();

        notificationManager.notify(random.nextInt(), builder.build());
    }
}
