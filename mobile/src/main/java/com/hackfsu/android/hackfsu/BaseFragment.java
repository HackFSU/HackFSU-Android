package com.hackfsu.android.hackfsu;

import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;


public class BaseFragment extends Fragment {


    public interface OnFragmentInteractionListener {

        void registerToolbar(Toolbar toolbar);

    }

}
