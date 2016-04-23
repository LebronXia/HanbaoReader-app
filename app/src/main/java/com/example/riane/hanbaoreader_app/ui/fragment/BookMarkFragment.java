package com.example.riane.hanbaoreader_app.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.riane.hanbaoreader_app.R;
import com.example.riane.hanbaoreader_app.cache.BookDao;
import com.example.riane.hanbaoreader_app.cache.BookMarkDao;
import com.example.riane.hanbaoreader_app.modle.Book;
import com.example.riane.hanbaoreader_app.modle.BookMark;
import com.example.riane.hanbaoreader_app.ui.activity.ReadBookActivity;
import com.example.riane.hanbaoreader_app.ui.adapter.BookCaseAdapter;
import com.example.riane.hanbaoreader_app.ui.adapter.BookMarkAdapter;
import com.example.riane.hanbaoreader_app.util.LogUtils;
import com.example.riane.hanbaoreader_app.widget.BookMarkPopWinsow;
import com.example.riane.hanbaoreader_app.widget.RecycleViewDivider;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Riane on 2016/4/10.
 */
public class BookMarkFragment extends Fragment{
    @Bind(R.id.bookmark_recycle_view)
    RecyclerView bookmarkrecycleview;
    @Bind(R.id.empty_view)
    RelativeLayout rl_emptyView;
    private BookMarkAdapter bookMarkAdapter;
    private List<BookMark> bookMarklist;
    private View view;
    BookMarkDao bookMarkDao;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_bookmenu,container,false);
        ButterKnife.bind(this, view);
        initData();
        return view;
    }

    private void initData() {
        bookMarkDao = new BookMarkDao(getActivity());
        bookMarklist = bookMarkDao.getAll();
        //LogUtils.d("bookMarklist" + bookMarklist.get(0).getTextword());
        if (bookMarklist == null || bookMarklist.size() < 1){
            bookmarkrecycleview.setVisibility(View.GONE);
            rl_emptyView.setVisibility(View.VISIBLE);
        } else {
            rl_emptyView.setVisibility(View.GONE);
            bookmarkrecycleview.setVisibility(View.VISIBLE);
            initRecycleView();
        }
    }

    private void initRecycleView() {
        //创建默认的线性LayoutManager
        bookmarkrecycleview.setLayoutManager(new LinearLayoutManager(getContext()));
        //如果确定每个item的高度是固定的，设置这个选项可以提高性能
        bookmarkrecycleview.setHasFixedSize(true);
        //加下划线
        bookmarkrecycleview.addItemDecoration(new RecycleViewDivider(getContext(), LinearLayoutManager.VERTICAL));
        bookMarkAdapter = new BookMarkAdapter(getActivity(),bookMarklist);
        bookmarkrecycleview.setAdapter(bookMarkAdapter);

        bookMarkAdapter.setOnItemClickListener(new BookMarkAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                BookMark bookMark = bookMarklist.get(position);
                Book book = new Book();
                book.setFilePath(bookMark.getBookpath());
                book.setBegin(bookMark.getBegin());
                startActivity(ReadBookActivity.getCallingIntent(getActivity(), book));
            }

            @Override
            public void onItemLongClick(View view, int position) {
                showPopFormBottom(view, position);
            }
        });

        // 设置item动画
        bookmarkrecycleview.setItemAnimator(new DefaultItemAnimator());
    }

    public void showPopFormBottom(View view, final int position){
        final boolean isDelete = false;
        BookMarkPopWinsow bookMarkPopWinsow = new BookMarkPopWinsow(getActivity(), new View.OnClickListener() {
            BookMark bookMark = bookMarklist.get(position);

            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.tv_toactivity:

                        Book book = new Book();
                        book.setFilePath(bookMark.getBookpath());
                        book.setBegin(bookMark.getBegin());
                        startActivity(ReadBookActivity.getCallingIntent(getActivity(), book));

                        break;
                    case R.id.tv_deletemark:
                        bookMarkDao.delete(bookMark);
                        bookMarkAdapter.notifyDataSetChanged();
                        break;
                }
            }

        });
        bookMarkPopWinsow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().finish();
    }
}
