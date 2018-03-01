package com.hackfsu.android.app.fragment.judging;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.hackfsu.android.app.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

/**
 * Created by andrew on 2/28/18.
 */

public class JudgingRankingFragment extends JudgingBaseFragment {


    private static final String TABLE_NUMBERS = "tablekNumbers";
    private ArrayList<Integer> mTableNumbers;
    private HackSelector mHackSelector;

    TextView[] mTableDisplays;
    Button mSubmitButton;


    public static JudgingRankingFragment newInstance(ArrayList<Integer> tableNumbers) {

        Bundle bundle = new Bundle();
        bundle.putIntegerArrayList(TABLE_NUMBERS, tableNumbers);

        JudgingRankingFragment newFragment = new JudgingRankingFragment();
        newFragment.setArguments(bundle);

        return newFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            mTableNumbers = args.getIntegerArrayList(TABLE_NUMBERS);
            mHackSelector = new HackSelector(mTableNumbers);
        }

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_judging_ranking, container, false);

        mSubmitButton = (Button) v.findViewById(R.id.btn_judge_submit);

        mTableDisplays = new TextView[] {
                v.findViewById(R.id.tv_judge_hack_1),
                v.findViewById(R.id.tv_judge_hack_2),
                v.findViewById(R.id.tv_judge_hack_3),
        };

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        String[] dialogTitles = getResources().getStringArray(R.array.judging_rank_choice);

        final ArrayList<String> transformedTableNumbers = new ArrayList<>();
        for (int table : mTableNumbers) {
            transformedTableNumbers.add(String.format(Locale.US, "Table #%d", table));
        }

        // Add listeners to tables
        resetDisplays();
        for (int i = 0; i < mTableDisplays.length; ++i) {
            if (i < mTableNumbers.size()) {

                TextView tableDisplay = mTableDisplays[i];
                final String title = dialogTitles[i];
                final int slotNo = i;

                tableDisplay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mHackSelector.showDialogForSlot(title, transformedTableNumbers, slotNo);
                    }
                });



            } else {
                mTableDisplays[i].setVisibility(View.GONE);
            }
        }

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!mHackSelector.selections.contains(-1))
                    mListener.submitHackScores(mHackSelector.selections);
                else {
                    new MaterialDialog.Builder((Context) mListener)
                            .content("Please rank all of your assigned hacks")
                            .positiveText("Okay")
                            .show();
                }
            }
        });



    }

    private void resetDisplays () {
        for (int i = 0; i < mTableDisplays.length; ++i) {
            if (i < mTableDisplays.length) {
                mTableDisplays[i].setText(R.string.judge_hack_empty);
            } else {
                mTableDisplays[i].setVisibility(View.GONE);
            }
        }
    }


    private class HackSelector {

        ArrayList<Integer> choices;
        ArrayList<Integer> selections;

        HackSelector(ArrayList<Integer> choices) {
            this.choices = choices;
            resetSelections();

        }

        int getSelectionAt(int slot) {
            if (slot >= selections.size()) return -1;
            else return selections.get(slot);
        }

        void resetSelections () {
            selections = new ArrayList<>(Arrays.asList(new Integer[] {-1, -1, -1}));
        }


        void showDialogForSlot(final String title, final ArrayList<String> items, final int slot) {

            new MaterialDialog.Builder((Context) mListener)
                    .title(title)
                    .items(items)
                    .itemsCallbackSingleChoice(getSelectionAt(slot),
                            new MaterialDialog.ListCallbackSingleChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog dialog, View itemView,
                                                   int which, CharSequence text) {

                            if (getSelectionAt(slot) != -1 ||
                                    selections.contains(choices.get(which))) {
                                resetDisplays();
                                resetSelections();
                            }

                            selections.set(slot, choices.get(which));
                            mTableDisplays[slot].setText(items.get(which));

                            return true;
                        }
                    })
                    .positiveText(R.string.judging_hack_select)
                    .show();
        }


    }
}
