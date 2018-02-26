package com.hackfsu.android.api.templates;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Randy Bruno-Piverger on 2/25/2018.
 */

public class ProfileResponse {



        @SerializedName("first_name")
        @Expose
        public String first_name;
        @SerializedName("last_name")
        @Expose
        public String last_name;
        @SerializedName("email")
        @Expose
        public String email;
        @SerializedName("shirt_size")
        @Expose
        public String shirt_size;
        @SerializedName("phone_number")
        @Expose
        public String phone_number;
        @SerializedName("diet")
        @Expose
        public String diet;
        @SerializedName("github")
        @Expose
        public String github;
        @SerializedName("linkedin")
        @Expose
        public String linkedin;
        @SerializedName("rsvp_confirmed")
        @Expose
        public String rsvp_confirmed;
        @SerializedName("checked_in")
        @Expose
        public String checked_in;
        @SerializedName("hexcode")
        @Expose
        public String hexcode;
        @SerializedName("qr")
        @Expose
        public String qr;
        @SerializedName("groups")
        public ArrayList<String> groups;



}
