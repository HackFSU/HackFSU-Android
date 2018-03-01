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
import com.hackfsu.android.app.fragment.judging.JudgingBaseFragment;
import com.hackfsu.android.app.fragment.judging.JudgingHackFragment;
import com.hackfsu.android.app.fragment.judging.JudgingIntroFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class JudgingActivity extends AppCompatActivity
        implements JudgingBaseFragment.OnJudgeFragmentInteractionListener {

    JudgingIntroFragment mIntroFragment;
    ArrayList<JudgingHackFragment> mHackFragments = new ArrayList<>();
    JudgeActivityFragmentAssistant assistant = new JudgeActivityFragmentAssistant(this);

    ArrayList<Integer> mTableNumbers;
    ArrayList<String> mAvailableSuperlatives;

    @SuppressLint("UseSparseArrays")
    Map<Integer, ArrayList<String>> mNominations = new HashMap<>();
    SparseArray<Integer[]> mCheckboxSelections = new SparseArray<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_judging);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_judging);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        JudgeAPI api = new JudgeAPI(this);
        api.getHackAssignment(new JudgeAPI.OnAssignmentRetrievedListener() {

            @Override
            public void onAssignment(ArrayList<Integer> tableNumbers, String expoString,
                                     ArrayList<String> superlatives) {
                toolbar.setTitle(expoString);

                mTableNumbers = tableNumbers;
                mAvailableSuperlatives = superlatives;

                initNewAssignment(tableNumbers);
                assistant.init(2 + tableNumbers.size());
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

    private void initNewAssignment(ArrayList<Integer> tableNumbers) {

        mIntroFragment = JudgingIntroFragment.newInstance(tableNumbers);

        mHackFragments.clear();
        for (Integer i : tableNumbers) {
            mHackFragments.add(JudgingHackFragment.newInstance(i));
        }

        // TODO add submission fragment
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
                        new SuperlativeDialogSelectionListener(this, tableNumber))
                .positiveText(R.string.judging_superlative_dialog_choose)
                .show();

    }

    @Override
    public void addHackSuperlative(int tableNumber, String superlative) {

    }

    @Override
    public void submitHackScores(ArrayList<Integer> scoreOrder) {

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

    JudgeActivityFragmentAssistant init(int totalFragments) {
        this.totalFragments = totalFragments;
        this.fragmentIndex = 0;
        Log.d(JudgeActivityFragmentAssistant.class.getName(),
                String.format(Locale.US, "Initialized with %d fragments", totalFragments));
        showFragment(0, false);
        return this;
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
//            judgingActivity.finish();
            return;
        }

        Fragment frag;
        FragmentManager fm = judgingActivity.getSupportFragmentManager();

        switch (index) {
            case 0:
                frag = judgingActivity.mIntroFragment;
                break;
            case 4:
                // TODO
                frag = judgingActivity.mIntroFragment;
                break;
            default:
                frag = judgingActivity.mHackFragments.get(index - 1);
        }

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

class SuperlativeDialogSelectionListener implements MaterialDialog.ListCallbackMultiChoice {

    JudgingActivity mJudgingActivity;
    int mTableNumber;

    public SuperlativeDialogSelectionListener(JudgingActivity mJudgingActivity, int tableNumber) {
        this.mJudgingActivity = mJudgingActivity;
        this.mTableNumber = tableNumber;
    }

    @Override
    public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
        /**
         * If you use alwaysCallMultiChoiceCallback(), which is discussed below,
         * returning false here won't allow the newly selected check box to actually be selected
         * (or the newly unselected check box to be unchecked).
         * See the limited multi choice dialog example in the sample project for details.
         **/

        if (!mJudgingActivity.mNominations.containsKey(mTableNumber)) {
            mJudgingActivity.mNominations.put(mTableNumber, new ArrayList<String>());
        }

        // Store explicit checkbox selection for redos
        mJudgingActivity.mCheckboxSelections.put(mTableNumber, which);

        // Save nominations
        ArrayList<String> nominations = new ArrayList<>();
        for (int index : which) {
            String superlativeName = mJudgingActivity.mAvailableSuperlatives.get(index);
            nominations.add(superlativeName);
            Log.d(JudgingActivity.class.getName(),
                    String.format(Locale.US, "Nominated table %d for superlative %s",
                            mTableNumber, superlativeName));
        }
        mJudgingActivity.mNominations.put(mTableNumber, nominations);
        
        return true;
    }
}
