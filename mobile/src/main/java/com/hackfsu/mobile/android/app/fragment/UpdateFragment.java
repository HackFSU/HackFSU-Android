package com.hackfsu.mobile.android.app.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hackfsu.mobile.android.api.API;
import com.hackfsu.mobile.android.api.model.UpdateModel;
import com.hackfsu.mobile.android.app.R;
//import com.parse.FindCallback;
//import com.parse.ParseClassName;
//import com.parse.ParseException;
//import com.parse.ParseObject;
//import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


public class UpdateFragment extends BaseFragment {

    RecyclerView mRecyclerView;
    SwipeRefreshLayout mSwipeLayout;
    LinearLayoutManager mLayoutManager;
    UpdatesRecyclerAdapter mAdapter;
    View mEmptyView;
    API mAPI;

    public static UpdateFragment newInstance() {
        return new UpdateFragment();
    }

    // Required empty public constructor
    public UpdateFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_list_refresh, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        mSwipeLayout = (SwipeRefreshLayout) v.findViewById(R.id.refresh_layout);
        mEmptyView = v.findViewById(R.id.empty_view);
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

        // specify an adapter
        mAdapter = new UpdatesRecyclerAdapter(new ArrayList<UpdateModel>());
        mRecyclerView.setAdapter(mAdapter);

        // TODO load the data

        // Swipe Reload
        mSwipeLayout.setColorSchemeResources(R.color.accent);
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeLayout.setRefreshing(false);
                // TODO add a refresh action
            }
        });
    }

    // TODO add updateAnnouncements() method

    // Adapter used by this fragment
    private class UpdatesRecyclerAdapter extends
            RecyclerView.Adapter<UpdatesRecyclerAdapter.ViewHolder> {

        private List<UpdateModel> mDataset;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        class ViewHolder extends RecyclerView.ViewHolder {
            View card;
            TextView mTitleText;
            TextView mSubtitleText;
            TextView mContentText;
            ImageView mIcon;
            ViewHolder(View v) {
                super(v);
                card = v;
                mTitleText = (TextView) v.findViewById(R.id.tv_title);
                mSubtitleText = (TextView) v.findViewById(R.id.tv_subtitle);
                mContentText = (TextView) v.findViewById(R.id.tv_content);
                mIcon = (ImageView) v.findViewById(R.id.iv_icon);
            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        UpdatesRecyclerAdapter(List<UpdateModel> myDataset) {
            mDataset = myDataset;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public UpdatesRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.tile_update, parent, false);

            return new ViewHolder(v);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element

            // TODO implement this method

        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataset.size();
        }

        void replaceDataset(List<UpdateModel> data) {
            mDataset = data;
            notifyDataSetChanged();

            if(data.size() > 0) mEmptyView.setVisibility(View.INVISIBLE);
            else mEmptyView.setVisibility(View.VISIBLE);

        }
    }
}
