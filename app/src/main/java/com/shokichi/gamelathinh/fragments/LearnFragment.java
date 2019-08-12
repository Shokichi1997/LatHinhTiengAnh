package com.shokichi.gamelathinh.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shokichi.gamelathinh.R;
import com.shokichi.gamelathinh.adapters.ScreenSliderPagerAdapter;

public class LearnFragment extends Fragment {
    private ViewPager mPager;

    private PagerAdapter mPagerAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = ( ViewGroup ) inflater.inflate(R.layout.activity_screen_slide,container,false);

        mPager = (ViewPager ) root.findViewById(R.id.pager);
        mPagerAdapter = new ScreenSliderPagerAdapter(getChildFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        return root;
    }
}
