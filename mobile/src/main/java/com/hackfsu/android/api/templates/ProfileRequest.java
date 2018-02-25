package com.hackfsu.android.api.templates;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Randy Bruno-Piverger on 2/25/2018.
 */

public class ProfileRequest {

    private String first_name;
    private String last_name;
    private String email;
    private String shirt_size;
    private String phone_number;
    private String diet;
    private String github;
    private String linkedin;
    private String rsvp_confirmed;
    private String checked_in;
    private String hexcode;
    private String qr;
    private ArrayList<String> groups;



    public String getFirst_name(){
        return first_name;
    }

    public String getLast_name(){
        return last_name;
    }

    public String getEmail(){
        return email;
    }

    public String getShirt_size(){

        return shirt_size;
    }
    
    public String getPhone_number(){
        return phone_number;
    }
    
    public String getDiet(){
    return diet;    
    }
    
    public String getGithub(){
        return github;
    }
    
    public String getLinkedin(){
      return linkedin;  
    }
    
    public String getRsvp_confirmed(){
        return rsvp_confirmed;
    }
    
    public String getChecked_in(){
        return checked_in;
    }
    
    public String getHexcode(){
        return hexcode;
    }
    
    public String getQr(){
        return  qr;
    }
    
    public ArrayList<String> getGroups(){
    return groups;
    }
    


}
