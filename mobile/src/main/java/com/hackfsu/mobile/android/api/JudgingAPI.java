package com.hackfsu.mobile.android.api;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.SparseArray;

import com.hackfsu.mobile.android.api.util.NetworkClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by andrew on 11/2/17.
 */

public class JudgingAPI extends API {

    final static String URL = URL_BASE + "/judge/hacks";

    public JudgingAPI(Activity mActivity) {
        super(mActivity);
    }

    public interface GetHacksCallback {
        void onDataReady(List<Integer> hacks, List<String> superlatives);
        void onFailure();
    }

    public interface PostResultsCallback {
        void onSuccess();
        void onFailure();
    }

    public void getHacks(final GetHacksCallback callback) {

        /* Sample JSON Format
            {
                "hacks": [22, 23, 24],
                "superlatives": [
                    "Best Veteran Team",
                    "Best Novice Team",
                    "Funniest Hack",
                    "..."
                ]
            }
         */

        networkClient.get(URL, new NetworkClient.NetworkCallback() {
            @Override
            public void onComplete(String response) {
                JSONObject responseJSON;
                JSONArray hacksJSON;
                JSONArray superlativesJSON;
                List<Integer> hacks = new ArrayList<Integer>();
                List<String> superlatives = new ArrayList<String>();

                try {
                    responseJSON = new JSONObject(response);
                    hacksJSON = responseJSON.getJSONArray("hacks");
                    superlativesJSON = responseJSON.getJSONArray("superlatives");

                    for(int i = 0; i < hacksJSON.length(); ++i) {
                        hacks.add(hacksJSON.getInt(i));
                    }

                    for(int i = 0; i < superlativesJSON.length(); ++i) {
                        superlatives.add(superlativesJSON.getString(i));
                    }

                    callback.onDataReady(hacks, superlatives);


                } catch (JSONException e) {
                    callback.onFailure();
                }
            }

            @Override
            public void onFailure(Exception e) {
                callback.onFailure();
            }
        });

    }

    public void postResults(HashMap<Integer, Integer> hacks,
                            HashMap<Integer, List<String>> superlatives,
                            final PostResultsCallback callback) {

        /* Sample JSON Format
            {
                "order": {
                    "1": 23,
                    "2": 24,
                    "3": 22
                },
                "superlatives": {
                    "22": ["Best Veteran Team"]
                 }
            }
         */

        JSONObject payloadJSON = new JSONObject();

        // Automatically convert hack ordering
        JSONObject hacksJSON = new JSONObject(hacks);

        // Handle the nested array of superlative nominations
        @SuppressLint("UseSparseArrays")
        HashMap<Integer, JSONArray> nominations = new HashMap<>();

        for(Integer hack : superlatives.keySet()) {
            List<String> noms = superlatives.get(hack);
            JSONArray nomsJSON = new JSONArray(noms);
            nominations.put(hack, nomsJSON);
        }

        JSONObject superlativesJSON = new JSONObject(nominations);

        // Assemble payload
        try {
            payloadJSON.put("order", hacksJSON);
            payloadJSON.put("superlatives", superlativesJSON);
        } catch (JSONException e) {
            callback.onFailure();
        }

        String payload = payloadJSON.toString();


        networkClient.post(URL, payload, new NetworkClient.NetworkCallback() {
            @Override
            public void onComplete(String json) {
                callback.onSuccess();
            }

            @Override
            public void onFailure(Exception e) {
                callback.onFailure();
            }
        });




    }

}
