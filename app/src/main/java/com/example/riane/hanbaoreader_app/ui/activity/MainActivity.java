package com.example.riane.hanbaoreader_app.ui.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;

import com.example.riane.hanbaoreader_app.R;
import com.example.riane.hanbaoreader_app.app.BaseActivity;
import com.example.riane.hanbaoreader_app.ui.fragment.BookCaseFragment;
import com.example.riane.hanbaoreader_app.ui.fragment.BookStoreFragment;
import com.example.riane.hanbaoreader_app.widget.MyTitleView;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @Bind(R.id.titltebar)
    MyTitleView titleView;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.nv_menu)
    NavigationView mNavigationView;
//    @Bind(R.id.tab_layout)
//    SmartTabLayout mSmartTabLayout;

    private Fragment mContent;
    private BookCaseFragment mBookCaseFragment= new BookCaseFragment();
    private BookStoreFragment mBookStoreFragment = new BookStoreFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        ButterKnife.bind(this);
        switchcontent(mBookStoreFragment, mBookCaseFragment);
        initData();
        initView();

    }

    private void initData() {

    }

    public void initView(){
        titleView.setFirsttabListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mSmartTabLayout.setVisibility(View.GONE);
                switchcontent(mBookStoreFragment, mBookCaseFragment);
            }
        });

        titleView.setSecondtabListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // mSmartTabLayout.setVisibility(View.VISIBLE);
                switchcontent(mBookCaseFragment, mBookStoreFragment);
            }
        });

        titleView.setBtn_hamburger(
                new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                //menuItem.setChecked(true);
                switch(menuItem.getItemId()){
                    case R.id.nav_menu_setting:
                        Intent settingIntent = new Intent(MainActivity.this, SettingsActivity.class);
                        settingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(settingIntent);
                        break;
                    case R.id.nav_menu_placebook:
                        Intent placeBookIntent = new Intent(MainActivity.this, PlaceBookActivity.class);
                        placeBookIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(placeBookIntent);
                        break;
                    default:
                        break;
                }


                mDrawerLayout.closeDrawers();
                return false;
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                                        .setAction("Action", null).show();
                startImprotActivity();
            }
        });
    }

    //切换Fragment,不会重新加载
    public void switchcontent(Fragment from, Fragment to){
        if (mContent != to){
            mContent = to;
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().setCustomAnimations(
                    R.anim.slide_in, R.anim.slide_out);
            if(!to.isAdded()){
                transaction.hide(from).add(R.id.framelayout,to).commit();
            } else {
                transaction.hide(from).show(to).commit();
            }
        }
    }

    public void startImprotActivity(){
        Intent intent = new Intent(MainActivity.this,ImportBookActivity.class);
        startActivity(intent);
    }

    public static Intent getCallingIntent(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }

}
