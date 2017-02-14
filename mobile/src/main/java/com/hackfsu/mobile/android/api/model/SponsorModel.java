package com.hackfsu.mobile.android.api.model;


/**
 * Created by andrew on 11/14/16.
 */

public class SponsorModel extends BaseModel {

    String name;
    String imageURL;
    String webURL;
    int tier;
    int order;

    public SponsorModel(String name, String imageURL, String webURL, int tier, int order) {
        this.name = name;
        this.imageURL = imageURL;
        this.webURL = webURL;
        this.tier = tier;
        this.order = order;
    }

    public String getName() {
        return name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getWebURL() {
        return webURL;
    }

    public int getTier() {
        return tier;
    }

    public int getOrder() {
        return order;
    }
}
