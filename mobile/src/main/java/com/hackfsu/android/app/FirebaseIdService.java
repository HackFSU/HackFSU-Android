package com.hackfsu.android.app;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.hackfsu.android.api.PushAPI;

/**
 * Created by Randy Bruno-Piverger on 2/21/2018.
 */

public class FirebaseIdService extends FirebaseInstanceIdService {

    PushAPI api = new PushAPI();

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(this.getClass().getName(), "Refreshed token: " + refreshedToken);


        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        api.uploadDeviceToken(refreshedToken);
    }
}
