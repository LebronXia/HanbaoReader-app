package com.example.riane.hanbaoreader_app.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.riane.hanbaoreader_app.R;
import com.example.riane.hanbaoreader_app.modle.entity.UserVO;
import com.example.riane.hanbaoreader_app.presenter.impl.BookStorePresent;
import com.example.riane.hanbaoreader_app.util.LogUtils;
import com.example.riane.hanbaoreader_app.util.ToastUtils;
import com.example.riane.hanbaoreader_app.view.impl.BookStoreView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Riane on 2016/4/20.
 */
public class BookListFragment extends Fragment implements BookStoreView {
    //    @Bind(R.id.recycler_view)
//    RecyclerView recyclerView;
    @Bind(R.id.progre_wheel)
    ContentLoadingProgressBar progreWheel;
    @Bind(R.id.rl_booklist)
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
        view = inflater.inflate(R.layout.fragmnet_bookstore_list,container,false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        mBookStorePresent = new BookStorePresent(this);
        progreWheel.setVisibility(View.VISIBLE);
        mBookStorePresent.loadData();
        //制造延时
        new Handler().postDelayed(new Runnable()  {
            @Override
            public void run() {
                mBookStorePresent.loadData();
            }
        },5000);
    }

    @Override
    public void showData(UserVO userVO) {
        LogUtils.d("user的数据" + userVO.getId() + userVO.getName());
        ToastUtils.showShort(getActivity(), "user的数据" + userVO.getId() + userVO.getName());
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
