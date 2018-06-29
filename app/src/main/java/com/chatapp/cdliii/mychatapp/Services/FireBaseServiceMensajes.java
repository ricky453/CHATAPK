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
import com.chatapp.cdliii.mychatapp.Usuarios.Amigos.DeleteFriendFromFriends;
import com.chatapp.cdliii.mychatapp.Usuarios.Amigos.FriendsAttributes;
import com.chatapp.cdliii.mychatapp.Usuarios.Buscador.AcceptRequestFromSearcher;
import com.chatapp.cdliii.mychatapp.Usuarios.Buscador.DeleteFriendFromSearcher;
import com.chatapp.cdliii.mychatapp.Usuarios.Buscador.DeleteRequestFromSearcher;
import com.chatapp.cdliii.mychatapp.Usuarios.Buscador.GetFriendRequestFromSearcher;
import com.chatapp.cdliii.mychatapp.Usuarios.Solicitudes.DeleteRequestFromRequests;
import com.chatapp.cdliii.mychatapp.Usuarios.Solicitudes.Requests;
import com.chatapp.cdliii.mychatapp.Usuarios.UserActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.greenrobot.eventbus.EventBus;

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
                String sub_tipo = remoteMessage.getData().get("sub_type");
                String usuario_envio_solicitud;
                switch(sub_tipo){
                    case "enviar_solicitud":
                        EventBus.getDefault().post(new Requests(remoteMessage.getData().get("user_envio_solicitud"),
                                remoteMessage.getData().get("user_envio_solicitud_nombre"),
                                3,
                                remoteMessage.getData().get("hora"),
                                R.drawable.ic_account_circle));
                        EventBus.getDefault().post(new GetFriendRequestFromSearcher(remoteMessage.getData().get("user_envio_solicitud")));
                        remoteMessage.getData().get("user_envio_solicitud");
                        showNotification(cabecera, cuerpo);
                        break;
                    case "cancelar_solicitud":
                        EventBus.getDefault().post(new DeleteRequestFromSearcher(remoteMessage.getData().get("user_envio_solicitud")));
                        EventBus.getDefault().post(new DeleteRequestFromRequests(remoteMessage.getData().get("user_envio_solicitud")));
                        break;
                    case "aceptar_solicitud":
                        EventBus.getDefault().post(new FriendsAttributes(remoteMessage.getData().get("user_envio_solicitud"),
                                remoteMessage.getData().get("user_envio_solicitud_nombre"),
                                remoteMessage.getData().get("ultimoMensaje")==null?"null":remoteMessage.getData().get("ultimoMensaje"),
                                remoteMessage.getData().get("hora_del_mensaje")==null?
                                        null:remoteMessage.getData().get("hora_del_mensaje").split(",")[0],
                                R.drawable.ic_account_circle,
                                remoteMessage.getData().get("type_mensaje")==null?null:remoteMessage.getData().get("type_mensaje")));
                        EventBus.getDefault().post(new DeleteRequestFromRequests(remoteMessage.getData().get("user_envio_solicitud")));
                        remoteMessage.getData().get("user_envio_solicitud");
                        EventBus.getDefault().post(new AcceptRequestFromSearcher(remoteMessage.getData().get("user_envio_solicitud")));
                        showNotification(cabecera, cuerpo);
                        break;
                    case "eliminar_amigo":
                        EventBus.getDefault().post(new DeleteRequestFromSearcher(remoteMessage.getData().get("user_envio_solicitud")));
                        EventBus.getDefault().post(new DeleteFriendFromFriends(remoteMessage.getData().get("user_envio_solicitud")));
                        break;

                }
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
        Intent i = new Intent(this, UserActivity.class);
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
