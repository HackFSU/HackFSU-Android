package com.hackfsu.hackfsu;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pascalwelsch.holocircularprogressbar.HoloCircularProgressBar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.PushService;

public class CountdownFragment extends Fragment {
    public CountdownFragment() {
        // Required empty public constructor
    }

    private static final String TAG_TITLE = "title";
    private static final String TAG_START = "startTime";
    private static final String TAG_END = "endTime";
    private static final String timezone = "America/New_York";

    Calendar startDate = Calendar.getInstance();
    Calendar current = Calendar.getInstance();
    Calendar endDate = Calendar.getInstance();
    CountDownTimer countDownTimer;

    ArrayList<HashMap<String,String>> countdownsList;

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

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        PushService.subscribe(getActivity(), "Countdowns", MainSplashActivity.class);
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
        public void onFragmentInteraction(Uri uri);
    }

    private void animate(final HoloCircularProgressBar progressBar,
                         final Animator.AnimatorListener listener) {

        final float progress = (float) (Math.random() * 2);
        int duration = 60000;
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

        protected void onPreExecute() {
            super.onPreExecute();
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

            if (countdwnsItems != null) {
                Log.i("onPostExecute", "CountdwnsItems is not NULL");
                //uses the first/oldest item in the parsed list
                //final int item = 0;
                // uses the last/newest item in the parse list
                final int item = countdwnsItems.size() - 1;
                TextView textView = (TextView)rootView.findViewById(R.id.countItem);
                Boolean started = true;

                //Getting current, start, and end dates as well as the
                //difference between current and end dates
                startDate.setTime(countdwnsItems.get(item).getDate(TAG_START));
                current = Calendar.getInstance();
                endDate.setTime(countdwnsItems.get(item).getDate(TAG_END));

                //Locking the Time Zone
                startDate.setTimeZone(TimeZone.getTimeZone(timezone));
                current.setTimeZone(TimeZone.getTimeZone(timezone));
                endDate.setTimeZone(TimeZone.getTimeZone(timezone));

                //getting what will be under the timer
                String start_AMPM = "PM";
                String end_AMPM = "PM";
                if (startDate.get(Calendar.AM_PM) == 0){
                    start_AMPM = "AM";
                }
                if (endDate.get(Calendar.AM_PM) == 0){
                    end_AMPM = "AM";
                }
                if (startDate.after(current)){
                    started = false;
                }
                String text = countdwnsItems.get(item).getString(TAG_TITLE) +
                        String.format("\n%d/%d", startDate.get(Calendar.MONTH),
                                startDate.get(Calendar.DAY_OF_MONTH)) +
                        String.format(" %d:%02d", startDate.get(Calendar.HOUR),
                                startDate.get(Calendar.MINUTE)) + start_AMPM + " - " +
                        String.format("%d:%02d", endDate.get(Calendar.HOUR),
                                endDate.get(Calendar.MINUTE)) + end_AMPM;
                /*String text = countdwnsItems.get(item).getString(TAG_TITLE) + "\n" +
                        startDate.get(Calendar.HOUR) + ":" +
                        startDate.get(Calendar.MINUTE) + " " + start_AMPM + " - " +
                        endDate.get(Calendar.HOUR) + ":" +
                        endDate.get(Calendar.MINUTE) + ":" + end_AMPM;*/

                //setting the text
                textView.setText(text);
                Log.d("CountDown", "Event set to: " + textView.getText().toString());

                //countDownTimer updates the time value according to the
                // current system time and end value. It displays the time
                // left in the format: dd:hh:mm. Any values which become 0's
                // are removed along with the ":" after them except for hh:mm.
                //      d = Day, h = Hour, m = Minute
                countDownTimer = new CountDownTimer(endDate.getTimeInMillis() - Calendar.getInstance().getTimeInMillis(), 60000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        //lengths of time in milliseconds
                        long remainingTime = millisUntilFinished;
                        int day = 86400000;
                        int hour = 3600000;
                        int minute = 60000;
                        //int second = 1000;
                        String text = "";

                        if (remainingTime >= day) {
                            text = (remainingTime/day) + ":";
                            remainingTime = remainingTime%day;
                            countTv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 66);
                        }else{
                            countTv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 88);
                        }
                        //***NOTE: To include seconds
                        // Uncomment all line below in this method
                        //  and change the count down interval above (about 20 lines up)
                        //  from 60000 to 1000
                        //if (remainingTime >= hour) {
                        text += String.format("%02d:",(remainingTime/hour));
                        remainingTime = remainingTime%hour;
                        //}
                        text += String.format("%02d", (remainingTime / minute));
                        //remainingTime = remainingTime % minute;
                        //if (remainingTime < hour) {
                        //    text += String.format(":%02d",(remainingTime/second));
                        //}

                        countTv.setTextColor(Color.BLACK);
                        countTv.setText(text);
                        Log.i("CountDownTimer","Finished onTick and set text = " + text);
                    }

                    @Override
                    public void onFinish() {
                        Log.i("CountDownTimer", "Started onFinish");
                        countTv.setText("Done");
                        countTv.setTextColor(Color.RED);
                    }
                };

                // counter is started
                if(started) {
                    countDownTimer.start();
                }else{
                    countTv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 60);
                    countTv.setText("HackFSU");
                    Log.i("CountDownTimer", "Event hasn't started, countDownTimer set to 'HackFsu'");
                }

                //Setting up the Progress Circle
                float progress = 0f;
                int duration = (int) (endDate.getTimeInMillis() - startDate.getTimeInMillis());
                if(started) {
                    if (duration > 0) {
                        long timeDiff = endDate.getTimeInMillis() - current.getTimeInMillis();
                        progress = timeDiff / duration;
                    } else {
                        duration = 0;
                    }
                }else{
                    duration = 0;
                }

                animate(mHoloCircularProgressBar, null, progress, duration);
                mHoloCircularProgressBar.setMarkerProgress(progress);
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
        }
    }
}
