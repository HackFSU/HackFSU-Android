package com.hackfsu.mobile.android.app.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.hackfsu.mobile.android.app.fragment.BaseFragment;
import com.hackfsu.mobile.android.app.fragment.FeedFragment;
import com.hackfsu.mobile.android.app.HackFSU;
import com.hackfsu.mobile.android.app.fragment.MapsFragment;
import com.hackfsu.mobile.android.app.R;
import com.hackfsu.mobile.android.app.fragment.ProfileFragment;
import com.hackfsu.mobile.android.app.fragment.ScheduleFragment;

public class MainActivity extends AppCompatActivity
        implements BaseFragment.OnFragmentInteractionListener {

    //DrawerLayout drawer;
    //NavigationView navigationView;

    String activeFragmentTag;
    int activeFragmentId;

    class JudgingData{
        String first;
        String second;
        String third;
        String fs;
        String ss;
        String ts;

        boolean complete;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initPrefs();


        // Restore or Init state
        //navigationView.setNavigationItemSelectedListener(this);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                newFragmentTransaction(item.getItemId());
               // gotoschedule(item.getItemId());
                return true;
            }
        });

        if (savedInstanceState != null) {
            restoreFragmentTransaction(savedInstanceState);
        } else {
            newFragmentTransaction(R.id.nav_live);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's instance
        /*FragmentManager fm = getSupportFragmentManager();
        Fragment fg = fm.findFragmentByTag(activeFragmentTag);
        fm.putFragment(outState, "fragment", fg);
        outState.putString("tag", activeFragmentTag); */
        outState.putInt("id", activeFragmentId);

    }

    //@Override
    public boolean onMenuItemClick(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        SharedPreferences sp = getSharedPreferences(HackFSU.PREFERENCES, MODE_PRIVATE);

        switch (id) {
            case R.id.action_countdown:
                boolean showCountdown = !sp.getBoolean(HackFSU.COUNTDOWN, true);
                Log.d("HackFSU", "" + showCountdown);
                item.setIcon(showCountdown ? R.drawable.ic_timer_24dp : R.drawable.ic_timer_off_white_24dp);
                sp.edit().putBoolean(HackFSU.COUNTDOWN, showCountdown).apply();
                break;
            case R.id.action_notifications:
                boolean allowNotifications = !sp.getBoolean(HackFSU.NOTIFICATIONS, true);
                Log.d("HackFSU", "" + allowNotifications);
                item.setIcon(allowNotifications ? R.drawable.ic_notifications_24dp : R.drawable.ic_notifications_off_24dp);
                sp.edit().putBoolean(HackFSU.NOTIFICATIONS, allowNotifications).apply();
                // Snackbar.make(drawer, (allowNotifications ? "Notifications Enabled" : "Notifications Disabled"), Snackbar.LENGTH_SHORT).show();
                break;
        }

        return true;
    }

    public void initPrefs() {
        SharedPreferences sp = getSharedPreferences(HackFSU.PREFERENCES, MODE_PRIVATE);
        if (!sp.contains(HackFSU.NOTIFICATIONS))
            sp.edit().putBoolean(HackFSU.NOTIFICATIONS, true).apply();
        if (!sp.contains(HackFSU.COUNTDOWN)) sp.edit().putBoolean(HackFSU.COUNTDOWN, true).apply();
    }

    @Override
    public void registerToolbar(Toolbar toolbar) {


    }


    public void newFragmentTransaction(int id) {

        // Be Prepared~!
        FragmentManager fm = getSupportFragmentManager();
        Fragment fg = null;

        // How can IDs be real if our eyes aren't real?
        switch (id) {
            case R.id.nav_live:
                fg = FeedFragment.newInstance();
                break;
            case R.id.nav_schedule:
                fg = ScheduleFragment.newInstance();
                break;
            case R.id.nav_map:
                fg = MapsFragment.newInstance();
                break;
            case R.id.nav_profile:
                fg = ProfileFragment.newInstance();
                break;

        }

        // Actual cannibal shia transaction
        if (fg != null) {
            activeFragmentTag = getFragmentTag(id);
            activeFragmentId = id;
            // navigationView.setCheckedItem(R.id.nav_live);
            fm.beginTransaction().replace(R.id.fragment_anchor, fg, activeFragmentTag).commit();
        }
    }

    public void restoreFragmentTransaction(Bundle savedInstanceState) {

        activeFragmentId = savedInstanceState.getInt("id");
        newFragmentTransaction(activeFragmentId);

        /* Fuck all of this
        // Prepare Fragment
        FragmentManager fm = getSupportFragmentManager();
        Fragment fg = fm.getFragment(savedInstanceState, "fragment");

        // Handle the metadata updates
        activeFragmentId = savedInstanceState.getInt("id");
        activeFragmentTag = getFragmentTag(activeFragmentId);
        navigationView.setCheckedItem(activeFragmentId);
        updateUI(activeFragmentId);

        // Just do it!!
        fm.beginTransaction().replace(R.id.fragment_anchor, fg, activeFragmentTag).commit();
        */
    }

    public String getFragmentTag(int id) {

        switch (id) {
            case R.id.nav_live:
                return "Live";
            case R.id.nav_map:
                return "Map";
            default:
                return "";
        }
    }

    public boolean shouldChangeFragments(int id) {

        FragmentManager fm = getSupportFragmentManager();
        return (fm.findFragmentByTag(getFragmentTag(id)) == null);

    }

}
