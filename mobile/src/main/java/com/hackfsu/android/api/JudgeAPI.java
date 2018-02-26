package com.hackfsu.android.api;

import android.app.Activity;

import java.util.ArrayList;

/**
 * Created by andrew on 2/26/18.
 */

public class JudgeAPI extends API {

    public JudgeAPI(Activity mActivity) {
        super(mActivity);
    }


    public interface onAssignmentRetrievedListener {
        void onAssignment(ArrayList<Integer> tableNumbers);
        void onFailure();
    }

    public void getHackAssignment(onAssignmentRetrievedListener listener) {

        // TODO get hacks
        listener.onAssignment(new ArrayList<Integer>());

    }


}
