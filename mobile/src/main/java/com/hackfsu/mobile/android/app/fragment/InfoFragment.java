package com.hackfsu.mobile.android.app.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hackfsu.mobile.android.api.API;
import com.hackfsu.mobile.android.api.model.MapModel;
import com.hackfsu.mobile.android.app.R;
import com.hackfsu.mobile.android.app.activity.MapViewActivity;
import com.squareup.picasso.Picasso;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class InfoFragment extends Fragment {


    Toolbar mToolbar;
    AppBarLayout mAppBar;
    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;

    SwipeRefreshLayout mSwipeLayout;

    BaseFragment.OnFragmentInteractionListener mListener;
    API mAPI;


    public static InfoFragment newInstance() {
        return new InfoFragment();
    }

    // Required empty public constructor
    public InfoFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info, container, false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    private void loadMaps() {
        mAPI.getMaps(new API.APICallback<MapModel>() {
            @Override
            public void onDataReady(List<MapModel> dataSet) {

            }
        });
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




}
