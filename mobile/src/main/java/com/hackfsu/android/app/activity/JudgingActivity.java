package com.hackfsu.android.app.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.hackfsu.android.api.JudgeAPI;
import com.hackfsu.android.app.R;
import com.hackfsu.android.app.fragment.judging.JudgingBaseFragment;
import com.hackfsu.android.app.fragment.judging.JudgingHackFragment;
import com.hackfsu.android.app.fragment.judging.JudgingIntroFragment;

import java.util.ArrayList;

public class JudgingActivity extends AppCompatActivity
        implements JudgingBaseFragment.OnJudgeFragmentInteractionListener {

    FrameLayout mFragmentAnchor;
    int fragmentIndex;


    JudgingIntroFragment mIntroFragment;
    ArrayList<JudgingHackFragment> mHackFragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_judging);

        JudgeAPI api = new JudgeAPI(this);

        api.getHackAssignment(new JudgeAPI.onAssignmentRetrievedListener() {
            @Override
            public void onAssignment(ArrayList<Integer> tableNumbers) {
                initNewAssignment(tableNumbers);
                showFragment(0);
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

    private void showFragment(int index) {

        if (index < 0 || index > 4) {
            finish();
            return;
        }

        FragmentManager fm = getSupportFragmentManager();
        Fragment frag = null;

        switch(index) {
            case 0:
                frag = mIntroFragment;
                break;
            case 4:
                // TODO
                frag = mIntroFragment;
            default:
                frag = mHackFragments.get(index - 1);
        }

        if (frag != null) {
            fm.beginTransaction().replace(R.id.fragment_anchor_judging, frag).commit();
        }

    }


    @Override
    public void showNextTable() {

    }

    @Override
    public void showPreviousTable() {

    }

    @Override
    public void addHackSuperlative(int tableNumber, String superlative) {

    }

    @Override
    public void submitHackScores(ArrayList<Integer> scoreOrder) {

    }
}
