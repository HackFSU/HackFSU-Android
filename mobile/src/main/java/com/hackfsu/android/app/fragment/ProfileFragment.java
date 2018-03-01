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
        TextView text = (TextView) v.findViewById(R.id.textView2);
        text.setText(preferences.getString("first_name", null));



        Button button1 = (Button) v.findViewById(R.id.button1);
        Button button2 = (Button) v.findViewById(R.id.button2);

        button1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(getContext(), JudgingActivity.class));
            }
        });

        button2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){


                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                final SharedPreferences.Editor edit = preferences.edit();
               edit.clear();
               // edit.putString("Logged_user", null);
                edit.putStringSet(PREF_COOKIES, null);
                edit.commit();


                startActivity(new Intent(getContext(), LoginActivity.class));
                getActivity().finish();
            }
        });


        return v;


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }




}
