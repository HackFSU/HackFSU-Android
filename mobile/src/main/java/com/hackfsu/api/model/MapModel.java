package com.hackfsu.api.model;

/**
 * Created by andrew on 11/14/16.
 */

public class MapModel extends BaseModel {

    String label;
    String URL;
    int ordering;

    public MapModel(String label, String URL, int ordering) {
        this.label = label;
        this.URL = URL;
        this.ordering = ordering;
    }

    public String getLabel() {
        return label;
    }

    public String getURL() {
        return URL;
    }

    public int getOrdering() {
        return ordering;
    }

}
