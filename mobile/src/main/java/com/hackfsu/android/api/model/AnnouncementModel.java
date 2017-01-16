package com.hackfsu.android.api.model;

import java.util.Calendar;

/**
 * Created by andrew on 11/14/16.
 */

public class AnnouncementModel extends BaseModel {

    String title;
    String content;
    Calendar time;

    public AnnouncementModel(String title, String content, Calendar time) {
        this.title = title;
        this.content = content;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Calendar getTime() {
        return time;
    }

}
