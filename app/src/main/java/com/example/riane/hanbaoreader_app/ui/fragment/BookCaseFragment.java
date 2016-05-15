package com.example.riane.hanbaoreader_app.ui.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.example.riane.hanbaoreader_app.R;
import com.example.riane.hanbaoreader_app.app.BaseFragment;
import com.example.riane.hanbaoreader_app.cache.BookDao;
import com.example.riane.hanbaoreader_app.cache.BookTagDao;
import com.example.riane.hanbaoreader_app.modle.Book;
import com.example.riane.hanbaoreader_app.modle.BookTag;
import com.example.riane.hanbaoreader_app.ui.activity.ReadBookActivity;
import com.example.riane.hanbaoreader_app.ui.adapter.BookCaseAdapter;
import com.example.riane.hanbaoreader_app.util.LogUtils;
import com.example.riane.hanbaoreader_app.util.SPUtils;
import com.example.riane.hanbaoreader_app.util.ToastUtils;
import com.example.riane.hanbaoreader_app.widget.FlowLayout;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Xiamu on 2016/3/17.
 */
public class BookCaseFragment extends BaseFragment {

    private static final int DIALOG_BUTTON = 0;
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private BookCaseAdapter mCaseAdapter;
    @Bind(R.id.empty_view)
    RelativeLayout rl_emptyView;

    private List<Book> mDatas;
    private BookDao bookDao;
    private BookTagDao bookTagDao;
    private ProgressDialog dialog;
    private AlertDialog classDialog;
    public static boolean isCardVIew = false;
    String textTag;
    private SmartTabLayout mSmartTabLayout;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case DIALOG_BUTTON:
                    classDialog.dismiss();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View  parentView = View.inflate(getContext(), R.layout.fragment_bookcase,null);
        ButterKnife.bind(this, parentView);
        return parentView;
    }

    @Override
    public void onStart() {
        super.onStart();
        initDatas();
        initRecycleView();
    }

    public void initRecycleView(){
        //平铺视图GridLayoutManager
        if (isCardVIew){
            GridLayoutManager layout = new GridLayoutManager(getActivity(),3);
            mRecyclerView.setLayoutManager(layout);
        }  else {
            //创建默认的线性LayoutManager
            LinearLayoutManager layout = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(layout);
        }
       // mSmartTabLayout = (SmartTabLayout) getActivity().findViewById(R.id.tab_layout);

        mSmartTabLayout = (SmartTabLayout) getActivity().findViewById(R.id.tab_layout);
        mSmartTabLayout.setVisibility(View.GONE);
        //如果确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
        mCaseAdapter = new BookCaseAdapter(getActivity(),mDatas);
        mRecyclerView.setAdapter(mCaseAdapter);

        mCaseAdapter.setOnItemClickListener(new BookCaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //Toast.makeText(getActivity(), position + " click", Toast.LENGTH_SHORT).show();
                readBook(position);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                // Toast.makeText(getActivity(), position + " long click", Toast.LENGTH_SHORT).show();
                showDialog(position);
            }
        });

        // 设置item动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void showDialog(final int position) {
        final String items[] = {"删除书籍", "归档图书"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(mDatas.get(position).getName());
        //设置列表显示
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (items[which].equals("删除书籍")) {
                    bookDao.delete(mDatas.get(position));
                    mDatas.remove(position);
                    mCaseAdapter.notifyDataSetChanged();
                } else if (items[which].equals("归档图书")){
                    showClassifyDialog(position);
                }
            }
        });
        builder.create().show();
    }

    private void showClassifyDialog(final int position){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("归档图书");
        List<String> list = null;
        if (bookTagDao.getAll().size() < 1){
            String[] res = new String[]{"玄幻","言情","武侠","科幻","历史","科学"};
            list = Arrays.asList(res);
        } else {
            List<BookTag> bookTags = bookTagDao.getAll();
            //LogUtils.d("bookTag + "bookTags.get(0).getBookTag());
            list = new ArrayList<>();
            for (BookTag tag : bookTags){
                LogUtils.d("bookTag + " + tag.getBookTag());
                list.add(tag.getBookTag());
            }
        }

        final View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_dialog_classify, null);
        FlowLayout flowLayout = (FlowLayout) view.findViewById(R.id.fl_classlist);

        Button btn_class = (Button) view.findViewById(R.id.dialog_btn_class);
        flowLayout.setHorizontalSpacing(10);
        flowLayout.setVerticalSpacing(20);
        for (final String re : list){
            Button classButton = new Button(getActivity());
            classButton.setText(re);
            classButton.setWidth(30);
            classButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (bookTagDao.getBookTag(mDatas.get(position).getFilePath())== null){
                        BookTag bookTag = new BookTag(mDatas.get(position).getFilePath(),re);
                        bookTagDao.add(bookTag);
                    } else {
                        BookTag bookTag = new BookTag(mDatas.get(position).getFilePath(),re);
                        bookTagDao.update(bookTag);
                    }

                    //回调吗
                    Message message = new Message();
                    message.what = DIALOG_BUTTON;
                    mHandler.sendMessage(message);
                }
            });
            flowLayout.addView(classButton);
        }

        builder.setView(view);
        classDialog = builder.show();
        btn_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                classDialog.dismiss();
                EditText et_class = (EditText) view.findViewById(R.id.dialog_et_class);
                //查看标签是否存在，不存在则插入，存在则更新
                if (bookTagDao.getBookTag(mDatas.get(position).getFilePath())== null){
                    textTag = et_class.getText().toString().trim();
                    BookTag bookTag = new BookTag(mDatas.get(position).getFilePath(),textTag);
                    bookTagDao.add(bookTag);
                } else {
                    textTag = et_class.getText().toString().trim();
                    LogUtils.d("更新书的标签" + textTag);
                    BookTag bookTag = new BookTag(mDatas.get(position).getFilePath(),textTag);
                    bookTagDao.update(bookTag);
                }
            }
        });
    }

    private void initDatas() {
        bookDao = new BookDao(getActivity());
        bookTagDao = new BookTagDao(getActivity());
        mDatas = bookDao.getAll();
        if (mDatas == null || mDatas.size() < 1){
            mRecyclerView.setVisibility(View.GONE);
            rl_emptyView.setVisibility(View.VISIBLE);
        } else {
            rl_emptyView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
        isCardVIew = (boolean) SPUtils.get(getActivity(), "IS_CARD", false);
        LogUtils.d("isCardView+" + isCardVIew);
    }

    //阅读
    public void readBook(int position){
        dialog = ProgressDialog.show(getActivity(),"温馨提示","正在加载文件",true);
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
}
