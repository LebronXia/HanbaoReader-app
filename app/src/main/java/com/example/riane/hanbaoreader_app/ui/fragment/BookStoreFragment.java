package com.example.riane.hanbaoreader_app.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.riane.hanbaoreader_app.R;
import com.example.riane.hanbaoreader_app.app.BaseFragment;
import com.example.riane.hanbaoreader_app.modle.entity.BookVO;
import com.example.riane.hanbaoreader_app.modle.entity.UserVO;
import com.example.riane.hanbaoreader_app.presenter.impl.BookStorePresent;
import com.example.riane.hanbaoreader_app.util.LogUtils;
import com.example.riane.hanbaoreader_app.util.ToastUtils;
import com.example.riane.hanbaoreader_app.view.impl.BookStoreView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Xiamu on 2016/3/17.
 */
public class BookStoreFragment extends BaseFragment implements BookStoreView{

//    @Bind(R.id.recycler_view)
//    RecyclerView recyclerView;
    @Bind(R.id.progre_wheel)
    ContentLoadingProgressBar progreWheel;
    @Bind(R.id.rl_bookstore)
    RelativeLayout rlBookstore;
    //    private ViewPager mViewPager;
//    private SmartTabLayout mSmartTabLayout;
    private View view;
    private BookStorePresent mBookStorePresent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        mParentView = View.inflate(getContext(), R.layout.fragment_bookstore, false);
//        mViewPager = (ViewPager) mParentView.findViewById(R.id.book_viewpager);
//        mSmartTabLayout = (SmartTabLayout) getActivity().findViewById(R.id.tab_layout);
        view = inflater.inflate(R.layout.fragment_bookstore,container,false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        mBookStorePresent = new BookStorePresent(this);
        progreWheel.setVisibility(View.VISIBLE);

        //制造延时
        new Handler().postDelayed(new Runnable()  {
            @Override
            public void run() {
                mBookStorePresent.loadData();
            }
        },2000);
    }

    @Override
    public void showData(UserVO userVO) {
        LogUtils.d("user的数据" + userVO.getId() + userVO.getName());
        ToastUtils.showShort(getActivity(),"user的数据" + userVO.getId() + userVO.getName() );
    }

    @Override
    public void showProgress() {
        progreWheel.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progreWheel.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        mBookStorePresent.detachView();
        super.onDestroyView();
        ButterKnife.unbind(this);

    }
}
