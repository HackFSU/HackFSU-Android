package com.hackfsu.android.app.activity;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.hackfsu.android.api.JudgeAPI;
import com.hackfsu.android.app.R;
import com.hackfsu.android.app.fragment.judging.JudgingRankingFragment;
import com.hackfsu.android.app.fragment.judging.JudgingBaseFragment;
import com.hackfsu.android.app.fragment.judging.JudgingHackFragment;
import com.hackfsu.android.app.fragment.judging.JudgingIntroFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class JudgingActivity extends AppCompatActivity
        implements JudgingBaseFragment.OnJudgeFragmentInteractionListener {

    Toolbar mToolbar;

    JudgingIntroFragment mIntroFragment;
    ArrayList<JudgingHackFragment> mHackFragments = new ArrayList<>();
    JudgingRankingFragment mRankFragment;
    JudgeActivityFragmentAssistant assistant = new JudgeActivityFragmentAssistant(this);

    ArrayList<Integer> mTableNumbers;
    ArrayList<String> mAvailableSuperlatives;

    JudgeAPI mJudgeAPI;

    @SuppressLint("UseSparseArrays")
    HashMap<Integer, ArrayList<String>> mNominations = new HashMap<>();
    SparseArray<Integer[]> mCheckboxSelections = new SparseArray<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_judging);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_judging);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mJudgeAPI = new JudgeAPI(this);
        initAssignment();

    }

    private void initAssignment() {
        mJudgeAPI.getHackAssignment(new JudgeAPI.OnAssignmentRetrievedListener() {

            @Override
            public void onAssignment(ArrayList<Integer> tableNumbers, String expoString,
                                     ArrayList<String> superlatives) {
                mToolbar.setTitle(expoString);

                mTableNumbers = tableNumbers;
                mAvailableSuperlatives = superlatives;

                assistant.init(tableNumbers, 2 + tableNumbers.size());
            }

            @Override
            public void onFailure() {
                Toast.makeText(
                        JudgingActivity.this,
                        R.string.judging_hack_retr_error,
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }


    @Override
    public void onBackPressed() {
        if (assistant.atFirstFragment()) { finish(); }
        else { assistant.showPrevFragment(); }
    }

    @Override
    public void showNextPage() {
        assistant.showNextFragment();
    }

    @Override
    public void showPreviousPage() {
        assistant.showPrevFragment();
    }


    @Override
    public void showHackSuperlativeDialog(final int tableNumber) {

        Integer[] prevSelection = mCheckboxSelections.get(tableNumber, new Integer[] {});

        new MaterialDialog.Builder(this)
                .title(R.string.judging_superlative_dialog_title)
                .items(mAvailableSuperlatives)
                .itemsCallbackMultiChoice(prevSelection,
                        new SuperlativeDialogSelectionListener(tableNumber))
                .positiveText(R.string.judging_superlative_dialog_choose)
                .show();

    }

    @Override
    public void submitHackScores(ArrayList<Integer> scoreOrder) {

        HashMap<String, Integer> order = new HashMap<>();
        for (int i = 0; i < scoreOrder.size(); ++i) {
            order.put(Integer.toString(i + 1), scoreOrder.get(i));
        }

        mJudgeAPI.submitJudgingAssignent(order, mNominations,
                new JudgeAPI.OnSubmitAssignmentListener() {
            @Override
            public void onSuccess() {
                new MaterialDialog.Builder(JudgingActivity.this)
                        .title("Hacks successfully submitted")
                        .content("You can now begin a new assignment")
                        .positiveText("Let's go")
                        .show();
                initAssignment();
            }

            @Override
            public void onFailure() {
                Toast.makeText(JudgingActivity.this,
                        "Whoops, issue submitting scores", Toast.LENGTH_LONG).show();
            }
        });
    }

    class SuperlativeDialogSelectionListener implements MaterialDialog.ListCallbackMultiChoice {

        private int mTableNumber;

        SuperlativeDialogSelectionListener(int tableNumber) {
            this.mTableNumber = tableNumber;
        }

        @Override
        public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {

            if (!mNominations.containsKey(mTableNumber)) {
                mNominations.put(mTableNumber, new ArrayList<String>());
            }

            // Store explicit checkbox selection for redos
            mCheckboxSelections.put(mTableNumber, which);

            // Save nominations
            ArrayList<String> nominations = new ArrayList<>();
            for (int index : which) {
                String superlativeName = mAvailableSuperlatives.get(index);
                nominations.add(superlativeName);
                Log.d(JudgingActivity.class.getName(),
                        String.format(Locale.US, "Nominated table %d for superlative %s",
                                mTableNumber, superlativeName));
            }
            mNominations.put(mTableNumber, nominations);

            return true;
        }

}



class JudgeActivityFragmentAssistant {

    /**
     * The point here is to abstract the details of fragment transactions away from the logic
     * of the activity itself. This is done to prevent developers from introducing bugs by
     * interacting with the logic of fragment transactions directly.
     */

    private JudgingActivity judgingActivity;

    private int fragmentIndex = 0;
    private int totalFragments = 0;

    JudgeActivityFragmentAssistant(JudgingActivity judgingActivity) {
        this.judgingActivity = judgingActivity;
    }

    void init(ArrayList<Integer> tableNumbers, int totalFragments) {

        Log.d(JudgeActivityFragmentAssistant.class.getName(),
                String.format(Locale.US, "Initializing with %d fragments", totalFragments));

        mIntroFragment = JudgingIntroFragment.newInstance(tableNumbers);
        mRankFragment = JudgingRankingFragment.newInstance(tableNumbers);

        mHackFragments.clear();
        for (Integer i : tableNumbers) {
            mHackFragments.add(JudgingHackFragment.newInstance(i));
        }

        this.totalFragments = totalFragments;
        this.fragmentIndex = 0;

        showFragment(0, false);
    }

    boolean atFirstFragment() {
        return fragmentIndex == 0;
    }

    boolean atLastFragment() {
        return fragmentIndex == (totalFragments - 1);
    }

    void showPrevFragment() {
        if (!atFirstFragment()) {
            fragmentIndex = fragmentIndex - 1;
            showFragment(fragmentIndex, false);
        }
    }

    void showNextFragment() {
        if (!atLastFragment()) {
            fragmentIndex = fragmentIndex + 1;
            showFragment(fragmentIndex, true);
        }
    }

    private void showFragment(int index, boolean showAnimation) {
        Log.d(JudgeActivityFragmentAssistant.class.getName(),
                String.format(Locale.US, "Showing fragment at index %d", index));

        if (index < 0 || index >= totalFragments) {
            Toast.makeText(judgingActivity, "Index error", Toast.LENGTH_SHORT).show();
            judgingActivity.finish();
            return;
        }

        Fragment frag;
        FragmentManager fm = judgingActivity.getSupportFragmentManager();

        if (index == 0)
            frag = judgingActivity.mIntroFragment;
        else if (index == judgingActivity.mHackFragments.size() + 1)
            frag = judgingActivity.mRankFragment;
        else
            frag = judgingActivity.mHackFragments.get(index - 1);

        if (frag != null) {
            FragmentTransaction ft = fm.beginTransaction();

            if (showAnimation) {
                ft.setCustomAnimations(R.animator.slide_in_from_left,
                        R.animator.slide_out_to_right, 0, 0);
            }

            ft.replace(R.id.fragment_anchor_judging, frag).commit();
        }

    }
}


}
