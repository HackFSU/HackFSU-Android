package com.hackfsu.android.app.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.hackfsu.android.api.API;
import com.hackfsu.android.api.AddCookiesInterceptor;
import com.hackfsu.android.api.ReceivedCookiesInterceptor;
import com.hackfsu.android.api.RetroAPI;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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

        }
    }


    /**
     * Determines whether the user is logged in based on whether the profile is stored.
     * @return Whether we are authenticated or not.
     */
    private boolean isAuthenticated() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String userDetatails = sharedPreferences.getString("Logged_user",null);
        return !(userDetatails == null) || (userDetatails.isEmpty());
    }

    private void getProfile() {
        //Cookie Catcher
        OkHttpClient client = new OkHttpClient();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.addInterceptor(new AddCookiesInterceptor(this)); // VERY VERY IMPORTANT
        builder.addInterceptor(new ReceivedCookiesInterceptor(this)); // VERY VERY IMPORTANT
        client = builder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API.API_HOST)
                .client(client) // VERY VERY IMPORTANT
                .addConverterFactory(GsonConverterFactory.create())
                .build(); // REQUIRED

        RetroAPI mapi = retrofit.create(RetroAPI.class);
    }
}
