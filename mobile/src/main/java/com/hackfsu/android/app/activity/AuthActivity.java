package com.hackfsu.android.app.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.hackfsu.android.api.AuthAPI;
import com.hackfsu.android.api.RetroAPI;
import com.hackfsu.android.api.templates.ProfileRequest;
import com.hackfsu.android.api.templates.ProfileResponse;
import com.hackfsu.android.api.util.AddCookiesInterceptor;
import com.hackfsu.android.api.util.ReceivedCookiesInterceptor;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.hackfsu.android.api.util.AddCookiesInterceptor.PREF_COOKIES;


/**
 * Created by andrew on 2/25/18.
 */

public class AuthActivity extends AppCompatActivity {

    final static String Test_Base = "https://testapi.hackfsu.com/";
    final static String Test_Profile = "api/user/get/profile";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!isAuthenticated()) {
            startActivity(new Intent(this, LoginActivity.class));

            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            getApplicationContext().startActivity(intent);


        }
        else
        {
            Log.d(this.getClass().getName(), "Loading Profile");
            AuthAPI Authenticate =  new AuthAPI(this);
            Authenticate.getProfile(this);
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
