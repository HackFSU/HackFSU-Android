package com.hackfsu.android.hackfsu;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


public class UpdateFragment extends BaseFragment {

    RecyclerView mRecyclerView;
    SwipeRefreshLayout mSwipeLayout;
    LinearLayoutManager mLayoutManager;
    UpdatesRecyclerAdapter mAdapter;
    View mEmptyView;

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


        // specify an adapter (see also next example)
        mAdapter = new UpdatesRecyclerAdapter(new ArrayList<UpdateItem>());
        mRecyclerView.setAdapter(mAdapter);


        // Initial Load
        ParseQuery<UpdateItem> query = ParseQuery.getQuery(ParseName.UPDATE);
        query.orderByDescending("createdAt");
        query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK); // !!!

        mSwipeLayout.post(new Runnable() {
            // Show that we're loading if slow
            @Override
            public void run() {
                mSwipeLayout.setRefreshing(true);
            }
        });

        query.findInBackground(new FindCallback<UpdateItem>() {
            @Override
            public void done(List<UpdateItem> list, ParseException e) {
                if (e != null) {
                    Log.e("HackFSU", e.getMessage());
                } else {
                    mAdapter.notifyItemRangeRemoved(0, mAdapter.getItemCount());
                    mAdapter.replaceDataset(list);
                    mAdapter.notifyItemRangeInserted(0, mAdapter.getItemCount());
                    mSwipeLayout.setRefreshing(false);
                }
            }
        });


        // Swipe Reload
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ParseQuery<UpdateItem> query = ParseQuery.getQuery(ParseName.UPDATE);
                query.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ONLY);
                query.orderByDescending("createdAt");
                query.findInBackground(new FindCallback<UpdateItem>() {
                    @Override
                    public void done(List<UpdateItem> list, ParseException e) {
                        if (e != null) {
                            Log.e("HackFSU", e.getMessage());
                            Snackbar.make(mRecyclerView, "Could not refresh.", Snackbar.LENGTH_SHORT)
                                    .show();
                        } else {
                            mAdapter.notifyItemRangeRemoved(0, mAdapter.getItemCount());
                            mAdapter.replaceDataset(list);
                            mAdapter.notifyItemRangeInserted(0, mAdapter.getItemCount());
                        }
                        mSwipeLayout.setRefreshing(false);
                    }
                });
            }
        });
        mSwipeLayout.setColorSchemeResources(R.color.accent);
    }


    // Adapter used by this fragment
    private class UpdatesRecyclerAdapter extends
            RecyclerView.Adapter<UpdatesRecyclerAdapter.ViewHolder> {

        private List<UpdateItem> mDataset;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public class ViewHolder extends RecyclerView.ViewHolder {
            public View card;
            public TextView mTitleText;
            public TextView mSubtitleText;
            public TextView mContentText;
            public ImageView mIcon;
            public ViewHolder(View v) {
                super(v);
                card = v;
                mTitleText = (TextView) v.findViewById(R.id.tv_title);
                mSubtitleText = (TextView) v.findViewById(R.id.tv_subtitle);
                mContentText = (TextView) v.findViewById(R.id.tv_content);
                mIcon = (ImageView) v.findViewById(R.id.iv_icon);
            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public UpdatesRecyclerAdapter(List<UpdateItem> myDataset) {
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

            holder.mTitleText.setText(mDataset.get(position).getTitle());
            holder.mContentText.setText(mDataset.get(position).getContent());

            SimpleDateFormat formatter = new SimpleDateFormat("EEE h:mm a", Locale.US);
            formatter.setTimeZone(TimeZone.getTimeZone("EST"));
            String timeStamp = formatter.format(mDataset.get(position).getTimestamp());
            holder.mSubtitleText.setText(timeStamp);

        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataset.size();
        }

        public void replaceDataset(List<UpdateItem> data) {
            mDataset = data;

            if(data.size() > 0) mEmptyView.setVisibility(View.INVISIBLE);
            else mEmptyView.setVisibility(View.VISIBLE);

        }
    }

    @ParseClassName("Update")
    public static class UpdateItem extends ParseObject {

        public UpdateItem() {}

        public String getContent() {
            return getString(ParseName.UPDATE_SUBTITLE);
        }

        public Date getTimestamp() {
            return getCreatedAt();
        }

        public String getTitle() {
            return getString(ParseName.UPDATE_TITLE);
        }
    }
}
