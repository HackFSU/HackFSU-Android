package com.hackfsu.android.app.fragment.judging;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hackfsu.android.app.R;

import java.util.ArrayList;


public class JudgingIntroFragment extends JudgingBaseFragment {

    private static final String HACK_NUMBERS = "hackNumbers";

    private ArrayList<Integer> mHackNumbers;

    LinearLayout mIntroTablesLayout;
    TextView mIntroLabel;
    TextView[] mAssignmentDisplays;
    Button mNextViewButton;


    public static JudgingIntroFragment newInstance(ArrayList<Integer> hackNumbers) {

        Bundle bundle = new Bundle();
        bundle.putIntegerArrayList(HACK_NUMBERS, hackNumbers);

        JudgingIntroFragment newFragment = new JudgingIntroFragment();
        newFragment.setArguments(bundle);

        return newFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            mHackNumbers = args.getIntegerArrayList(HACK_NUMBERS);
        }

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_judging_intro, container, false);

        mIntroLabel = (TextView) v.findViewById(R.id.tv_judging_intro_label);
        mIntroTablesLayout = (LinearLayout) v.findViewById(R.id.layout_judge_intro_tables);
        mNextViewButton = (Button) v.findViewById(R.id.btn_judge_next);

        mAssignmentDisplays = new TextView[] {
                v.findViewById(R.id.tv_judge_intro_hack_1),
                v.findViewById(R.id.tv_judge_intro_hack_2),
                v.findViewById(R.id.tv_judge_intro_hack_3),
        };

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        if (mHackNumbers.isEmpty()) {
            // Hide display components
            mIntroTablesLayout.setVisibility(View.GONE);

            mIntroLabel.setText(R.string.judging_hack_retr_error);

        }

        else {
            for (int i = 0; i < mAssignmentDisplays.length; ++i) {
                if (i < mHackNumbers.size()) {
                    String s = "Table " + mHackNumbers.get(i);
                    mAssignmentDisplays[i].setText(s);
                } else {
                    mAssignmentDisplays[i].setVisibility(View.GONE);
                }
            }

        }

        mNextViewButton.setOnClickListener(new NextPageButtonListener());


    }
}
