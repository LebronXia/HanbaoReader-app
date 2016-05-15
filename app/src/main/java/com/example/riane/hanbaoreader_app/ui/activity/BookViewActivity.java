package com.example.riane.hanbaoreader_app.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.riane.hanbaoreader_app.R;
import com.example.riane.hanbaoreader_app.app.BaseActivity;
import com.example.riane.hanbaoreader_app.modle.Book;
import com.example.riane.hanbaoreader_app.modle.entity.BookVO;
import com.example.riane.hanbaoreader_app.ui.fragment.BookViewFragment;
import com.example.riane.hanbaoreader_app.ui.fragment.SettingFragment;

import java.io.Serializable;

/**
 * Created by Riane on 2016/5/7.
 */
public class BookViewActivity extends BaseActivity{
    private final static String EXTRA_BOOKVIEW = "extra_bookview";
    private Toolbar toolbar;
    private BookVO mBookVO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mBookVO = (BookVO) getIntent().getSerializableExtra(EXTRA_BOOKVIEW);
        toolbar = (Toolbar) findViewById(R.id.toolbar_setting);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(mBookVO.getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        BookViewFragment bookViewFragment = BookViewFragment.newInstance(mBookVO);
        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_setting,bookViewFragment).commit();
    }

    public static Intent getCallingIntent(Context context, BookVO bookVO) {
        Intent intent = new Intent(context, BookViewActivity.class);
        intent.putExtra(EXTRA_BOOKVIEW, (Serializable) bookVO);
        return intent;
    }
}
