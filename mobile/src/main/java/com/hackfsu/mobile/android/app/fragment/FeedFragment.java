package com.hackfsu.mobile.android.app.fragment;

import android.content.Context;
import android.content.SharedPreferences;
//import android.graphics.Typeface;
import android.graphics.Typeface;
import android.os.Bundle;
//import android.os.CountDownTimer;
//import android.support.design.widget.AppBarLayout;
//import android.support.design.widget.CollapsingToolbarLayout;
//import android.support.design.widget.TabLayout;
import android.os.CountDownTimer;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
//import android.util.Log;
import android.util.Log;
import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuItem;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

//import android.widget.TextView;

import com.hackfsu.mobile.android.api.API;
//import com.hackfsu.mobile.android.api.model.CountdownModel;
import com.hackfsu.mobile.android.api.model.CountdownModel;
import com.hackfsu.mobile.android.app.HackFSU;
import com.hackfsu.mobile.android.app.adapter.PagerAdapter;
import com.hackfsu.mobile.android.app.R;
//import com.parse.GetCallback;
//import com.parse.ParseClassName;
//import com.parse.ParseException;
//import com.parse.ParseObject;
//import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//import java.util.Locale;
//import java.util.concurrent.TimeUnit;

public class FeedFragment extends BaseFragment {

    private OnFragmentInteractionListener mListener;

    // View Items
    Toolbar mToolbar;
    TabLayout mTabLayout;
    ViewPager mViewPager;
    AppBarLayout mAppBar;
    CollapsingToolbarLayout mCollasping;
    TextView mCountdownLabel;
    TextView mCountdownTime;

    //boolean showCountdown;

    API mAPI;

    public static FeedFragment newInstance() {
        return new FeedFragment();
    }

    // Required empty public constructor
    public FeedFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_feed, container, false);

        mToolbar = (Toolbar) v.findViewById(R.id.toolbar);
        mTabLayout = (TabLayout) v.findViewById(R.id.tabs);
        mViewPager = (ViewPager) v.findViewById(R.id.viewpager);
        mAppBar = (AppBarLayout) v.findViewById(R.id.app_bar);
        mCollasping = (CollapsingToolbarLayout) v.findViewById(R.id.collapsing_toolbar);
        mCountdownLabel = (TextView) v.findViewById(R.id.tv_countdown_label);
        mCountdownTime = (TextView) v.findViewById(R.id.tv_countdown_time);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Toolbar stuff
        //toolbar.inflateMenu(R.menu.menu_main);
        //setSupportActionBar(mToolbar);
        //mToolbar.setTitle("HackFSU");
       // mToolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);  // Removing Toolbar icon for bottom nav
        mToolbar.inflateMenu(R.menu.menu_main);
        mListener.registerToolbar(mToolbar);

        // Init toolbar icons
        final SharedPreferences sp = getContext().getSharedPreferences(HackFSU.PREFERENCES, Context.MODE_PRIVATE);
        Menu menu = mToolbar.getMenu();
        MenuItem notifs = menu.findItem(R.id.action_notifications);
        notifs.setIcon(sp.getBoolean(HackFSU.NOTIFICATIONS, true) ?
                R.drawable.ic_notifications_24dp : R.drawable.ic_notifications_off_24dp);
        MenuItem countdown = menu.findItem(R.id.action_countdown);
            countdown.setIcon(sp.getBoolean(HackFSU.COUNTDOWN, true) ?
                R.drawable.ic_timer_24dp : R.drawable.ic_timer_off_white_24dp);


        // View Pager setup
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(UpdateFragment.newInstance());
        //Removing Schedule fragment from front page
        //fragments.add(ScheduleFragment.newInstance());
        PagerAdapter mPagerAdapter = new PagerAdapter(getChildFragmentManager(), fragments);
         mViewPager.setAdapter(mPagerAdapter);
        //mTabLayout.setupWithViewPager(mViewPager);

        if(savedInstanceState != null) {
            mViewPager.setCurrentItem(savedInstanceState.getInt("pos"));
        }


        // Custom toolbar font
        Typeface face;
        face = Typeface.createFromAsset(getContext().getAssets(), getResources().getString(R.string.hackfsu_font));
        mCollasping.setCollapsedTitleTypeface(face);
        mCollasping.setExpandedTitleTypeface(face);
        mCollasping.setTitle("HackFSU");

        mCountdownLabel.setTypeface(face);
        mCountdownTime.setTypeface(face);
        
        // Countdown

        mAPI = new API(getActivity());
        initNextTimer();
    }



    public void initNextTimer() {

        mAPI.getCountdowns(new API.APICallback<CountdownModel>() {
            @Override
            public void onDataReady(List<CountdownModel> dataSet) {

                Log.d("initNextTimer()", "onDataReady called");

                CountdownModel model = null;

                for (CountdownModel countdown : dataSet) {
                    if (countdown.getStartTime().getTimeInMillis() > System.currentTimeMillis()) {
                        model = countdown;
                        break;
                    }
                }


                if (model != null) {

                    final CountdownModel model2 = model;

                    long until = (model.getStartTime().getTimeInMillis() - System.currentTimeMillis());
                    Log.d("initNextTimer()", "ms until: " + until);


                    new CountDownTimer(until, 1000) {
                        @Override
                        public void onTick(long millis) {
                            String out = String.format(Locale.US, "%02d:%02d:%02d",
                                    TimeUnit.MILLISECONDS.toHours(millis),
                                    TimeUnit.MILLISECONDS.toMinutes(millis) -
                                            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                                    TimeUnit.MILLISECONDS.toSeconds(millis) -
                                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));

                            mCountdownTime.setText(out);
                            mCountdownLabel.setText(model2.getLabel());
                        }

                        @Override
                        public void onFinish() {
                            mCountdownTime.setText("HACKFSU");
                            mCountdownLabel.setText("");
                            initNextTimer();
                        }
                    }.start();

                } else {
                    mCountdownTime.setText("HACKFSU");
                }
            }
        });


//        ParseQuery<CountdownItem> query = new ParseQuery<CountdownItem>(ParseName.COUNTDOWNITEM);
//        query.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE);
//        query.whereGreaterThan(ParseName.COUNTDOWN_TIME, now);
//        query.getFirstInBackground(new GetCallback<CountdownItem>() {
//            @Override
//            public void done(final CountdownItem object, ParseException e) {
//
//                if(e == null && object != null) {
//                    mCountdownLabel.setText(object.getLabel());
//                    Log.d("HackFSU", "Object label: " + object.getLabel());
//                    Log.d("HackFSU", "Object time: " + object.getStart().toString());
//
//
//                    long until = (object.getStart().getStart() - System.currentTimeMillis());
//                    new CountDownTimer(until, 1000) {
//                        @Override
//                        public void onFinish() {
//                            mCountdownTime.setText("HackFSU");
//                            mCountdownLabel.setText("");
//                            initNextTimer();
//                        }
//
//                        @Override
//                        public void onTick(long millis) {
//                            String out = String.format("%02d:%02d:%02d",
//                                    TimeUnit.MILLISECONDS.toHours(millis),
//                                    TimeUnit.MILLISECONDS.toMinutes(millis) -
//                                            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
//                                    TimeUnit.MILLISECONDS.toSeconds(millis) -
//                                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
//
//                            mCountdownTime.setText(out);
//
//                        }
//                    }.start();
//                } else if(object == null) {
//                    mCountdownTime.setText("HackFSU");
//                    mCountdownLabel.setText("");
//                } else if(e != null) {
//                    Log.e("HackFSU", e.getMessage());
//                }
//            }
//        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("pos", mViewPager.getCurrentItem());
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


//    @ParseClassName(ParseName.COUNTDOWNITEM)
//    public static class CountdownItem extends ParseObject {
//
//        public CountdownItem(){}
//
//        public String getLabel() {
//            return getString(ParseName.COUNTDOWN_LABEL);
//        }
//
//        public Date getStart() {
//            return getDate(ParseName.COUNTDOWN_TIME);
//        }
//
//
//    }

}
