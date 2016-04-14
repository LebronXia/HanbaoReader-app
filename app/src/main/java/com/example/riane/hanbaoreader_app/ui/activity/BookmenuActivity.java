package com.example.riane.hanbaoreader_app.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Toast;

import com.example.riane.hanbaoreader_app.R;
import com.example.riane.hanbaoreader_app.app.BaseActivity;
import com.example.riane.hanbaoreader_app.ui.adapter.BookMarkAdapter;
import com.example.riane.hanbaoreader_app.ui.adapter.BookTabAdapter;
import com.example.riane.hanbaoreader_app.ui.fragment.BookMarkFragment;
import com.example.riane.hanbaoreader_app.widget.BookMarkPopWinsow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Riane on 2016/4/10.
 */
public class BookmenuActivity extends BaseActivity{

    @Bind(R.id.tab_fragment_title)
    TabLayout tabLayout;
    @Bind(R.id.vp_fragment_pager)
    ViewPager viewPager;

    private BookMarkFragment bookMarkFragment;
    private BookMarkFragment bookMenuFragment;

    private List<Fragment> list_fragment;
    private List<String> list_title;

    private BookTabAdapter bookTabAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmenu);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        //初始化各fragment
        bookMarkFragment = new BookMarkFragment();
        bookMenuFragment = new BookMarkFragment();

        list_fragment = new ArrayList<>();
        list_fragment.add(bookMarkFragment);
        list_fragment.add(bookMenuFragment);

        list_title = new ArrayList<>();
        list_title.add("目录");
        list_title.add("书签");

        //设置Tablayout的模式
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        //为TabLayout添加tab名称
        tabLayout.addTab(tabLayout.newTab().setText(list_title.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(list_title.get(1)));

        bookTabAdapter = new BookTabAdapter(getSupportFragmentManager(),list_fragment,list_title);
        viewPager.setAdapter(bookTabAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabsFromPagerAdapter(bookTabAdapter);
    }


    public static Intent getCallingIntent(Context context){
        Intent intent = new Intent(context, BookmenuActivity.class);
        return intent;
    }
}
