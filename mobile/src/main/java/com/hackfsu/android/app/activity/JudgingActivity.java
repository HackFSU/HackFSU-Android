package com.hackfsu.android.app.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.hackfsu.android.api.JudgeAPI;
import com.hackfsu.android.app.R;
import com.hackfsu.android.app.fragment.judging.JudgingBaseFragment;
import com.hackfsu.android.app.fragment.judging.JudgingHackFragment;
import com.hackfsu.android.app.fragment.judging.JudgingIntroFragment;

import java.util.ArrayList;

public class JudgingActivity extends AppCompatActivity
        implements JudgingBaseFragment.OnJudgeFragmentInteractionListener {

    JudgingIntroFragment mIntroFragment;
    ArrayList<JudgingHackFragment> mHackFragments = new ArrayList<>();
    JudgeActivityFragmentAssistant assistant = new JudgeActivityFragmentAssistant(this);

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
                initNewAssignment(tableNumbers);
                assistant.init(tableNumbers.size());
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
        showFragment(0, false);
        return this;
    }

    boolean atFirstFragment() {
        return fragmentIndex == 0;
    }

//    boolean atLastFragment() {
//        return fragmentIndex == (totalFragments - 1);
//    }

    void showPrevFragment() {
        showFragment(--fragmentIndex, false);
    }

    void showNextFragment() {
        showFragment(++fragmentIndex, true);
    }

    private void showFragment(int index, boolean showAnimation) {

        if (index < 0 || index > 4) {
            Toast.makeText(judgingActivity, "Index error", Toast.LENGTH_SHORT).show();
            judgingActivity.finish();
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
