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

        }
        else {
            // TODO Grab the profile and store somewhere smart

            //Cookie Catcher
            OkHttpClient client = new OkHttpClient();
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            final ProfileRequest information = new ProfileRequest();
            final ProfileResponse information_2 = new ProfileResponse();
            builder.addInterceptor(new AddCookiesInterceptor(this)); // VERY VERY IMPORTANT
            builder.addInterceptor(new ReceivedCookiesInterceptor(this)); // VERY VERY IMPORTANT
            client = builder.build();
            final Context context = this;


            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Test_Base)
                    .client(client) // VERY VERY IMPORTANT
                    .addConverterFactory(GsonConverterFactory.create())
                    .build(); // REQUIRED

            RetroAPI mapi = retrofit.create(RetroAPI.class);
            Call<ProfileResponse> call = mapi.GetProfile(information_2);


            call.enqueue(new Callback<ProfileResponse>() {

                @Override
                //Once the call has finished
                public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {

                    if (response.isSuccessful()) {
                      String x = information_2.first_name;
                        Log.e("RequestCall", "Request Successful");
                        Toast.makeText(context, "Welcome to HackFSU, " + x,
                                Toast.LENGTH_LONG).show();
                       // startActivity(new Intent(context, MainActivity.class));
                        int f;
                        f = response.code();
                        Log.d(this.getClass().getName(), "Response code: " + f);
                    } else {
                        // show error message
                        Log.e("RequestCall", "Request failed");
                    }
                }

                @Override
                //If the call failed
                public void onFailure(Call<ProfileResponse> call, Throwable t) {

                    Log.e("RequestCall", "Request failed");
                }
            });



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
