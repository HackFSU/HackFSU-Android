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
import android.widget.TextView;


public class ScheduleFragment extends BaseFragment {

    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;

    // TODO: Rename and change types and number of parameters
    public static UpdateFragment newInstance() {
        UpdateFragment fragment = new UpdateFragment();
        return fragment;
    }

    public ScheduleFragment() {
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
        ScheduleRecyclerAdapter mAdapter = new ScheduleRecyclerAdapter(new String[]{"Hello",
                "World", "HackFSU","World", "HackFSU","World", "HackFSU","World", "HackFSU"});
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        //mRecyclerView.scrollToPosition(0);
    }

    // Adapter used by this fragment
    private static class ScheduleRecyclerAdapter extends
            RecyclerView.Adapter<ScheduleRecyclerAdapter.ViewHolder> {

        private String[] mDataset;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public static class ViewHolder extends RecyclerView.ViewHolder {
            public View card;
            public TextView mTextView;
            public ViewHolder(View v) {
                super(v);
                card = v;
                mTextView = (TextView) v.findViewById(R.id.update_title);
            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public ScheduleRecyclerAdapter(String[] myDataset) {
            mDataset = myDataset;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public ScheduleRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                    int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.update, parent, false);

            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            holder.mTextView.setText(mDataset[position]);

        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataset.length;
        }
    }
}
