package com.example.riane.hanbaoreader_app.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Riane on 2016/4/20.
 */
public abstract class PagerAdapter extends FragmentStatePagerAdapter{

    private String[] titles;
    public PagerAdapter(FragmentManager fm, String[] titles) {
        super(fm);
        this.titles = titles;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

//    @Override
//    public Fragment getItem(int position) {
//        return null;
//    }
}
