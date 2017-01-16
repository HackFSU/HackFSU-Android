package com.hackfsu.android.app;

import android.app.Application;

import com.hackfsu.android.app.fragment.FeedFragment;
import com.hackfsu.android.app.fragment.MapsFragment;
import com.hackfsu.android.app.fragment.ScheduleFragment;
import com.hackfsu.android.app.fragment.SponsorsFragment;
import com.hackfsu.android.app.fragment.UpdateFragment;
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
        ParseObject.registerSubclass(SponsorsFragment.Sponsor.class);
        ParseObject.registerSubclass(MapsFragment.MapItem.class);
        ParseObject.registerSubclass(FeedFragment.CountdownItem.class);

        Parse.initialize(this, "7MgItVIkvSmADkIdIVPmEbIOOZQ84ilW224wXsgS", "hHoLbbe3SWIzt6JiXaNY5gdPQ47QBGH6AlbHHTih");
        ParseInstallation.getCurrentInstallation().saveInBackground();

        ParsePush.subscribeInBackground("updates");
    }
}
