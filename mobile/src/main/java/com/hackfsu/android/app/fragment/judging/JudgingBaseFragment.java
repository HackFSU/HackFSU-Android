package com.hackfsu.android.app.fragment.judging;

import android.content.Context;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.view.View;

import java.util.ArrayList;


abstract public class JudgingBaseFragment extends Fragment {

    protected OnJudgeFragmentInteractionListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnJudgeFragmentInteractionListener) {
            mListener = (OnJudgeFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnJudgeFragmentInteractionListener");
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnJudgeFragmentInteractionListener {
        void showNextPage();
        void showPreviousPage();
        void addHackSuperlative(int tableNumber, String superlative);
        void submitHackScores(ArrayList<Integer> scoreOrder);
    }


    public class NextPageButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            mListener.showNextPage();
        }
    }
}
