package com.hackfsu.android.api.model;


/**
 * Created by andrew on 11/14/16.
 */

public class SponsorModel extends BaseModel {

    String name;
    String URL;
    int ordering;

    public SponsorModel(String name, String URL, int ordering) {
        this.name = name;
        this.URL = URL;
        this.ordering = ordering;
    }

    public String getName() {
        return name;
    }

    public String getURL() {
        return URL;
    }

    public int getOrdering() {
        return ordering;
    }

}
