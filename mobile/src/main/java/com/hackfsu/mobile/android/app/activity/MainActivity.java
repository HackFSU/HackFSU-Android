package com.hackfsu.android.app.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.hackfsu.android.app.fragment.BaseFragment;
import com.hackfsu.android.app.fragment.FeedFragment;
import com.hackfsu.android.app.HackFSU;
import com.hackfsu.android.app.fragment.MapsFragment;
import com.hackfsu.android.app.R;
import com.hackfsu.android.app.fragment.SponsorsFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Toolbar.OnMenuItemClickListener,
        BaseFragment.OnFragmentInteractionListener {

    DrawerLayout drawer;
    NavigationView navigationView;

    String activeFragmentTag;
    int activeFragmentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initPrefs();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        // Restore or Init state
        navigationView.setNavigationItemSelectedListener(this);
        if(savedInstanceState != null) {
            restoreFragmentTransaction(savedInstanceState);
        } else {
            newFragmentTransaction(R.id.nav_live);
        }


        TextView navHeader = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tv_navtitle);
        navHeader.setText("HACKFSU");
        navHeader.setTypeface(Typeface.createFromAsset(getAssets(), getResources().getString(R.string.hackfsu_font)));

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

    @Override
    public void onBackPressed() {
        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        SharedPreferences sp = getSharedPreferences(HackFSU.PREFERENCES, MODE_PRIVATE);

        switch(id) {
            case R.id.action_countdown:
                boolean showCountdown = !sp.getBoolean(HackFSU.COUNTDOWN, true);
                Log.d("HackFSU", ""+showCountdown);
                item.setIcon(showCountdown ? R.drawable.ic_timer_24dp : R.drawable.ic_timer_off_white_24dp);
                sp.edit().putBoolean(HackFSU.COUNTDOWN, showCountdown).apply();
                break;
            case R.id.action_notifications:
                boolean allowNotifications = !sp.getBoolean(HackFSU.NOTIFICATIONS, true);
                Log.d("HackFSU", ""+allowNotifications);
                item.setIcon(allowNotifications ? R.drawable.ic_notifications_24dp : R.drawable.ic_notifications_off_24dp);
                sp.edit().putBoolean(HackFSU.NOTIFICATIONS, allowNotifications).apply();
                Snackbar.make(drawer, (allowNotifications ? "Notifications Enabled" : "Notifications Disabled"), Snackbar.LENGTH_SHORT).show();
                break;
        }

        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if(item.getGroupId() == R.id.primary && shouldChangeFragments(id)) {

            newFragmentTransaction(id);

            if(id == R.id.nav_sponsors) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
            }

        } else if (item.getGroupId() == R.id.secondary) {
            switch (id) {
//                case R.id.nav_help:
//                    startActivity(new Intent(this, HelpActivity.class));
//                    break;
                case R.id.nav_website:
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.hackfsu.com"));
                    startActivity(browserIntent);

            }
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void initPrefs() {
        SharedPreferences sp = getSharedPreferences(HackFSU.PREFERENCES, MODE_PRIVATE);
        if(!sp.contains(HackFSU.NOTIFICATIONS)) sp.edit().putBoolean(HackFSU.NOTIFICATIONS, true).apply();
        if(!sp.contains(HackFSU.COUNTDOWN)) sp.edit().putBoolean(HackFSU.COUNTDOWN, true).apply();
    }

    @Override
    public void registerToolbar(Toolbar toolbar) {

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });
        toolbar.setOnMenuItemClickListener(this);


    }

    public void updateUI(int id) {
        switch(id) {
            case R.id.nav_live:
                drawer.setStatusBarBackgroundColor(getResources().getColor(R.color.primaryDark));
                break;
            case R.id.nav_map:
                drawer.setStatusBarBackgroundColor(getResources().getColor(R.color.maps_dark));
                break;
            case R.id.nav_sponsors:
                drawer.setStatusBarBackgroundColor(getResources().getColor(R.color.sponsors_dark));
                break;
        }
    }

    public void newFragmentTransaction(int id) {

        // Be Prepared~!
        FragmentManager fm = getSupportFragmentManager();
        Fragment fg = null;

        // How can IDs be real if our eyes aren't real?
        switch (id) {
            case R.id.nav_live:
                fg = FeedFragment.newInstance(); break;
            case R.id.nav_map:
                fg = MapsFragment.newInstance(); break;
            case R.id.nav_sponsors:
                fg = SponsorsFragment.newInstance(); break;
        }

        // Actual cannibal shia transaction
        if(fg != null) {
            updateUI(id);
            activeFragmentTag = getFragmentTag(id);
            activeFragmentId = id;
            navigationView.setCheckedItem(R.id.nav_live);
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
            case R.id.nav_live: return "Live";
            case R.id.nav_map: return "Map";
            case R.id.nav_sponsors: return "Sponsors";
            default: return "";
        }
    }

    public boolean shouldChangeFragments(int id) {

        FragmentManager fm = getSupportFragmentManager();
        return (fm.findFragmentByTag(getFragmentTag(id)) == null);

    }
}
