package com.hackfsu.hackfsu15;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for the Tab Swipe
 * ToDO: Countdown should stay on. Might have to implement it like the IOS version.
 *
 * @author Iosif
 */

public class mainPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> allFragments;
    private Context context;

    public mainPagerAdapter(FragmentManager fm){
        super(fm);
        this.allFragments = new ArrayList<Fragment>();
        allFragments.add( new CountdownFragment() );
        allFragments.add( new UpdatesFragment()   );
        allFragments.add( new ScheduleFragment()  );
        allFragments.add( new MapFragment()       );
        allFragments.add( new SponsorsFragment()  );
    }

    @Override
    public Fragment getItem(int position) {
        return allFragments.get(position);
    }

    @Override
    public int getCount() {
        return allFragments.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

}
