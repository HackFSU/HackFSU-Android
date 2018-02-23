package com.hackfsu.android.api;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.hackfsu.android.app.BuildConfig;

import java.io.IOException;

/**
 * Created by andrew on 2/22/18.
 */

public class PushAPI {

    private NetworkClient networkClient = new NetworkClient();
    final static String API_HOST = BuildConfig.API_HOST;


    public void uploadDeviceToken(String token) {

        /* Sample JSON request:
            {
                "deviceID": "ddddddd",
               "platform": 2
            }
        */

        String payload = "{\"platform\": 2, \"deviceID\": \"" + token + "\"}";

        try {
            networkClient.post(API_HOST + "/api/push/register", payload,
                    new NetworkClient.NetworkCallback() {
                        @Override
                        public void onComplete(String json) {
                            Log.d(PushAPI.this.getClass().getName(), "Uploaded token");
                        }

                        @Override
                        public void onFailure(Exception e) {
                            Log.d(PushAPI.this.getClass().getName(), "Failed to upload token");
                        }
                    });
        } catch (IOException e) {
            Log.e(PushAPI.this.getClass().getName(), e.getLocalizedMessage());
        }



    }


}
