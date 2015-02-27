package com.hackfsu.hackfsu;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.pascalwelsch.holocircularprogressbar.HoloCircularProgressBar;

import java.util.Calendar;


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

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    View rootView;

    private OnFragmentInteractionListener mListener;

    //CircleThangs
    private HoloCircularProgressBar mHoloCircularProgressBar;
    private ObjectAnimator mProgressBarAnimator;
    private TextView countTv;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CountdownFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CountdownFragment newInstance(String param1, String param2) {
        CountdownFragment fragment = new CountdownFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public CountdownFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_countdown, container, false);
        mHoloCircularProgressBar = (HoloCircularProgressBar) rootView.findViewById(R.id.countdown);
        countTv = (TextView) rootView.findViewById(R.id.countdownTV);

        if(mProgressBarAnimator != null)
            mProgressBarAnimator.cancel();

        //Get Current Time
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR);
        int min = c.get(Calendar.MINUTE);

        //Get Current Time
        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();


        if( (month != 2) && (day < 20 ) )   //Display Countdown Until Hackathon
        {                               //March is 02

            // Calculate days until hackathon
            animate(mHoloCircularProgressBar, null, 1f, 5000);
            mHoloCircularProgressBar.setMarkerProgress(1f);

            countTv.setText("HackFSU starts in " + day + " days!");
        }
        else {             //Display Countdown Until Hacking Ends

            //Calculate how many seconds until Hackathon ends.
            //Set it to progress.
            animate(mHoloCircularProgressBar, null, 1f, 5000);
            mHoloCircularProgressBar.setMarkerProgress(1f);

            countTv.setText("HackFSU in " + day + " days!");
        }

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

}
