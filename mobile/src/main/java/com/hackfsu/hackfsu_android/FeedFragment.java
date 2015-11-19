package com.hackfsu.hackfsu_android;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FeedFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FeedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FeedFragment extends BaseFragment {

    private OnFragmentInteractionListener mListener;

    // View Items
    Toolbar mToolbar;
    TabLayout mTabLayout;
    ViewPager mViewPager;
    AppBarLayout mAppBar;
    CollapsingToolbarLayout mCollasping;

    public static FeedFragment newInstance() {
        return new FeedFragment();
    }

    public FeedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_feed, container, false);

        mToolbar = (Toolbar) v.findViewById(R.id.toolbar);
        mTabLayout = (TabLayout) v.findViewById(R.id.tabs);
        mViewPager = (ViewPager) v.findViewById(R.id.viewpager);
        mAppBar = (AppBarLayout) v.findViewById(R.id.action_bar);
        mCollasping = (CollapsingToolbarLayout) v.findViewById(R.id.collapsing_toolbar);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Toolbar stuff
        //toolbar.inflateMenu(R.menu.menu_main);
        //setSupportActionBar(mToolbar);
        //mToolbar.setTitle("HackFSU");
        mToolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        mToolbar.inflateMenu(R.menu.menu_main);
        mListener.registerToolbar(mToolbar);



        // View Pager setup
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(UpdateFragment.newInstance());
        fragments.add(ScheduleFragment.newInstance());
        PagerAdapter mPagerAdapter = new PagerAdapter(getChildFragmentManager(), fragments);
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        AppBarLayout.OnOffsetChangedListener barListener = new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if(mCollasping.getHeight() + verticalOffset < 2 * ViewCompat.getMinimumHeight(mCollasping)) {
                    //hello.animate().alpha(1).setDuration(600);
                } else {
                    //hello.animate().alpha(0).setDuration(600);
                }
            }
        };

        mAppBar.addOnOffsetChangedListener(barListener);

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

}
