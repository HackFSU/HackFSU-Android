package com.hackfsu.hackfsu_android;

import android.content.Intent;
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

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Toolbar.OnMenuItemClickListener,
        BaseFragment.OnFragmentInteractionListener {

    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        /*// Toolbar stuff
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.inflateMenu(R.menu.menu_main);
        //setSupportActionBar(mToolbar);
        //mToolbar.setTitle("HackFSU");
        mToolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });
        mToolbar.setOnMenuItemClickListener(this);
        mToolbar.inflateMenu(R.menu.menu_main);*/

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_live);



        // Tab Layout setup
        /*mTabLayout = (TabLayout) findViewById(R.id.tabs);


        // View Pager setup
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(UpdateFragment.newInstance());
        fragments.add(UpdateFragment.newInstance());
        fragments.add(UpdateFragment.newInstance());
        fragments.add(UpdateFragment.newInstance());
        PagerAdapter mPagerAdapter = new PagerAdapter(super.getSupportFragmentManager(), fragments);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);*/

        FragmentManager fm = getSupportFragmentManager();
        FeedFragment ff = FeedFragment.newInstance();
        fm.beginTransaction().replace(R.id.fragment_anchor, ff).commit();








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

        if(item.getGroupId() == R.id.primary) {

            FragmentManager fm = getSupportFragmentManager();
            Fragment fg;

            switch (id) {
                case R.id.nav_live:
                    fg = FeedFragment.newInstance(); break;
                case R.id.nav_map:
                    fg = MapsFragment.newInstance(); break;
                default: return true;
            }

            updateUI(id);

            fm.beginTransaction().replace(R.id.fragment_anchor, fg).commit();

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        } else {
            switch (id) {
                case R.id.nav_help:
                    startActivity(new Intent(this, HelpActivity.class));
                    break;
                case R.id.nav_settings:
                    break;
            }
        }


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
}
