package com.hackfsu.android.app.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;


public class BaseFragment extends Fragment {


    public interface OnFragmentInteractionListener {

        void registerToolbar(Toolbar toolbar);

    }

    public interface RefreshActionListener {
        void onComplete();
    }

    protected class RefreshCompleteListener implements RefreshActionListener {

        SwipeRefreshLayout mSwipeRefreshLayout;

        public RefreshCompleteListener(SwipeRefreshLayout mSwipeRefreshLayout) {
            this.mSwipeRefreshLayout = mSwipeRefreshLayout;
        }

        @Override
        public void onComplete() {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

}
