package com.example.jong.savangerhunt_1;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class ViewpagerContainer extends Fragment {
    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;

    private OnFragmentInteractionListener mListener;


    public static ViewpagerContainer newInstance() {
        ViewpagerContainer fragment = new ViewpagerContainer();
        return fragment;
    }

    public ViewpagerContainer() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_viewpager_container, container, false);
        mViewPager=(ViewPager) v.findViewById(R.id.pager);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(0);
        return v;
    }

    public Fragment getviewpagerfragment(int page){
        return mSectionsPagerAdapter.getActiveFragment(getChildFragmentManager(),mViewPager,page);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
