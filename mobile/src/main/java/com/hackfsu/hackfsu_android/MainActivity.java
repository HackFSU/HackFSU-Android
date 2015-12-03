package com.hackfsu.hackfsu_android;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

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
        navHeader.setTypeface(Typeface.createFromAsset(getAssets(), "unisans.OTF"));

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        switch(id) {
            //case R.id.action_settings:
            //    return true;
            /*case R.id.action_countdown:
                RelativeLayout countdown = (RelativeLayout) findViewById(R.id.timer_box);

                if(countdown.getVisibility() == View.VISIBLE) {
                    countdown.setVisibility(View.GONE);
                } else {
                    countdown.setVisibility(View.VISIBLE);
                }

                return true;*/
        }


        return false;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if(item.getGroupId() == R.id.primary && shouldChangeFragments(id)) {

            newFragmentTransaction(id);

        } else if (item.getGroupId() == R.id.secondary) {
            switch (id) {
                case R.id.nav_help:
                    startActivity(new Intent(this, HelpActivity.class));
                    break;
                case R.id.nav_settings:
                    break;
            }
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
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
