package com.hackfsu.android.app.fragment.judging;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hackfsu.android.app.R;

import java.util.ArrayList;


public class JudgingHackFragment extends JudgingBaseFragment {

    private static final String HACK_NUMBER = "hackNumber";

    private int mHackNumber;


    public static JudgingHackFragment newInstance(int hackNumber) {

        Bundle bundle = new Bundle();
        bundle.putInt(HACK_NUMBER, hackNumber);

        JudgingHackFragment newFragment = new JudgingHackFragment();
        newFragment.setArguments(bundle);

        return newFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            mHackNumber = args.getInt(HACK_NUMBER);
        }

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_judging_individual, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }
}
