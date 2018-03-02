package com.hackfsu.android.api;

import android.app.Activity;
import android.util.Log;

import com.hackfsu.android.api.model.EventModel;
import com.hackfsu.android.api.templates.EventsResponse;
import com.hackfsu.android.api.util.AddCookiesInterceptor;
import com.hackfsu.android.api.util.ReceivedCookiesInterceptor;
import com.hackfsu.android.app.BuildConfig;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Randy Bruno-Piverger on 2/28/2018.
 */

public class EventAPI extends API {

    final static String API_HOST = BuildConfig.API_HOST;

    public EventAPI(Activity mActivity) {
        super(mActivity);
    }


    public interface OnEventsReceivedListener {
        void onEvents(ArrayList<EventModel> eventList);
        void onFailure(String message);
    }



    public void getHackerEvents(final OnEventsReceivedListener listener) {

        OkHttpClient client;
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new AddCookiesInterceptor(mActivity)); // VERY VERY IMPORTANT
        builder.addInterceptor(new ReceivedCookiesInterceptor(mActivity)); // VERY VERY IMPORTANT
        client = builder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_HOST)
                .client(client) // VERY VERY IMPORTANT
                .addConverterFactory(GsonConverterFactory.create())
                .build(); // REQUIRED

        RetroAPI mapi = retrofit.create(RetroAPI.class);
        Call<EventsResponse> call = mapi.GetEvents(new AddCookiesInterceptor(mActivity));


        call.enqueue(new Callback<EventsResponse>() {

            @SuppressWarnings("ConstantConditions")
            @Override
            public void onResponse(Call<EventsResponse> call, Response<EventsResponse> response) {

                String message = "Derp message";


                if (response.isSuccessful() && response.body() != null) {

                    Log.d(EventAPI.class.getName(), "Request Successful: getHackerEvents");
                    Log.d(this.getClass().getName(), "Response code: " + response.code());

                    try {
                        listener.onEvents(response.body().events);
                        return;
                    }
                    catch (Exception e) {
//                        Log.e(JudgeAPI.class.getName(), e.getLocalizedMessage());
                        message = "judgeAPI catch:" + e.getClass().getName();
                    }

                } else {
                    Log.e("RequestCall", "Request failed");
                    message = "response not successful";

                }

                listener.onFailure(message);

            }

            @Override
            public void onFailure(Call<EventsResponse> call, Throwable t) {
                Log.e("RequestCall", "Request failed");
                listener.onFailure("Request failed");
            }
        });

    }


}
