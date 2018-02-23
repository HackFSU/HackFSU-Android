package com.hackfsu.android.app.fragment;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hackfsu.android.api.API;
import com.hackfsu.android.api.model.ScheduleModel;
import com.hackfsu.android.app.R;
//import com.parse.FindCallback;
//import com.parse.ParseClassName;
//import com.parse.ParseException;
//import com.parse.ParseObject;
//import com.parse.ParseQuery;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


public class ScheduleFragment extends BaseFragment {

    BaseFragment.OnFragmentInteractionListener mListener;
    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    ScheduleRecyclerAdapter mAdapter;
    SwipeRefreshLayout mSwipeLayout;
    API mAPI;

    public static ScheduleFragment newInstance() {
        return new ScheduleFragment();
    }

    // Required empty public constructor
    public ScheduleFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_schedule, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        mSwipeLayout = (SwipeRefreshLayout) v.findViewById(R.id.refresh_layout);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Register toolbar
        //  mToolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        //mCollapsing.setTitle("Schedule");
       // mListener.registerToolbar(mToolbar);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(false);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(getContext())
                        .colorResId(R.color.divider_dark)
                        .build()
        );

        // specify an adapter (see also next example)
        mAdapter = new ScheduleRecyclerAdapter(new ArrayList<ScheduleModel>());
        mRecyclerView.setAdapter(mAdapter);

        //
        mAPI = new API(getActivity());
        retrieveSchedule();

        // Swipe Reload
        mSwipeLayout.setColorSchemeResources(R.color.accent);
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                retrieveSchedule();
                mSwipeLayout.setRefreshing(false);
            }
        });

    }

    // Adapter used by this fragment
    private class ScheduleRecyclerAdapter extends
            RecyclerView.Adapter<ScheduleRecyclerAdapter.ViewHolder> {

        private List<ScheduleModel> mDataset;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public class ViewHolder extends RecyclerView.ViewHolder {
            public View card;
            public TextView mTitleText;
            public TextView mSubtitleText;
            public TextView mTimeText;
            public ViewHolder(View v) {
                super(v);
                card = v;
                mTitleText = (TextView) v.findViewById(R.id.tv_title);
                mSubtitleText = (TextView) v.findViewById(R.id.tv_subtitle);
                mTimeText = (TextView) v.findViewById(R.id.tv_time);
            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public ScheduleRecyclerAdapter(ArrayList<ScheduleModel> myDataset) {
            mDataset = myDataset;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public ScheduleRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                    int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.tile_schedule, parent, false);

            return new ViewHolder(v);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element

            // Display time
            SimpleDateFormat formatter = new SimpleDateFormat("EEE hh:mm a", Locale.US);
            formatter.setTimeZone(TimeZone.getTimeZone("EST"));
            String startTime = formatter.format(mDataset.get(position).getStart().getTime());


            holder.mTitleText.setText(mDataset.get(position).getName());
            holder.mTimeText.setText(startTime);

            String desc = mDataset.get(position).getDescription();

            if(!desc.isEmpty()) {
                holder.mSubtitleText.setText(desc);
                holder.mSubtitleText.setVisibility(View.VISIBLE);
            } else {
                holder.mSubtitleText.setVisibility(View.GONE);
            }

        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataset.size();
        }

        public void replaceDataset(List<ScheduleModel> data) {
            mDataset = data;

        }
    }

    public void retrieveSchedule() {

        mAPI.getSchedule(new API.APICallback<ScheduleModel>() {
            @Override
            public void onDataReady(List<ScheduleModel> dataSet) {
                mAdapter.replaceDataset(dataSet);
                mAdapter.notifyDataSetChanged();
            }
        });

    }


}
