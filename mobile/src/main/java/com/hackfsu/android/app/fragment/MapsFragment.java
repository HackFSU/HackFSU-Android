package com.hackfsu.android.app.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hackfsu.android.api.API;
import com.hackfsu.android.api.model.MapModel;
import com.hackfsu.android.app.R;
//import com.parse.FindCallback;
//import com.parse.GetDataCallback;
//import com.parse.ParseClassName;
//import com.parse.ParseException;
//import com.parse.ParseFile;
//import com.parse.ParseImageView;
//import com.parse.ParseObject;
//import com.parse.ParseQuery;
import com.hackfsu.android.app.activity.MapViewActivity;
import com.squareup.picasso.Picasso;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;


public class MapsFragment extends BaseFragment {

    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    MapItemRecyclerAdapter mAdapter;
    SwipeRefreshLayout mSwipeLayout;

    BaseFragment.OnFragmentInteractionListener mListener;
    API mAPI;


    public static MapsFragment newInstance() {
        return new MapsFragment();
    }

    // Required empty public constructor
    public MapsFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_map, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        mSwipeLayout = (SwipeRefreshLayout) v.findViewById(R.id.refresh_layout);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(false);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext()).build());

        // specify an adapter (see also next example)
        mAdapter = new MapItemRecyclerAdapter(new ArrayList<MapModel>());
        mRecyclerView.setAdapter(mAdapter);

        mAPI = new API(getActivity());
        loadMaps();

        mSwipeLayout.setColorSchemeResources(R.color.accent);
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                loadMaps();
                mSwipeLayout.setRefreshing(false);

            }
        });
    }

    private void loadMaps() {
        mAPI.getMaps(new API.APICallback<MapModel>() {
            @Override
            public void onDataReady(List<MapModel> dataSet) {
                mAdapter.replaceDataset(dataSet);
                mAdapter.notifyDataSetChanged();
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

        private List<MapModel> mDataset;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public class ViewHolder extends RecyclerView.ViewHolder {
            public View tile;
            public ImageView mMapItemImage;
            public ViewHolder(View v) {
                super(v);
                tile = v;
                mMapItemImage = (ImageView) v.findViewById(R.id.iv_map_image);
            }
        }

        public MapItemRecyclerAdapter(ArrayList<MapModel> myDataset) {
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
        public void onBindViewHolder(final ViewHolder holder, int position) {

            Picasso.with(getContext())
                    .load(mDataset.get(position).getURL())
                    .into(holder.mMapItemImage);

            holder.mMapItemImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bitmap bitmap = ((BitmapDrawable) holder.mMapItemImage.getDrawable()).getBitmap();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] imageInByte = baos.toByteArray();

                    Intent intent = new Intent(getContext(), MapViewActivity.class);
                    intent.putExtra("map", imageInByte);
                    getContext().startActivity(intent);

                }
            });

        }

        @Override
        public int getItemCount() {
            return mDataset.size();
        }

        // Custom method for hot-swapping data
        public void replaceDataset(List<MapModel> data) {
            mDataset = data;
        }
    }

}
