package com.example.riane.hanbaoreader_app.ui.activity;

import android.os.Bundle;

import com.example.riane.hanbaoreader_app.R;
import com.example.riane.hanbaoreader_app.app.BaseActivity;
import com.example.riane.hanbaoreader_app.ui.fragment.SettingFragment;
import com.example.riane.hanbaoreader_app.ui.fragment.SplashFragment;

/**
 * Created by Riane on 2016/4/29.
 */
public class SplashActivity extends BaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        SplashFragment splashFragment = new SplashFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_setting,splashFragment).commit();
    }
}
