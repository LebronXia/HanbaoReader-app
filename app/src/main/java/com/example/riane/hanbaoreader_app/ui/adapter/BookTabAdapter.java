package com.example.riane.hanbaoreader_app.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Riane on 2016/4/10.
 */
public class BookTabAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragmentlist;
    private List<String> mTitleList;

    public BookTabAdapter(FragmentManager fm, List<Fragment> fragmentlist, List<String> titleList) {
        super(fm);
        this.mFragmentlist = fragmentlist;
        this.mTitleList = titleList;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentlist.get(position);
    }

    @Override
    public int getCount() {
        return mTitleList.size();
    }

    //显示tabs上的名字
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitleList.get(position % mTitleList.size());
    }
}
