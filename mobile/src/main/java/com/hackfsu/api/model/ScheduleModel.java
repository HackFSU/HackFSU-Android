package com.hackfsu.api.model;

import java.util.Calendar;

/**
 * Created by andrew on 11/14/16.
 */

public class ScheduleModel extends BaseModel {

    String name;
    String location;
    Calendar time;

    public ScheduleModel(String name, String location, Calendar time) {
        this.name = name;
        this.location = location;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public Calendar getTime() {
        return time;
    }

}
