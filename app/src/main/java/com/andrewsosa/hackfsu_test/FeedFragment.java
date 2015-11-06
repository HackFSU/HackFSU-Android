package com.andrewsosa.hackfsu_test;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
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
public class FeedFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    // View Items
    Toolbar mToolbar;
    TabLayout mTabLayout;
    ViewPager mViewPager;

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
        ArrayList<android.support.v4.app.Fragment> fragments = new ArrayList<>();
        fragments.add(UpdateFragment.newInstance());
        fragments.add(UpdateFragment.newInstance());
        //fragments.add(UpdateFragment.newInstance());
        //fragments.add(UpdateFragment.newInstance());
        PagerAdapter mPagerAdapter = new PagerAdapter(getChildFragmentManager(), fragments);
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {

        void registerToolbar(Toolbar toolbar);

    }

}
