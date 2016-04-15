package com.example.riane.hanbaoreader_app.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.riane.hanbaoreader_app.R;
import com.example.riane.hanbaoreader_app.app.BaseFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Xiamu on 2016/3/17.
 */
public class BookStoreFragment extends BaseFragment {

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.progre_wheel)
    ContentLoadingProgressBar progreWheel;
    @Bind(R.id.rl_bookstore)
    RelativeLayout rlBookstore;
    //    private ViewPager mViewPager;
//    private SmartTabLayout mSmartTabLayout;
    private View mParentView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mParentView = View.inflate(getContext(), R.layout.fragment_bookstore, null);
//        mViewPager = (ViewPager) mParentView.findViewById(R.id.book_viewpager);
//        mSmartTabLayout = (SmartTabLayout) getActivity().findViewById(R.id.tab_layout);

        ButterKnife.bind(this, mParentView);
        return mParentView;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
