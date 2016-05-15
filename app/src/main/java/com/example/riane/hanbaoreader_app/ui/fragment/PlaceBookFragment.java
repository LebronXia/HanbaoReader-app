package com.example.riane.hanbaoreader_app.ui.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.riane.hanbaoreader_app.R;
import com.example.riane.hanbaoreader_app.app.BaseFragment;
import com.example.riane.hanbaoreader_app.cache.BookDao;
import com.example.riane.hanbaoreader_app.config.Constant;
import com.example.riane.hanbaoreader_app.modle.Book;
import com.example.riane.hanbaoreader_app.ui.activity.ReadBookActivity;
import com.example.riane.hanbaoreader_app.ui.adapter.BookCaseAdapter;
import com.example.riane.hanbaoreader_app.ui.adapter.PlaceBookAdapter;
import com.example.riane.hanbaoreader_app.util.LogUtils;
import com.example.riane.hanbaoreader_app.util.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Riane on 2016/4/14.
 */
public class PlaceBookFragment extends BaseFragment {
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.empty_view)
    RelativeLayout mEmptyView;
    private List<String> bookPaths;
    private PlaceBookAdapter mPlaceBookAdapter;
    private List<Book> mDatas;
    private BookDao mBookDao;
    private ProgressDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frament_placebook, container, false);
        bookPaths = getArguments().getStringArrayList(Constant.BUNDLE_PLACE);
        mBookDao = new BookDao(getActivity());
        for (String bookPath : bookPaths){
            mDatas = new ArrayList<>();
            Book book = mBookDao.getBookBybookPath(bookPath);
            mDatas.add(book);
        }
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        //创建默认的线性LayoutManager
        LinearLayoutManager layout = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layout);
        //如果确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
        if (mDatas == null || mDatas.size() < 1){
            mEmptyView.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            mEmptyView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);

        }

        mPlaceBookAdapter = new PlaceBookAdapter(getActivity(),mDatas);
        mRecyclerView.setAdapter(mPlaceBookAdapter);
        mPlaceBookAdapter.setOnItemClickListener(new PlaceBookAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //Toast.makeText(getActivity(), position + " click", Toast.LENGTH_SHORT).show();
                readBook(position);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                // Toast.makeText(getActivity(), position + " long click", Toast.LENGTH_SHORT).show();
                // showDialog(position);
            }
        });

        // 设置item动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    //阅读
    public void readBook(int position){
        dialog = ProgressDialog.show(getActivity(), "温馨提示", "正在加载文件", true);
        Book book = mDatas.get(position);
        File file = new File(book.getFilePath());
        if (!file.exists()){
            ToastUtils.showShort(getActivity(), "文件不存在");
            dialog.dismiss();
            return;
        } else {
            dialog.dismiss();
            startActivity(ReadBookActivity.getCallingIntent(getActivity(), book));
        }

    }

    public static PlaceBookFragment newInstance(ArrayList<String> bookPaths) {
        //通过Bundle保存数据
        Bundle args = new Bundle();
        // args.putString(Constant.BUNDLE_PLACE, bookPath);
        args.putStringArrayList(Constant.BUNDLE_PLACE, bookPaths);
        PlaceBookFragment fragment = new PlaceBookFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        mPlaceBookAdapter.notifyDataSetChanged();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
