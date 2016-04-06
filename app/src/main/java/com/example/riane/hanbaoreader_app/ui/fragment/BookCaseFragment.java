package com.example.riane.hanbaoreader_app.ui.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.riane.hanbaoreader_app.R;
import com.example.riane.hanbaoreader_app.app.BaseFragment;
import com.example.riane.hanbaoreader_app.cache.BookDao;
import com.example.riane.hanbaoreader_app.modle.Book;
import com.example.riane.hanbaoreader_app.ui.activity.ReadBookActivity;
import com.example.riane.hanbaoreader_app.ui.adapter.BookCaseAdapter;
import com.example.riane.hanbaoreader_app.util.ToastUtils;

import java.io.File;
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
    @Bind(R.id.empty_view)
    RelativeLayout rl_emptyView;
    private List<Book> mDatas;

    private ProgressDialog dialog;

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
        StaggeredGridLayoutManager layout = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layout);
        //如果确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
        mCaseAdapter = new BookCaseAdapter(getActivity(),mDatas);
        mRecyclerView.setAdapter(mCaseAdapter);

        mCaseAdapter.setOnItemClickListener(new BookCaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getActivity(), position + " click", Toast.LENGTH_SHORT).show();
                readBook(position);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(getActivity(), position + " long click", Toast.LENGTH_SHORT).show();
            }
        });

        // 设置item动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


    }

    private void initDatas() {
        BookDao bookDao = new BookDao(getActivity());
        mDatas = bookDao.getAll();
        if (mDatas == null && mDatas.size() < 1){
            mRecyclerView.setVisibility(View.GONE);
            rl_emptyView.setVisibility(View.VISIBLE);
        } else {
            rl_emptyView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    //阅读
    public void readBook(int position){
        dialog = ProgressDialog.show(getActivity(),"温馨提示","正在加载文件",true);
        Book book = mDatas.get(position);
        File file = new File(book.getFilePath());
        if (!file.exists()){
            ToastUtils.toast(getActivity(), "文件不存在");
            dialog.dismiss();
            return;
        }
        dialog.dismiss();
       startActivity(ReadBookActivity.getCallingIntent(getActivity(), book));
    }
}
