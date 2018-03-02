package com.hackfsu.android.app.activity;



import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.hackfsu.android.api.API;
import com.hackfsu.android.api.util.AddCookiesInterceptor;
import com.hackfsu.android.api.util.ReceivedCookiesInterceptor;
import com.hackfsu.android.api.RetroAPI;
import com.hackfsu.android.app.R;


import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {


    private EditText mUserText;
    private EditText mPassText;
    private KonfettiView mConfettiView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density  = getResources().getDisplayMetrics().density;
        float dpHeight = outMetrics.heightPixels / density;
        float dpWidth  = outMetrics.widthPixels / density;

        mUserText = (EditText) findViewById(R.id.editText);
        mPassText = (EditText) findViewById(R.id.editText2);

        mConfettiView = (KonfettiView) findViewById(R.id.confetti);
//        mConfettiView.build()
//            .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
//            .setDirection(90.0)
//            .setSpeed(0.25f, 0.5f)
//            .setFadeOutEnabled(false)
//            .addShapes(Shape.RECT, Shape.CIRCLE)
//            .addSizes(new Size(12, 2))
//            .setPosition(0, 0)
//            .streamMaxParticles(5, 500);

        Log.d("derp", ""+ dpWidth);

    }


    public void onClick(View view) {
        if (view.getId() == R.id.btn_login) {

            String username = mUserText.getText().toString();
            String password = mPassText.getText().toString();

            if (!username.isEmpty() && !password.isEmpty()) {
                login(username, password);
            } else {
                Toast.makeText(this,
                        "Please enter your username and password", Toast.LENGTH_SHORT).show();
            }

        }
    }


    public void login(final String email, final String password){

        String json = "{\n" +
                "\t\"email\":\" "+email+"\",\n" +
                "\t\"password\":\""+password+"\"\n" +
                "}";

        final Context context = this;

        //Cookie Catcher
        OkHttpClient client;
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

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);

        mapi.postLogin(requestBody).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    Log.d(this.getClass().getName(), "Successful login!!");
                    // Do awesome stuff

                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                    final SharedPreferences.Editor edit = preferences.edit();
                    edit.putString("Logged_user", email);
                    edit.commit();

                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    context.startActivity(i);

//                    Toast.makeText(context, "Found your profile, response code: " + response.code(),
//                            Toast.LENGTH_LONG).show();
                    finish();


                } else if (response.code() == 401) {
                    Log.d(this.getClass().getName(), "Unauthorized");
                    // Handle unauthorized
                } else {
                    int x;
                    x = response.code();
                    Log.d(this.getClass().getName(), "Response code: " + x);
                    badLogin();
                }


            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                error();

            }

        });


    }

    void badLogin() {
        new MaterialDialog.Builder(LoginActivity.this)
                .content("Invalid username or password.")
                .positiveText("Whoops")
                .show();
    }

    void error() {
        new MaterialDialog.Builder(LoginActivity.this)
                .content("Ahhh!! There's been an error!")
                .positiveText("Whoops")
                .show();
    }

}
