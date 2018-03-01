package com.hackfsu.android.app.fragment.judging;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hackfsu.android.app.R;

import java.util.Locale;


public class JudgingHackFragment extends JudgingBaseFragment {

    private static final String HACK_NUMBER = "hackNumber";

    private int mTableNumber;
    private TextView mTableNumberView;
    private Button mAddSuperlativeButton;
    private Button mNextPageButton;

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
            mTableNumber = args.getInt(HACK_NUMBER);
        }

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_judging_individual, container, false);
        mTableNumberView = (TextView) v.findViewById(R.id.tv_judge_individual_table_no);
        mNextPageButton = (Button) v.findViewById(R.id.btn_judge_next);
        mAddSuperlativeButton = (Button) v.findViewById(R.id.btn_judge_add_superlative);
        return v;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mTableNumberView.setText(String.format(Locale.US, "%d", mTableNumber));
        mNextPageButton.setOnClickListener(new NextPageButtonListener());
        mAddSuperlativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.showHackSuperlativeDialog(mTableNumber);
            }
        });
    }

}