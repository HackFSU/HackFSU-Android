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
 */
public class MapsFragment extends BaseFragment {

    private BaseFragment.OnFragmentInteractionListener mListener;

    // View Items
    Toolbar mToolbar;
    TabLayout mTabLayout;
    ViewPager mViewPager;


    public static MapsFragment newInstance() {
        return new MapsFragment();
    }

    public MapsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_maps, container, false);

        mToolbar = (Toolbar) v.findViewById(R.id.toolbar);



        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        mToolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        //mToolbar.inflateMenu(R.menu.menu_main);
        mToolbar.setTitle("Venue Map");
        mListener.registerToolbar(mToolbar);

    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {
            mListener = (BaseFragment.OnFragmentInteractionListener) activity;
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
    /*public interface OnFragmentInteractionListener {

        void registerToolbar(Toolbar toolbar);

    }*/
}
