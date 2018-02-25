package com.hackfsu.android.app.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;


/**
 * Created by andrew on 2/25/18.
 */

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!isAuthenticated()) {
            startActivity(new Intent(this, LoginActivity.class));
        }
        else {
            // TODO Grab the profile and store somewhere smart
            
        }
    }


    /**
     * Determines whether the user is logged in based on whether the profile is stored.
     * @return Whether we are authenticated or not.
     */
    private boolean isAuthenticated() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String userDetails = sharedPreferences.getString("Logged_user",null);
        return userDetails != null && !userDetails.isEmpty();
    }


}
