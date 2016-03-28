package com.example.riane.hanbaoreader_app.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.riane.hanbaoreader_app.R;
import com.example.riane.hanbaoreader_app.app.BaseActivity;
import com.example.riane.hanbaoreader_app.view.impl.ImportBookView;

import java.io.File;
import java.util.logging.Handler;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Xiamu on 2016/3/26.
 */
public class ImportBookActivity extends BaseActivity implements ImportBookView{

    @Bind(R.id.id_toolbar)
    Toolbar mToolbar;
    @Bind(R.id.tv_path)
    TextView mPathTextView;
    @Bind(R.id.tv_pre)
    TextView mPreviousTextView;
//    @Bind(R.id.rv_improtbook)
//    RecyclerView mRecyclerView;

    private File rootDir;
    private Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import);
        ButterKnife.bind(this);
        //清空默认title
        mToolbar.setTitle("");// 标题的文字需在setSupportActionBar之前，不然会无效
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImportBookActivity.this.onBackPressed();
            }
        });
    }

    @Override
    public void initToolbar() {

    }

    @Override
    public void initRecyclerView() {

    }

    @Override
    public void loadData() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
