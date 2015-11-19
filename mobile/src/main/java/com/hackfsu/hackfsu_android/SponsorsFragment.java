package com.hackfsu.hackfsu_android;


import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SponsorsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SponsorsFragment extends BaseFragment {

    Toolbar mToolbar;
    OnFragmentInteractionListener mListener;

    public static SponsorsFragment newInstance() {
        return new SponsorsFragment();
    }

    // Required empty public constructor
    public SponsorsFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_feed, container, false);

        mToolbar = (Toolbar) v.findViewById(R.id.toolbar);


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
