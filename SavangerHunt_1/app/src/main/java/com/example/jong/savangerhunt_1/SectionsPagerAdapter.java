package com.example.jong.savangerhunt_1;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.Locale;

/**
 * Created by root on 10/22/15.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private StageData stageData;
    private int currStage;
    private int visibleStage;
    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        Bundle args = new Bundle();
        android.support.v4.app.Fragment fragment;
        switch (position) {

            case 0:
                fragment = new TabbedFragment_clip();
                args.putInt(TabbedFragment_clip.ARG_SECTION_NUMBER, position + 1);
                fragment.setArguments(args);
                return fragment;

            case 1:
                    fragment = new TabbedFragment_map();
                    args.putInt(TabbedFragment_map.ARG_SECTION_NUMBER, position + 1);
                    fragment.setArguments(args);
                    return fragment;
        }
        return null;
    }

    private static String makeFragmentName(int viewId, int index) {
        return "android:switcher:" + viewId + ":" + index;
    }

    public Fragment getActiveFragment(FragmentManager fm, ViewPager container, int position) {
        String name = makeFragmentName(container.getId(), position);
        return  fm.findFragmentByTag(name);
    }
}