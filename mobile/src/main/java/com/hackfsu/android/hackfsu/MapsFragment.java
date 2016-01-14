package com.hackfsu.android.hackfsu;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hackfsu.android.hackfsu.R;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;


public class MapsFragment extends BaseFragment {

    Toolbar mToolbar;
    AppBarLayout mAppBar;
    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    MapItemRecyclerAdapter mAdapter;
    BaseFragment.OnFragmentInteractionListener mListener;


    public static MapsFragment newInstance() {
        return new MapsFragment();
    }

    // Required empty public constructor
    public MapsFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_sponsors, container, false);
        mToolbar = (Toolbar) v.findViewById(R.id.toolbar);
        mAppBar = (AppBarLayout) v.findViewById(R.id.app_bar);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Register toolbar
        mToolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        mToolbar.setTitle("Venue Map");
        mListener.registerToolbar(mToolbar);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(false);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext()).build());

        // specify an adapter (see also next example)
        mAdapter = new MapItemRecyclerAdapter(new ArrayList<MapItem>());
        mRecyclerView.setAdapter(mAdapter);

        ParseQuery<MapItem> query = ParseQuery.getQuery(ParseName.MAPITEM);
        query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
        query.orderByAscending(ParseName.MAP_FLOOR);
        query.findInBackground(new FindCallback<MapItem>() {
            @Override
            public void done(List<MapItem> list, ParseException e) {
                if(e != null) {
                    Log.e("HackFSU", "Error: " + e.getMessage());
                } else {
                    mAdapter.replaceDataset(list);
                    mAdapter.notifyItemRangeChanged(0, mAdapter.getItemCount());
                }
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

    // Adapter used by this fragment
    private class MapItemRecyclerAdapter extends
            RecyclerView.Adapter<MapItemRecyclerAdapter.ViewHolder> {

        private List<MapItem> mDataset;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public class ViewHolder extends RecyclerView.ViewHolder {
            public View tile;
            public ParseImageView mMapItemImage;
            public ViewHolder(View v) {
                super(v);
                tile = v;
                mMapItemImage = (ParseImageView) v.findViewById(R.id.iv_map_image);
            }
        }

        public MapItemRecyclerAdapter(ArrayList<MapItem> myDataset) {
            mDataset = myDataset;
        }

        // Instantiate each viewholder
        @Override
        public MapItemRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                    int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.tile_map, parent, false);

            return new ViewHolder(v);
        }

        // Populate the viewholder with data
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            holder.mMapItemImage.setParseFile(mDataset.get(position).getImage());
            holder.mMapItemImage.loadInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    if(e != null) {
                        Log.e("HackFSU", e.getMessage());
                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            return mDataset.size();
        }

        // Custom method for hot-swapping data
        public void replaceDataset(List<MapItem> data) {
            mDataset = data;
        }
    }

    @ParseClassName(ParseName.MAPITEM)
    public static class MapItem extends ParseObject {

        public MapItem(){}

        public ParseFile getImage() {
            return getParseFile(ParseName.MAP_IMAGE);
        }

        public int getFloor() {
            return getInt(ParseName.MAP_FLOOR);
        }


    }



}
