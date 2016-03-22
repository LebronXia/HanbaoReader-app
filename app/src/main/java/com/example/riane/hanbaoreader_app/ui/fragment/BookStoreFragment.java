package com.example.riane.hanbaoreader_app.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.riane.hanbaoreader_app.R;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

/**
 * Created by Xiamu on 2016/3/17.
 */
public class BookStoreFragment extends Fragment{

    private ViewPager mViewPager;
    private SmartTabLayout mSmartTabLayout;
    private View mParentView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mParentView = View.inflate(getContext(), R.layout.fragment_bookstore, null);
        mViewPager = (ViewPager) mParentView.findViewById(R.id.book_viewpager);
        mSmartTabLayout = (SmartTabLayout) getActivity().findViewById(R.id.tab_layout);
        return mParentView;
    }
}
