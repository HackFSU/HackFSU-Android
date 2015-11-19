package com.hackfsu.hackfsu_android;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.view.View;


public class ViewPagerBehavior extends CoordinatorLayout.Behavior<ViewPager> {

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, ViewPager child, View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, ViewPager child, View dependency) {

        float translationY = Math.min(0, dependency.getTranslationY() - dependency.getHeight());
        child.setTranslationY(translationY);

        return true;
    }
}
