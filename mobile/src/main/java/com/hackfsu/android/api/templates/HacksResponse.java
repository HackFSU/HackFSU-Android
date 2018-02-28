package com.hackfsu.android.api.templates;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Randy Bruno-Piverger on 2/26/2018.
 */

public class HacksResponse {

    @SerializedName("hacks")
    public ArrayList<String> hacks;
    @SerializedName("superlatives")
    public ArrayList<String> superlatives;
    @SerializedName("expo")
    @Expose
    public String expo;

    public ArrayList<String> getHacks() {
        return hacks;
    }

    public ArrayList<String> getSuperlatives() {
        return superlatives;
    }

    public String getExpo() {
        return expo;
    }
}
