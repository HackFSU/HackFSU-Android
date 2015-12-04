package com.hackfsu.hackfsu_android;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;


public class HackFSU extends Application {

    public static final String PREFERENCES = "preferences";
    public static final String NOTIFICATIONS = "notifications";
    public static final String COUNTDOWN = "countdown";

    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(ScheduleFragment.ScheduleItem.class);
        ParseObject.registerSubclass(ScheduleFragment.ScheduleDivider.class);
        ParseObject.registerSubclass(UpdateFragment.UpdateItem.class);
        //Parse.enableLocalDatastore(this);
        Parse.initialize(this, "7MgItVIkvSmADkIdIVPmEbIOOZQ84ilW224wXsgS", "hHoLbbe3SWIzt6JiXaNY5gdPQ47QBGH6AlbHHTih");
        ParseInstallation.getCurrentInstallation().saveInBackground();
        ParsePush.subscribeInBackground("announcements");
    }
}
