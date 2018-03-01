package com.hackfsu.android.api.templates;

import com.google.gson.annotations.SerializedName;
import com.hackfsu.android.api.model.EventModel;

import java.util.ArrayList;

/**
 * Created by Randy Bruno-Piverger on 2/28/2018.
 */

public class EventsResponse {

    @SerializedName("events")
    public ArrayList<EventModel> events;
}
