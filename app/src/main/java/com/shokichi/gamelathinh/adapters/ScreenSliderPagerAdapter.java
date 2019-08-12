package com.shokichi.gamelathinh.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


import com.shokichi.gamelathinh.fragments.ChoosePraticeFragment;
import com.shokichi.gamelathinh.fragments.ScreenSlideFragment;

public class ScreenSliderPagerAdapter extends FragmentStatePagerAdapter {
    /**
     * The number of pages to show in this screen
     */
    private static final int NUM_PAGES = 9;
    private static final String ARG_KEY = "page";

    public ScreenSliderPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if(position<NUM_PAGES-1){
            Fragment fragment = new ScreenSlideFragment();
            Bundle args = new Bundle();
            //put page number
            args.putInt(ARG_KEY,position);
            fragment.setArguments(args);
            return fragment;
        }
        else {
            return new ChoosePraticeFragment();
        }


    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }
}
