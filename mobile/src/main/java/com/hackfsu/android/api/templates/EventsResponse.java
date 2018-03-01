package com.hackfsu.android.api.templates;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Randy Bruno-Piverger on 2/28/2018.
 */

public class EventsResponse {

    @SerializedName("events")
    public ArrayList<String> events;
}
