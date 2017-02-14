package com.hackfsu.mobile.android.api.model;

import java.util.Calendar;

/**
 * Created by andrew on 11/14/16.
 */

public class CountdownModel extends BaseModel {

    String label;
    Calendar startTime;
    Calendar endTime;

    public CountdownModel(String label, Calendar startTime, Calendar endTime) {
        this.label = label;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getLabel() {
        return label;
    }

    public Calendar getStartTime() {
        return startTime;
    }

    public Calendar getEndTime() {
        return endTime;
    }
}
