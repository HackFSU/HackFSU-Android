package com.hackfsu.mobile.android.app.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hackfsu.mobile.android.api.API;
import com.hackfsu.mobile.android.api.model.SponsorModel;
import com.hackfsu.mobile.android.app.R;
//import com.parse.FindCallback;
//import com.parse.GetDataCallback;
//import com.parse.ParseClassName;
//import com.parse.ParseException;
//import com.parse.ParseFile;
//import com.parse.ParseImageView;
//import com.parse.ParseObject;
//import com.parse.ParseQuery;
import com.squareup.picasso.Picasso;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;


public class SponsorsFragment extends BaseFragment {

    Toolbar mToolbar;
    AppBarLayout mAppBar;
    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    SponsorRecyclerAdapter mAdapter;
    SwipeRefreshLayout mSwipeLayout;
    API mAPI;

    BaseFragment.OnFragmentInteractionListener mListener;


    // TODO: Rename and change types and number of parameters
    public static SponsorsFragment newInstance() {
        return new SponsorsFragment();
    }

    // Required empty public constructor
    public SponsorsFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_sponsors, container, false);
        mToolbar = (Toolbar) v.findViewById(R.id.toolbar);
        mAppBar = (AppBarLayout) v.findViewById(R.id.app_bar);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        mSwipeLayout = (SwipeRefreshLayout) v.findViewById(R.id.refresh_layout);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Register toolbar
        mToolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        mToolbar.setTitle("Sponsors");
        //mCollapsing.setTitle("Sponsors");
        mListener.registerToolbar(mToolbar);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext()).build());

        // specify an adapter (see also next example)
        mAdapter = new SponsorRecyclerAdapter(new ArrayList<SponsorModel>());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemViewCacheSize(13);

        mAPI = new API(getActivity());
        loadSponsors();


        // Swipe Reload
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadSponsors();
                mSwipeLayout.setRefreshing(false);

            }
        });
        mSwipeLayout.setColorSchemeResources(R.color.accent);
    }

    public void loadSponsors() {
        mAPI.getSponsors(new API.APICallback<SponsorModel>() {
            @Override
            public void onDataReady(List<SponsorModel> dataSet) {
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
    private class SponsorRecyclerAdapter extends
            RecyclerView.Adapter<SponsorRecyclerAdapter.ViewHolder> {

        private List<SponsorModel> mDataset;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public class ViewHolder extends RecyclerView.ViewHolder {
            public LinearLayout tile;
            public TextView mSponsorName;
            public ImageView mSponsorImage;
            public TextView mSponsorLevel;
            public View mLevelLayout;
            public ViewHolder(View v) {
                super(v);
                tile = (LinearLayout) v;
                mSponsorName = (TextView) v.findViewById(R.id.tv_sponsor_name);
                mSponsorImage = (ImageView) v.findViewById(R.id.iv_sponsor_image);
                mSponsorLevel = (TextView) v.findViewById(R.id.tv_sponsor_level);
                mLevelLayout = v.findViewById(R.id.level_layout);
            }
        }

        public SponsorRecyclerAdapter(ArrayList<SponsorModel> myDataset) {
            mDataset = myDataset;
        }

        // Instantiate each viewholder
        @Override
        public SponsorRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                    int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.tile_sponsor, parent, false);

            return new ViewHolder(v);
        }

        // Populate the viewholder with data
        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

            holder.mSponsorName.setText(mDataset.get(position).getName());
            Picasso.with(getContext())
                    .load(mDataset.get(position).getImageURL())
                    .into(holder.mSponsorImage);

            holder.mSponsorImage.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse(
                            mDataset.get(holder.getAdapterPosition()).getWebURL()
                    ));
                    startActivity(intent);
                }
            });


//            // Retrieve Data
//            final String name = mDataset.get(position).getName();
//            final ParseFile image = mDataset.get(position).getImage();
//            final int level = mDataset.get(position).getLevel();
//
//            // Display data
//            holder.mSponsorName.setText(name);
//            holder.mSponsorImage.setParseFile(image);
//            holder.mSponsorImage.loadInBackground(new GetDataCallback() {
//                @Override
//                public void done(byte[] data, ParseException e) {
//                    if(e != null) {
//                        Log.e("HackFSU Sponsors", "Someone fucked up uploading the image for " + name);
//                    } else {
//                        /*int height = holder.mSponsorImage.getHeight();
//                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.mSponsorImage.getLayoutParams();
//                        params.height = height;
//                        holder.mSponsorImage.setLayoutParams(params);
//                        */
//                    }
//                }
//            });
//
//            // Level
//
//            Typeface face;
//            face = Typeface.createFromAsset(getContext().getAssets(), getResources().getString(R.string.hackfsu_font));
//
//            String tier = "Tier " + String.valueOf(level);
//            holder.mSponsorLevel.setText(tier);
//            holder.mSponsorLevel.setTypeface(face);
//            Resources r = SponsorsFragment.this.getContext().getResources();
//            switch (level) {
//                case 2:
//                    holder.mLevelLayout.setBackgroundResource(R.drawable.bg_sponsor_badge_2);
//                    break;
//                case 3:
//                    holder.mLevelLayout.setBackgroundResource(R.drawable.bg_sponsor_badge_3);
//
//                    break;
//                case 4:
//                    holder.mLevelLayout.setBackgroundResource(R.drawable.bg_sponsor_badge_4);
//                    break;
//
//            }

        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
            super.onBindViewHolder(holder, position, payloads);
        }

        @Override
        public int getItemCount() {
            return mDataset.size();
        }

        // Custom method for hot-swapping data
        public void replaceDataset(List<SponsorModel> data) {
            mDataset = data;
        }
    }

//    @ParseClassName(ParseName.SPONSOR)
//    public static class SponsorModel extends ParseObject {
//
//        public SponsorModel(){}
//
//        public String getName() {
//            return getString(ParseName.SPONSOR_NAME);
//        }
//
//        public ParseFile getImage() {
//            return getParseFile(ParseName.SPONSOR_IMAGE);
//        }
//
//        public int getLevel() {
//            return getInt(ParseName.SPONSOR_LEVEL);
//        }
//
//
//    }



}
