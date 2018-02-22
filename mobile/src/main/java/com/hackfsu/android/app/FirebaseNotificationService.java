package com.hackfsu.android.app;

import android.app.NotificationManager;
import android.content.Context;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Randy Bruno-Piverger on 2/21/2018.
 */

public class FirebaseNotificationService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(this.getClass().getName(), "From: " + remoteMessage.getFrom());
        String content;


        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(this.getClass().getName(), "Message data payload: " + remoteMessage.getData());

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {

            Log.d(this.getClass().getName(), "Message Notification Body: " + remoteMessage.getNotification().getBody());
           content = remoteMessage.getNotification().getBody();
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_notifications_active_black_24dp)
                    .setContentTitle("Attention Hackers")
                    .setContentText(content)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                    mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(1, mBuilder.build());

        }



    }
}
