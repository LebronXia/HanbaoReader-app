package com.example.riane.hanbaoreader_app.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.riane.hanbaoreader_app.R;
import com.example.riane.hanbaoreader_app.app.BaseFragment;
import com.example.riane.hanbaoreader_app.ui.adapter.BookCaseAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Xiamu on 2016/3/17.
 */
public class BookCaseFragment extends BaseFragment {

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private BookCaseAdapter mCaseAdapter;
    private List<Integer> mDatas;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View  parentView = View.inflate(getContext(), R.layout.fragment_bookcase,null);
        ButterKnife.bind(this, parentView);
        initDatas();
        initRecycleView();
        return parentView;
    }

    public void initRecycleView(){
        //创建默认的线性LayoutManager
        //GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),3);
        StaggeredGridLayoutManager layout = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layout);
        //如果确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
        mCaseAdapter = new BookCaseAdapter(getActivity(),mDatas);
        mRecyclerView.setAdapter(mCaseAdapter);

        mCaseAdapter.setOnItemClickListener(new BookCaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getActivity(), position + " click", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(getActivity(), position + " long click", Toast.LENGTH_SHORT).show();
            }
        });

        // 设置item动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


    }

    private void initDatas()
    {
        mDatas = new ArrayList<Integer>(Arrays.asList(R.mipmap.mao,
                R.mipmap.mao, R.mipmap.mao, R.mipmap.mao, R.mipmap.mao,
                R.mipmap.mao, R.mipmap.mao, R.mipmap.mao, R.mipmap.mao));
    }
}
