package com.hackfsu.hackfsu;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.pascalwelsch.holocircularprogressbar.HoloCircularProgressBar;

import org.json.JSONArray;

import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.PushService;

/**
 * Countdown Fragment.
 *
 * ToDo:
 * Add timer
 * Add Logo in middle.
 * Fix timing - make it so that it counts down 30hrs.
 * Improve look.
 *
 * @author Iosif
 */
public class CountdownFragment extends Fragment {
    public CountdownFragment() {
        // Required empty public constructor
    }

    private static final String TAG_TITLE = "title";
    private static final String TAG_START = "startTime";
    //private static final String TAG_END = "endTime";

    String title = "title";
    String time = "00:00";
    int start = 0;
    int end = 0;
    int timeLeft = 0;

    JSONArray countdowns = null;
    ArrayList<HashMap<String,String>> countdownsList;
    ListView lv;
    View rootView;
    private OnFragmentInteractionListener mListener;

    //CircleThangs
    private HoloCircularProgressBar mHoloCircularProgressBar;
    private ObjectAnimator mProgressBarAnimator;
    private TextView countTv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_countdown, container, false);
        mHoloCircularProgressBar = (HoloCircularProgressBar) rootView.findViewById(R.id.countdown);
        countTv = (TextView) rootView.findViewById(R.id.countdownTV);

        if(mProgressBarAnimator != null)
            mProgressBarAnimator.cancel();

        countdownsList = new ArrayList<HashMap<String, String>>();
        new ParseUpdates().execute();


        //Display Countdown Until Hacking Ends

        //Calculate how many seconds until Hackathon ends.
        //Set it to progress.
        animate(mHoloCircularProgressBar, null, 1f, 5000);
        mHoloCircularProgressBar.setMarkerProgress(1f);
        //countTv.setText("HackFSU in " + day + " days!");

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        PushService.subscribe(getActivity(), "Countdowns", MainSplashActivity.class);
        lv = (ListView) getActivity().findViewById(R.id.countdown_list);
        countTv = (TextView) getActivity().findViewById(R.id.countTv);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    /**
     * Animate.
     *
     * @param progressBar the progress bar
     * @param listener the listener
     */
    private void animate(final HoloCircularProgressBar progressBar,
                         final Animator.AnimatorListener listener) {

        final float progress = (float) (Math.random() * 2);
        int duration = 3000;
        animate(progressBar, listener, progress, duration);
    }

    private void animate(final HoloCircularProgressBar progressBar, final Animator.AnimatorListener listener,
                         final float progress, final int duration) {
        mProgressBarAnimator = ObjectAnimator.ofFloat(progressBar, "progress", progress);
        mProgressBarAnimator.setDuration(duration);
        mProgressBarAnimator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationCancel(final Animator animation) {
            }
            @Override
            public void onAnimationEnd(final Animator animation) {
                progressBar.setProgress(progress);
            }
            @Override
            public void onAnimationRepeat(final Animator animation) {
            }
            @Override
            public void onAnimationStart(final Animator animation) {
            }
        });
        if (listener != null) {
            mProgressBarAnimator.addListener(listener);
        }
        mProgressBarAnimator.reverse();
        mProgressBarAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(final ValueAnimator animation) {
                progressBar.setProgress((Float) animation.getAnimatedValue());
            }
        });
        progressBar.setMarkerProgress(progress);
        mProgressBarAnimator.start();
    }

    /*
    *
    * Parse Things
    *
    * */
    private class ParseUpdates extends AsyncTask<String,String, List<ParseObject>> {
        private ProgressDialog loadDialog;

        protected void onPreExecute() {
            super.onPreExecute();
            loadDialog = new ProgressDialog(getActivity());
            loadDialog.setMessage("Getting Count .. probably.");
            loadDialog.setIndeterminate(false);
            loadDialog.setCancelable(true);
            loadDialog.show();
        }

        @Override
        protected List<ParseObject> doInBackground(String... args) {
            List<ParseObject> countdwnsList = null;

            ParseQuery<ParseObject> query = ParseQuery.getQuery("Countdowns");
            query.orderByDescending("createdAt");

            try {
                countdwnsList = query.find();
                return countdwnsList;
            } catch (ParseException e) {
                e.printStackTrace();
                return countdwnsList;
            }
        }

        @Override
        protected void onPostExecute(List<ParseObject> countdwnsItems) {
            super.onPostExecute(countdwnsItems);
            loadDialog.dismiss();

            if (countdwnsItems != null) {
                // looping through all updates
                for (int i = 0; i < countdwnsItems.size(); i++) {

                    // tmp hashmap for single update
                    HashMap<String, String> count = new HashMap<String, String>();

                    // adding each child node to HashMap key => value
                    count.put(TAG_TITLE, countdwnsItems.get(i).getString("title"));

                    Format formatter = new SimpleDateFormat("h:mma", Locale.US);
                    String s = formatter.format(countdwnsItems.get(i).getDate(
                            "startTime"));
                    count.put(TAG_START, s);
                    //count.put(TAG_END, countdwns.get(i).getString("endTime"));

                    //GET Start, End time.
                    //Calculate Time Left
                    title = countdwnsItems.get( i ).getString("title");
                    time = formatter.format(countdwnsItems.get( 0 ).getDate(
                            "startTime"));

                    formatter = new SimpleDateFormat("ss", Locale.US);
                    s= formatter.format(countdwnsItems.get(0).getDate("startTime"));
                    start = Integer.parseInt(s);

                    s = formatter.format(countdwnsItems.get(0).getDate("endTime"));
                    end = Integer.parseInt(s);

                    timeLeft = end - start;
                    if(timeLeft < 0)
                        timeLeft = 0;

                    time = Integer.toString(timeLeft);

                    // adding to list
                    countdownsList.add(count);
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }


            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(CountdownFragment.this.getActivity(),
                    countdownsList,R.layout.countdown_item,
                    new String[] { TAG_TITLE, TAG_START },
                    new int[] { R.id.countdownTV, R.id.startTimeTV });

            lv.setAdapter(adapter);

            countTv.setText(time);
        }
    }
}
