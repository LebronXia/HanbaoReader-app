package com.example.riane.hanbaoreader_app.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.riane.hanbaoreader_app.R;
import com.example.riane.hanbaoreader_app.modle.Book;
import com.example.riane.hanbaoreader_app.modle.entity.BookVO;
import com.example.riane.hanbaoreader_app.modle.entity.UserVO;
import com.example.riane.hanbaoreader_app.presenter.impl.BookStorePresent;
import com.example.riane.hanbaoreader_app.ui.adapter.BookCaseAdapter;
import com.example.riane.hanbaoreader_app.ui.adapter.BookStroeListAdapter;
import com.example.riane.hanbaoreader_app.util.LogUtils;
import com.example.riane.hanbaoreader_app.util.ToastUtils;
import com.example.riane.hanbaoreader_app.view.impl.BookStoreView;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Riane on 2016/4/20.
 */
public class BookListFragment extends Fragment implements BookStoreView {
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.progre_wheel)
    ContentLoadingProgressBar progreWheel;
    @Bind(R.id.rl_booklist)
    RelativeLayout rlBookstore;
    @Bind(R.id.pull_to_refresh)
    PullToRefreshView mPullToRefreshView;
    @Bind(R.id.tv_load_empty)
    TextView mTvLoadEmpty;
    @Bind(R.id.tv_load_error)
    TextView mTvLoadError;

    private View view;
    private BookStroeListAdapter mBookStroeListAdapter;
    private BookStorePresent mBookStorePresent;
    private boolean withRefreshView = true;
    private String tag;
    private int position;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        mParentView = View.inflate(getContext(), R.layout.fragment_bookstore, false);
//        mViewPager = (ViewPager) mParentView.findViewById(R.id.book_viewpager);
//        mSmartTabLayout = (SmartTabLayout) getActivity().findViewById(R.id.tab_layout);
        view = inflater.inflate(R.layout.fragmnet_bookstore_list,container,false);
        ButterKnife.bind(this, view);
        initData();
        initView();
        return view;
    }

    private void initData() {
        mBookStorePresent = new BookStorePresent(this);
        position = getArguments().getInt(BookStoreFragment.id_pos);
        tag = getArguments().getString(BookStoreFragment.id_category);
    }

    private void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //如果确定每个item的高度是固定的，设置这个选项可以提高性能
        recyclerView.setHasFixedSize(true);

        progreWheel.setVisibility(View.VISIBLE);
        mBookStorePresent.loadData(tag);
//        //制造延时
//        new Handler().postDelayed(new Runnable()  {
//            @Override
//            public void run() {
//                mBookStorePresent.loadData(tag);
//            }
//        },5000);
        if (withRefreshView){
            mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener(){
                @Override
                public void onRefresh() {
                    mBookStorePresent.loadData(tag);
                }
            });
        }
    }

    @Override
    public void showData(List<BookVO> bookVOs) {
        LogUtils.d("user的数据" + bookVOs.get(0).getName());
       ToastUtils.showShort(getActivity(), "user的数据" + bookVOs.get(0).getName() + bookVOs.get(0).getAuthor());
        mBookStroeListAdapter = new BookStroeListAdapter(getActivity(),bookVOs);
        recyclerView.setAdapter(mBookStroeListAdapter);

        mBookStroeListAdapter.setOnItemClickListener(new BookStroeListAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

        // 设置item动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    @Override
    public void showProgress() {
        progreWheel.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progreWheel.setVisibility(View.GONE);
        mPullToRefreshView.setRefreshing(false);
    }

    @Override
    public void showLoadEmpty() {
        mTvLoadEmpty.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadEmpty() {
        mTvLoadEmpty.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        mBookStorePresent.detachView();
        super.onDestroyView();
        ButterKnife.unbind(this);

    }

}
