package com.hackfsu.android.api.util;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by andrew on 11/14/16.
 */

public class NetworkClient {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    OkHttpClient httpClient = new OkHttpClient();

    public void get(String url, final NetworkCallback callback) {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Call call = httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callback.onComplete(response.body().string());
            }
        });

    }

    public void post(String url, String json, NetworkCallback callback) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = httpClient.newCall(request).execute()) {
            callback.onComplete(response.body().string());
        } catch (Exception e) {
            callback.onFailure(e);
        }
    }

    public interface NetworkCallback {
        void onComplete(String json);

        void onFailure(Exception e);
    }


}
