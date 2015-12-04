package com.hackfsu.hackfsu_android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.parse.ParsePushBroadcastReceiver;

/**
 * Created by andrewsosa on 11/19/15.
 */
public class CustomPushReceiver extends ParsePushBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        // Check for the setting
        SharedPreferences sp = context.getSharedPreferences(HackFSU.PREFERENCES, Context.MODE_PRIVATE);
        if(sp.getBoolean(HackFSU.NOTIFICATIONS, true)) {
            super.onReceive(context, intent);
        }

    }

}
