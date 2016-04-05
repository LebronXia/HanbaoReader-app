package com.example.riane.hanbaoreader_app.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.riane.hanbaoreader_app.R;
import com.example.riane.hanbaoreader_app.app.BaseActivity;
import com.example.riane.hanbaoreader_app.cache.BookDao;
import com.example.riane.hanbaoreader_app.modle.Book;
import com.example.riane.hanbaoreader_app.modle.FileItem;
import com.example.riane.hanbaoreader_app.ui.adapter.SearchResultAdapter;
import com.example.riane.hanbaoreader_app.util.ToolUtils;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.refactor.library.SmoothCheckBox;

/**
 * Created by Xiamu on 2016/3/29.
 */
public class SearchBookActivity extends BaseActivity {

    private static final String EXTRA_SEARCH_BOOK = "com.riane.extra_search_book";

    @Bind(R.id.id_toolbar)
    Toolbar mToolbar;
    @Bind(R.id.toolbar_txt)
    TextView mToolbarText;
    @Bind(R.id.toolbar_txt_btn)
    TextView mBtnToolbar;
    @Bind(R.id.txt_recycle_view)
    RecyclerView mFifleRecycleView;
    @Bind(R.id.item_fifle_txt)
    TextView fifletext;

    //扫描接收的数据
    private ArrayList<String> mSearchitems;
    private ArrayList<FileItem> mFileItems;
    private FileItem mFileItem;
    private SearchResultAdapter mSearchResultAdapter;
    BookDao bookDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(SearchBookActivity.this);
        initData();
        initView();
    }

    private void initData() {
        bookDao = new BookDao(SearchBookActivity.this);
        mSearchitems = new ArrayList<String>();
        mFileItems = new ArrayList<FileItem>();
        mSearchitems = (ArrayList<String>) this.getIntent().getSerializableExtra(EXTRA_SEARCH_BOOK);
        if (mSearchitems != null && mSearchitems.size() > 0){
            for (int i = 0; i < mSearchitems.size(); i++){
                String path = mSearchitems.get(i);
                File file = new File(path);
                mFileItem = new FileItem(file.getName(),SearchBookActivity.this.getResources().getDrawable(R.mipmap.file_txt),
                        file.getPath(), ToolUtils.FormetFileSize(file.length()));
                mFileItems.add(mFileItem);
            }
        }
        //设置是否导入图书
        List<Book> books = bookDao.getAll();
        for (int i = 0; i < books.size(); i++){
            for (int j = 0; j < mFileItems.size(); j++){
                if (books.get(i).getName().equals(mFileItems.get(j).getFileName())){
                    mFileItems.get(j).setIsImport(true);
                }
            }
        }
    }

    private void initView() {
        //清空默认title
        mToolbar.setTitle("");// 标题的文字需在setSupportActionBar之前，不然会无效
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbarText.setText("扫描结果");
        mBtnToolbar.setText("全选");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ImportBookActivity.this.onBackPressed();
                finish();
            }
        });

        fifletext.setText("共为你搜索到" + mFileItems.size() + "个文件");
        mSearchResultAdapter = new SearchResultAdapter(SearchBookActivity.this,mFileItems);
        mSearchResultAdapter.setOnItemClickListener(new SearchResultAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                FileItem fileItem = mFileItems.get(position);
                fileItem.setIsChecked(!fileItem.isChecked());
                SmoothCheckBox checkBox = (SmoothCheckBox) view.findViewById(R.id.scb_check);
                checkBox.setChecked(fileItem.isChecked(), true);
            }
        });

        mFifleRecycleView.setLayoutManager(new LinearLayoutManager(SearchBookActivity.this));
        mFifleRecycleView.setAdapter(mSearchResultAdapter);
    }

    @OnClick(R.id.toolbar_txt_btn)
    public void setBtnToolbar(){
        if (mBtnToolbar.getText() == "全选"){
            for (FileItem fileItem:mFileItems
                    ) { fileItem.setIsChecked(true);
            }
            mBtnToolbar.setText("全不选");
        } else {
            for (FileItem fileItem:mFileItems
                    ) { fileItem.setIsChecked(false);
            }
            mBtnToolbar.setText("全选");
        }
        mSearchResultAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.btn_improt)
    public void setImportBookBtn(){

        for (int i = 0 ; i < mFileItems.size(); i ++){
            if (mFileItems.get(i).isChecked() == true){
                String filename = mFileItems.get(i).getFileName();
                String filepath = mFileItems.get(i).getFilepath();

                    Book book = new Book(filename,filepath, new Date(), 0, "0.00%");
                    bookDao.add(book);

            }
        }
    }

    public static Intent getCallingIntent(Context context, ArrayList<String> searchitems){
        Intent intent = new Intent(context, SearchBookActivity.class);
        //intent.putStringArrayListExtra(EXTRA_SEARCH_BOOK,searchitems);
        intent.putExtra(EXTRA_SEARCH_BOOK,(Serializable)searchitems);
        return intent;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mFileItems = null;
        mSearchitems = null;
    }
}
