package com.hackfsu.android.app.activity;



import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.hackfsu.android.api.AuthAPI;
import com.hackfsu.android.api.util.AddCookiesInterceptor;
import com.hackfsu.android.api.util.ReceivedCookiesInterceptor;
import com.hackfsu.android.api.RetroAPI;
import com.hackfsu.android.app.R;


import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static java.security.AccessController.getContext;

public class LoginActivity extends AppCompatActivity {




    final static String Test_Base = "https://testapi.hackfsu.com/";
    final static String Test_Login = "api/user/login";
    final static String Test_Profile = "api/user/get/profile";
    final static String Test_getHacks = "api/judge/hacks";
    final static String Test_sendHacks = "api/judge/hacks/upload";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


    }


    public void onClick(View view) {
//
    Login();

    }


    public void Login(){

        EditText user  = (EditText)findViewById(R.id.editText);
        EditText pass  = (EditText)findViewById(R.id.editText2);
        final String usr_entry = user.getText().toString();
        String pass_entry = pass.getText().toString();
        final Context  context = this;

        String json = "{\n" +
                "\t\"email\":\" "+usr_entry+"\",\n" +
                "\t\"password\":\""+pass_entry+"\"\n" +
                "}";



//Cookie Catcher
        OkHttpClient client = new OkHttpClient();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new AddCookiesInterceptor(context)); // VERY VERY IMPORTANT
        builder.addInterceptor(new ReceivedCookiesInterceptor(context)); // VERY VERY IMPORTANT
        client = builder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Test_Base)
                .client(client) // VERY VERY IMPORTANT
                .addConverterFactory(GsonConverterFactory.create())
                .build(); // REQUIRED

        RetroAPI mapi = retrofit.create(RetroAPI.class);

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);

        mapi.postLogin(requestBody).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    Log.d(this.getClass().getName(), "Successful login!!");
                    // Do awesome stuff

                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                    final SharedPreferences.Editor edit = preferences.edit();
                    edit.putString("Logged_user", usr_entry);
                    edit.commit();

                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    context.startActivity(i);

                    Toast.makeText(context, "Found your profile, response code: " + response.code(),
                            Toast.LENGTH_LONG).show();
                   finish();


                } else if (response.code() == 401) {
                    Log.d(this.getClass().getName(), "Unauthorized");
                    // Handle unauthorized
                } else {
                    int x;
                    x = response.code();
                    Log.d(this.getClass().getName(), "Response code: " + x);
                    // Handle other responses
                    Toast.makeText(context, "Can't find your profile, response code: " + x,
                            Toast.LENGTH_LONG).show();
                }


            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


                Toast.makeText(context, "Can't find your profile",
                        Toast.LENGTH_LONG).show();

            }

        });


    }

}
