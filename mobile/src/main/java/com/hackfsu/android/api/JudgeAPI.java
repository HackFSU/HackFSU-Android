package com.hackfsu.android.api;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.hackfsu.android.api.templates.HacksResponse;
import com.hackfsu.android.api.util.AddCookiesInterceptor;
import com.hackfsu.android.api.util.ReceivedCookiesInterceptor;
import com.hackfsu.android.app.activity.MainActivity;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by andrew on 2/26/18.
 */

public class JudgeAPI extends API {

    public JudgeAPI(Activity mActivity) {
        super(mActivity);
    }


    public interface onAssignmentRetrievedListener {
        void onAssignment(ArrayList<Integer> tableNumbers);
        void onFailure();
    }


    public void getHackAssignment(onAssignmentRetrievedListener listener) {

        // TODO get hacks
        listener.onAssignment(new ArrayList<Integer>());

    }



    public void getHacks(Activity mActivity) {
        // Getting hacks

        final String Test_Base = "https://testapi.hackfsu.com/";


        //Cookies
        OkHttpClient client = new OkHttpClient();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new AddCookiesInterceptor(mActivity)); // VERY VERY IMPORTANT
        builder.addInterceptor(new ReceivedCookiesInterceptor(mActivity)); // VERY VERY IMPORTANT
        client = builder.build();
        final Context c = mActivity;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Test_Base)
                .client(client) // VERY VERY IMPORTANT
                .addConverterFactory(GsonConverterFactory.create())
                .build(); // REQUIRED

        RetroAPI mapi = retrofit.create(RetroAPI.class);
        Call<HacksResponse> call = mapi.GetHacks(new AddCookiesInterceptor(mActivity));


        call.enqueue(new Callback<HacksResponse>() {

            @Override
            //Once the call has finished
            public void onResponse(Call<HacksResponse> call, Response<HacksResponse> response) {

                if (response.isSuccessful()) {

                    String x = response.body().expo;
                    Log.e("RequestCall", "Request Successful");
                    Toast.makeText(c, "Expo is: " + x,
                            Toast.LENGTH_LONG).show();
                    // startActivity(new Intent(context, MainActivity.class));
                    int f;
                    f = response.code();
                    Log.d(this.getClass().getName(), "Response code: " + f);


                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(c);
                    final SharedPreferences.Editor edit = preferences.edit();
                    edit.putString("expo", response.body().expo);


                    for(int i = 0; i < response.body().superlatives.size(); i++)
                    {
                        edit.putString("superlatives"+i , response.body().superlatives.get(i));
                    }
                    for(int i = 0; i < response.body().hacks.size(); i++)
                    {
                        edit.putString("hacks"+i , response.body().hacks.get(i));
                    }
                    edit.putInt("number_of_hacks", response.body().hacks.size());
                    edit.commit();

                } else {
                    // show error message
                    Log.e("RequestCall", "Request failed");
                }
            }

            @Override
            //If the call failed
            public void onFailure(Call<HacksResponse> call, Throwable t) {

                Log.e("RequestCall", "Request failed");
            }
        });

    }




    public void SendHacks(final Activity mActivity){


        //TODO: THROW BUNDLE HERE, SET HACKS EQUAL TO THESE VARIABLES (IN ORDER OR THEIR RANK) AND GO (IT SHOULD WORK)

        String hack_1 = null;
        String hack_2 = null;
        String hack_3 = null;


        String json = "{\n" +
                "\t\"order\": {\" "+hack_1+"\",\n" +
                "\t\""+hack_2+"\"\n" +
                "\t\""+hack_3+"\"\n"
                +"}";



//Cookie Catcher
        OkHttpClient client = new OkHttpClient();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new AddCookiesInterceptor(mActivity)); // VERY VERY IMPORTANT
        builder.addInterceptor(new ReceivedCookiesInterceptor(mActivity)); // VERY VERY IMPORTANT
        client = builder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Test_Base)
                .client(client) // VERY VERY IMPORTANT
                .addConverterFactory(GsonConverterFactory.create())
                .build(); // REQUIRED

        RetroAPI mapi = retrofit.create(RetroAPI.class);

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);

        //SENDING HACKS
        mapi.postHacks(requestBody).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    Toast.makeText(mActivity, "Hacks Sent Successfully, Response code: " + response.code(),
                            Toast.LENGTH_LONG).show();

                } else if (response.code() == 401) {
                    Log.d(this.getClass().getName(), "Unauthorized");
                    // Handle unauthorized
                } else {
                    int x;
                    x = response.code();
                    Log.d(this.getClass().getName(), "Response code: " + x);
                    // Handle other responses
                    Toast.makeText(mActivity, "Can't send Hacks, response code: " + x,
                            Toast.LENGTH_LONG).show();
                }

            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


                Toast.makeText(mActivity, "Submission Failed",
                        Toast.LENGTH_LONG).show();

            }

        });
    }

}
