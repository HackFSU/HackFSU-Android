package com.hackfsu.android.api;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hackfsu.android.api.templates.HacksResponse;
import com.hackfsu.android.api.util.AddCookiesInterceptor;
import com.hackfsu.android.api.util.ReceivedCookiesInterceptor;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class JudgeAPI extends API {

    public JudgeAPI(Activity mActivity) {
        super(mActivity);
    }


    public interface OnAssignmentRetrievedListener {
        void onAssignment(ArrayList<Integer> tableNumbers, String expoString,
                          ArrayList<String> superlatives);
        void onFailure();
    }


    public void getHackAssignment(final OnAssignmentRetrievedListener listener) {

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
        Call<HacksResponse> call = mapi.GetHacks(new AddCookiesInterceptor(mActivity));


        call.enqueue(new Callback<HacksResponse>() {

            @SuppressWarnings("ConstantConditions")
            @Override
            public void onResponse(Call<HacksResponse> call, Response<HacksResponse> response) {

                if (response.isSuccessful() && response.body() != null) {

                    Log.d("RequestCall", "Request Successful");
                    Log.d(this.getClass().getName(), "Response code: " + response.code());

                    try {
                        // Convert hacks to ints
                        ArrayList<Integer> hackNumbers = new ArrayList<>();
                        for (String s : response.body().getHacks()) {
                            hackNumbers.add(Integer.parseInt(s));
                        }

                        listener.onAssignment(hackNumbers, response.body().getExpo(),
                                response.body().getSuperlatives());
                        return;
                    }
                    catch (NullPointerException e) {
                        Log.e(JudgeAPI.class.getName(), e.getLocalizedMessage());
                    }

                } else {
                    Log.e("RequestCall", "Request failed");

                }

                listener.onFailure();

            }

            @Override
            public void onFailure(Call<HacksResponse> call, Throwable t) {
                Log.e("RequestCall", "Request failed");
                listener.onFailure();
            }
        });

    }

    public interface OnSubmitAssignmentListener {
        void onSuccess();
        void onFailure();
    }

    private class JudgingResults {
        HashMap<String, Integer> order;
        HashMap<Integer, ArrayList<String>> superlatives;

        public JudgingResults() {
        }
    }


    public void submitJudgingAssignent(HashMap<String, Integer> order,
                                       HashMap<Integer, ArrayList<String>> superlatives,
                                       final OnSubmitAssignmentListener listener) {


        JudgingResults results = new JudgingResults();
        results.order = order;
        results.superlatives = superlatives;
        String json = new Gson().toJson(results);


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

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);

        //SENDING HACKS
        mapi.postHacks(requestBody).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    Toast.makeText(mActivity, "Hacks Sent Successfully, Response code: " + response.code(),
                            Toast.LENGTH_LONG).show();

                    listener.onSuccess();
                    return;


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

                listener.onFailure();

            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


                Toast.makeText(mActivity, "Submission Failed",
                        Toast.LENGTH_LONG).show();

                listener.onFailure();

            }

        });
    }

}
