package com.example.yelp.ui.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class MyViewPagerAdapter extends FragmentStatePagerAdapter {
    Result result;
    public MyViewPagerAdapter(FragmentManager fm,Result result) {
        super(fm);
        this.result = result;
    }
    @Override
    public Fragment getItem(int i) {
        Fragment fragment = null;
        switch (i){
            case 1:fragment = new MapsFragment(result);
                break;
            case 2:fragment = new ReviewFragment(result);
                break;
            default:
                fragment = new BussinessDetailsFragment(result);
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:return "BUSINESS DETAILS";
            case 1:return "MAP LOCATION";
            case 2:return "REVIEWS";
        }
        return "";
    }
}