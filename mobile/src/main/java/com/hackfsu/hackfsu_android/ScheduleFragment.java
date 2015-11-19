package com.hackfsu.hackfsu_android;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;


public class ScheduleFragment extends BaseFragment {

    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;

    // TODO: Rename and change types and number of parameters
    public static ScheduleFragment newInstance() {
        return new ScheduleFragment();
    }

    // Required empty public constructor
    public ScheduleFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_list, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
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
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext()).build());

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
        public ScheduleRecyclerAdapter(String[] myDataset) {
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
            holder.mSubtitleText.setText(mDataset[position]);

        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataset.length;
        }
    }
}
