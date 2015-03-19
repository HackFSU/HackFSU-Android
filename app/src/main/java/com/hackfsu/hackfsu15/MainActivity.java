package com.hackfsu.hackfsu15;


import android.app.ActionBar;
import android.app.FragmentTransaction;

import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;


/**
 * Main activity for the application.
 * Uses Tab view.
 * 
 * @author Iosif
 * 
 */
public class    MainActivity extends FragmentActivity
        implements CountdownFragment.OnFragmentInteractionListener {

    // used to store app title
    private CharSequence mTitle;

    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    //Pager Widget, Handles animation and allows swiping horizontally.
    private ViewPager mPager;

    //Provides pages to the view pager widget.
    private mainPagerAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pager);

        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        mTitle = getTitle();

        // nav drawer icons from resources
        // MIGHT BE REMOVED W/ THE NEW TABS
        //navMenuIcons = getResources()
        //        .obtainTypedArray(R.array.nav_drawer_icons);

        mAdapter = new mainPagerAdapter(getSupportFragmentManager());
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        //ActionBar for Tabs
        final ActionBar actionBar = getActionBar();

        //Specifies that tabs should be displayed in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);



        //Listener for Tab Click
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                mPager.setCurrentItem(tab.getPosition());

            }

            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
                //Hide given tab.
            }

            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
            }
        };

        //Listener For Tab Swipe
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        // When swiping between pages, select the
                        // corresponding tab.
                        getActionBar().setSelectedNavigationItem(position);
                    }
                });



        for (int i = 0; i < 5; i++) {
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(navMenuTitles[i])
                            .setTabListener(tabListener));
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    /**
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    public void onFragmentInteraction(Uri uri) {
    }

}