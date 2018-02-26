package com.hackfsu.android.app.activity;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import android.support.annotation.NonNull;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter;
import com.hackfsu.android.app.fragment.BaseFragment;
import com.hackfsu.android.app.fragment.FeedFragment;
import com.hackfsu.android.app.fragment.InfoFragment;
import com.hackfsu.android.app.R;
import com.hackfsu.android.app.fragment.MapsFragment;
import com.hackfsu.android.app.fragment.ProfileFragment;
import com.hackfsu.android.app.fragment.ScheduleFragment;

public class MainActivity extends AuthActivity
        implements BaseFragment.OnFragmentInteractionListener {

    final static String Test_Base = "https://testapi.hackfsu.com/";
    final static String Test_Login = "api/user/login";
    final static String Test_Profile = "api/user/get/profile";
    final static String Test_getHacks = "api/judge/hacks";
    final static String Test_sendHacks = "api/judge/hacks/upload";

    String activeFragmentTag;
    int activeFragmentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Restore or Init state
        final AHBottomNavigation bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);
        bottomNavigation.setBehaviorTranslationEnabled(false);
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);

        AHBottomNavigationAdapter adapter = new AHBottomNavigationAdapter(this, R.menu.nav_main);
        adapter.setupWithBottomNavigation(bottomNavigation);

        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override

            public boolean onTabSelected(int position, boolean wasSelected) {
                int id = R.id.nav_live;
                int src = R.drawable.livebar;
                switch (position) {
                    case 1:
                        id = R.id.nav_schedule;
                        src = R.drawable.schedulebar;
                        break;
                    case 2:
                        id= R.id.nav_map;
                        src=R.drawable.infobar;
                        break;
                    case 3:
                        id = R.id.nav_profile;
                        src = R.drawable.profilebar;
                        break;
                }
                newFragmentTransaction(id);
                swapHeaderDrawable(src);
            return true;
            }

            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                newFragmentTransaction(item.getItemId());
                // gotoschedule(item.getItemId());

                return true;
            }
        });

        Typeface face;
        face = Typeface.createFromAsset(this.getAssets(), getResources().getString(R.string.hackfsu_font));

        if (savedInstanceState != null) {
            restoreFragmentTransaction(savedInstanceState);
        } else {
            newFragmentTransaction(R.id.nav_live);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("id", activeFragmentId);
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
                fg= MapsFragment.newInstance();
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
    }

    public String getFragmentTag(int id) {

        switch (id) {
            case R.id.nav_live:
                return "Live";
            default:
                return "";
        }
    }

    public void swapHeaderDrawable(int src) {
        ImageView imageView = (ImageView) findViewById(R.id.iv_toolbar_bg);
        imageView.setImageResource(src);
    }


}
