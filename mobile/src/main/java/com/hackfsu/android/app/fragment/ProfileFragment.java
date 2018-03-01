package com.hackfsu.android.app.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hackfsu.android.api.API;
import com.hackfsu.android.app.R;
import com.hackfsu.android.app.activity.JudgingActivity;
import com.hackfsu.android.app.activity.LoginActivity;

import java.io.File;
import java.util.HashSet;

import static com.hackfsu.android.api.util.AddCookiesInterceptor.PREF_COOKIES;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment .OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends BaseFragment {

    API mAPI;

    BaseFragment.OnFragmentInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance () { return new ProfileFragment();}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        TextView userNameText = (TextView) v.findViewById(R.id.textView2);
        userNameText.setText(preferences.getString("first_name", null));


        Button judgingButton = (Button) v.findViewById(R.id.button1);
        judgingButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(getContext(), JudgingActivity.class));
            }
        });

        return v;


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


}
