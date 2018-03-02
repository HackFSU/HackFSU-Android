package com.hackfsu.android.app.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArraySet;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hackfsu.android.api.API;
import com.hackfsu.android.api.EventAPI;
import com.hackfsu.android.api.model.EventModel;
import com.hackfsu.android.api.util.ISO8601Updates;
import com.hackfsu.android.app.R;
import com.hackfsu.android.app.activity.JudgingActivity;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment .OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends BaseFragment {

    API mAPI;

    BaseFragment.OnFragmentInteractionListener mListener;

    private TextView mNameText;
    private TextView mGroupsText;
    private Button mJudgingButton;
    private ImageView mQRView;
    private RecyclerView mEventsRecycler;
    private EventRecyclerAdapter mEventRecyclerAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance () { return new ProfileFragment();}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        mNameText = (TextView) v.findViewById(R.id.tv_profile_name);
        mGroupsText = (TextView) v.findViewById(R.id.tv_profile_groups);
        mJudgingButton = (Button) v.findViewById(R.id.btn_profile_judging);
        mQRView = (ImageView) v.findViewById(R.id.iv_profile_qr);
        mEventsRecycler = (RecyclerView) v.findViewById(R.id.recycler_profile_events);
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_profile);

        return v;


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);




        mEventsRecycler.setLayoutManager(new GridLayoutManager(getContext(), 2,
                LinearLayoutManager.VERTICAL, false));

        mEventRecyclerAdapter = new EventRecyclerAdapter();
        mEventsRecycler.setAdapter(mEventRecyclerAdapter);

        mSwipeRefreshLayout.setRefreshing(true);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData(new RefreshCompleteListener(mSwipeRefreshLayout));
            }
        });

        loadData(new RefreshCompleteListener(mSwipeRefreshLayout));

    }



    private void loadData(final RefreshCompleteListener callback) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        String fullName = String.format(Locale.US, "%s %s",
                preferences.getString("first_name", null),
                preferences.getString("last_name", null));

        mNameText.setText(fullName);
        String hexcode = preferences.getString("hexcode", "");
        Set<String> groupSet = preferences.getStringSet("groups", new ArraySet<String>());
        if (groupSet.contains("attendee")) groupSet.remove("attendee");
        if (groupSet.contains("judge")) mJudgingButton.setVisibility(View.VISIBLE);

        ArrayList<String> subtitleItems = new ArrayList<>(groupSet);
        subtitleItems.add(String.format(Locale.US, "#%s", hexcode));
        String groupString = TextUtils.join("  |  ", subtitleItems);
        mGroupsText.setText(groupString);

        mJudgingButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(getContext(), JudgingActivity.class));
            }
        });


        String qrURL = preferences.getString("qr", null);

        Picasso.with(getContext())
                .load(qrURL)
                .into(mQRView);

        EventAPI eventAPI = new EventAPI(getActivity());
        eventAPI.getHackerEvents(new EventAPI.OnEventsReceivedListener() {
            @Override
            public void onEvents(ArrayList<EventModel> eventList) {

                if (eventList != null) {

//                    try {
//                    Toast.makeText(getContext(), ""+eventList.size(), Toast.LENGTH_LONG).show();
                    Log.d(ProfileFragment.class.getName(), "eventList size: " + eventList.size());
                    for (EventModel s: eventList) {
                        Log.d(ProfileFragment.class.getName(), s.name);
                    }
//                    }
//                    catch (Exception e) {
//                        Toast.makeText(getContext(), "logging isssue", Toast.LENGTH_SHORT).show();
//                    }

                    //

                    try {
                        Collections.reverse(eventList);
                    }
                    catch (Exception e) {
                        Toast.makeText(getContext(), "reveres issue", Toast.LENGTH_SHORT).show();
                    }

                    //

                    try {
                        mEventRecyclerAdapter.replaceDataset(eventList);
                        mEventRecyclerAdapter.notifyDataSetChanged();
                    }
                    catch (Exception e) {
                        Toast.makeText(getContext(), "adapter issue", Toast.LENGTH_SHORT).show();
                    }

                }

                try {
                    callback.onComplete();
                }
                catch (Exception e) {
                    Toast.makeText(getContext(), "callback issue: " + e.getClass().getName(), Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(String message) {
                Log.e(ProfileFragment.class.getName(), "Failed getting Profile Events");
                Toast.makeText(getContext(),
                        message, Toast.LENGTH_SHORT).show();
                callback.onComplete();
            }

        });

    }

    private class EventRecyclerAdapter
            extends RecyclerView.Adapter<EventRecyclerAdapter.ViewHolder> {

        private ArrayList<EventModel> mDataset = new ArrayList<>();

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView eventNameView;
            TextView timestampView;

            public ViewHolder(View itemView) {
                super(itemView);
                eventNameView = itemView.findViewById(R.id.tv_profile_event_name);
                timestampView = itemView.findViewById(R.id.tv_profile_event_time);
            }
        }

        public EventRecyclerAdapter() {}

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(
                    LayoutInflater.from(getContext())
                            .inflate(R.layout.tile_profile_event, parent, false)
            );
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            try {
                holder.eventNameView.setText(mDataset.get(position).name);
            }
            catch (Exception e) {
                Toast.makeText(getContext(), "onBind:"+e.getClass().getName(), Toast.LENGTH_SHORT).show();
            }

            try {
                SimpleDateFormat formatter = new SimpleDateFormat("EEE h:mm a", Locale.US);
                formatter.setTimeZone(TimeZone.getTimeZone("EST"));
                Calendar cal = ISO8601Updates.toCalendar(mDataset.get(position).time);
                String timeStamp = formatter.format(cal.getTime());
                holder.timestampView.setText(timeStamp);
            }
            catch (ParseException e) {
                holder.timestampView.setVisibility(View.GONE);
            }

        }

        @Override
        public int getItemCount() {
            return mDataset.size();
        }

        public void replaceDataset(ArrayList<EventModel> dataset) {
            mDataset = dataset;
        }
    }


}
