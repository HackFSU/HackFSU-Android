package com.hackfsu.api;

import java.io.IOException;

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

    public void get(String url, NetworkCallback callback) {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            callback.onComplete(response.body().string());
        } catch (Exception e) {
            callback.onFailure(e);
        }

        // TODO consider sample code from stackoverflow
        // http://stackoverflow.com/questions/28221555/how-does-okhttp-get-json-string

//        String jsonData = responses.body().string();
//        JSONObject Jobject = new JSONObject(jsonData);
//        JSONArray Jarray = Jobject.getJSONArray("employees");
//
//        for (int i = 0; i < Jarray.length(); i++) {
//            JSONObject object     = Jarray.getJSONObject(i);
//        }
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
