package com.example.pc.myapplication.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.pc.myapplication.LoginActivity;
import com.example.pc.myapplication.R;
import com.example.pc.myapplication.ciudadesTools.Publicidad;
import com.example.pc.myapplication.commonfunctions.Consts;
import com.example.pc.myapplication.commonfunctions.IDGenerator;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;


public class FCM_Service extends FirebaseMessagingService {

    private static final String TAG = FCM_Service.class.getSimpleName();


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.i(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getNotification() != null) {

            Log.i(TAG, "Message data payload: " + remoteMessage.getNotification().getBody());


            Publicidad publicidad = new Publicidad(remoteMessage.getNotification());

            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            intent.putExtra(Consts.HAS_PUBLICIDAD,true);
            intent.putExtra(Consts.PUBLICIDAD, publicidad);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                    PendingIntent.FLAG_CANCEL_CURRENT);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.trip_icono)
                    .setContentTitle(getResources().getString(R.string.app_name)
                                    + " - " + publicidad.getNombre())
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);

            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
            inboxStyle.addLine(publicidad.getLink());
            inboxStyle.addLine(publicidad.getDescripcion());
            notificationBuilder.setStyle(inboxStyle);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            int idGen = IDGenerator.generateViewId();
            notificationManager.notify(idGen, notificationBuilder.build());
        }

    }
}
