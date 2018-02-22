package com.hackfsu.mobile.android.app;

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

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(this.getClass().getName(), "Message data payload: " + remoteMessage.getData());



        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(this.getClass().getName(), "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }
}
