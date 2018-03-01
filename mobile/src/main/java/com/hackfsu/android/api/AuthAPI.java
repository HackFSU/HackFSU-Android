package com.hackfsu.android.api;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.util.ArraySet;
import android.util.Log;
import android.widget.Toast;

import com.hackfsu.android.api.templates.ProfileRequest;
import com.hackfsu.android.api.templates.ProfileResponse;
import com.hackfsu.android.api.util.AddCookiesInterceptor;
import com.hackfsu.android.api.util.ReceivedCookiesInterceptor;
import com.hackfsu.android.app.activity.LoginActivity;

import java.util.Set;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by andrew on 2/25/18.
 */

public class AuthAPI extends API {

    public AuthAPI(Activity mActivity) {
        super(mActivity);
    }

    public void getProfile(Context context) {


            // TODO Grab the profile and store somewhere smart

        //Cookie Catcher
        OkHttpClient client = new OkHttpClient();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new AddCookiesInterceptor(context)); // VERY VERY IMPORTANT
        builder.addInterceptor(new ReceivedCookiesInterceptor(context)); // VERY VERY IMPORTANT
        client = builder.build();
        final Context c = context;


        Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Test_Base)
                    .client(client) // VERY VERY IMPORTANT
                    .addConverterFactory(GsonConverterFactory.create())
                    .build(); // REQUIRED

            RetroAPI mapi = retrofit.create(RetroAPI.class);
            Call<ProfileResponse> call = mapi.GetProfile(new AddCookiesInterceptor(context));


            call.enqueue(new Callback<ProfileResponse>() {

                @Override
                //Once the call has finished
                public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {

                    if (response.isSuccessful()) {

                        String x = response.body().first_name;
                        Log.e("RequestCall", "Request Successful");
//                        Toast.makeText(c, "Welcome to HackFSU, " + x,
//                                Toast.LENGTH_LONG).show();
                        // startActivity(new Intent(context, MainActivity.class));
                        int f;
                        f = response.code();
                        Log.d(this.getClass().getName(), "Response code: " + f);


                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(c);
                        final SharedPreferences.Editor edit = preferences.edit();
                        edit.putString("first_name", response.body().first_name);
                        edit.putString("last_name", response.body().last_name);
                        edit.putString("email", response.body().email);
                        edit.putString("shirt_size", response.body().shirt_size);
                        edit.putString("phone_number", response.body().phone_number);
                        edit.putString("diet", response.body().diet);
                        edit.putString("github", response.body().github);
                        edit.putString("linkedin", response.body().linkedin);
                        edit.putString("rsvp_confirmed", response.body().rsvp_confirmed);
                        edit.putString("checked_in", response.body().checked_in);
                        edit.putString("hexcode", response.body().hexcode);
                        edit.putString("qr", response.body().qr);
                        edit.putString("Logged_user", response.body().email);
                        edit.commit();


                        Set<String> groupSet = new ArraySet<>();
                        for(String group : response.body().groups) {
                            groupSet.add(group);
                        }
                        edit.putStringSet("groups", groupSet);


                        edit.commit();

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
