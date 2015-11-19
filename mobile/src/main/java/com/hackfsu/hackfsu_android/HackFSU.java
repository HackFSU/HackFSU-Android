package com.hackfsu.hackfsu_android;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;


public class HackFSU extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "7MgItVIkvSmADkIdIVPmEbIOOZQ84ilW224wXsgS", "hHoLbbe3SWIzt6JiXaNY5gdPQ47QBGH6AlbHHTih");
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }
}
