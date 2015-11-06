package com.andrewsosa.hackfsu_test;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class UpdateFragment extends Fragment {

    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;

    // TODO: Rename and change types and number of parameters
    public static UpdateFragment newInstance() {
        UpdateFragment fragment = new UpdateFragment();
        return fragment;
    }

    public UpdateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_update, container, false);


        mRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);


        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);


        // specify an adapter (see also next example)
        RecyclerAdapter mAdapter = new RecyclerAdapter(new String[]{"Hello",
                "World", "HackFSU","World", "HackFSU","World", "HackFSU","World", "HackFSU"});
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        //mRecyclerView.scrollToPosition(0);
    }
}
