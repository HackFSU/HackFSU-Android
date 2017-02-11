package com.hackfsu.mobile.android.api.model;

import java.util.Calendar;

/**
 * Created by andrew on 11/14/16.
 */

/*
        JSON Format Sample
        [
            {
              "name": "Opening Ceremony",
              "end": "2017-02-18T00:00:00+00:00",
              "type": 0,
              "start": "2017-02-17T23:00:00+00:00",
              "description": ""
            }
        ]
 */


public class ScheduleModel extends BaseModel {

    String name;
    Calendar start;
    Calendar end;
    String description;
    int type;

    public ScheduleModel() {
    }

    public ScheduleModel(String name, Calendar start, Calendar end, String description, int type) {
        this.name = name;
        this.start = start;
        this.end = end;
        this.description = description;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Calendar getStart() {
        return start;
    }

    public Calendar getEnd() {
        return end;
    }

    public String getDescription() {
        return description;
    }

    public int getType() {
        return type;
    }
}
