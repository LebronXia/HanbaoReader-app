package com.example.riane.hanbaoreader_app.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.riane.hanbaoreader_app.R;
import com.example.riane.hanbaoreader_app.app.BaseFragment;
import com.example.riane.hanbaoreader_app.config.Constant;
import com.example.riane.hanbaoreader_app.ui.adapter.PagerAdapter;
import com.example.riane.hanbaoreader_app.util.LogUtils;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Xiamu on 2016/3/17.
 */
public class BookStoreFragment extends BaseFragment {

    public static final String id_pos = "ID_POS";
    public static final String id_category = "ID_CATEGORY";
    @Bind(R.id.inner_viewpager)
    ViewPager mInnerViewpager;
    private View parentView;
    private SmartTabLayout mSmartTabLayout;
    private PagerAdapter mPagerAdapter;
    private List<BookListFragment> mFragments = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        parentView = View.inflate(getActivity(), R.layout.fragment_bookstore, null);
        ButterKnife.bind(this, parentView);
        mSmartTabLayout = (SmartTabLayout) getActivity().findViewById(R.id.tab_layout);
        mSmartTabLayout.setVisibility(View.VISIBLE);
        mPagerAdapter = new PagerAdapter(getChildFragmentManager(), Constant.Tag_Titles){
            @Override
            public Fragment getItem(int position) {
                BookListFragment fragment = new BookListFragment();
                Bundle bundle = new Bundle();
              //  LogUtils.d("Item + " + position);
               // LogUtils.d("Item + " + Constant.Tag_Titles[position]);
                bundle.putInt(id_pos, position);
                bundle.putString(id_category, Constant.Tag_Titles[position]);
                fragment.setArguments(bundle);
                return fragment;
            }
        };
        mInnerViewpager.setAdapter(mPagerAdapter);
        mSmartTabLayout.setViewPager(mInnerViewpager);
        return parentView;
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(getChildFragmentManager().getFragments()!=null){
            getChildFragmentManager().getFragments().clear();
            mSmartTabLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
