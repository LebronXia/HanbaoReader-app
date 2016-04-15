package com.example.riane.hanbaoreader_app.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.riane.hanbaoreader_app.R;
import com.example.riane.hanbaoreader_app.app.BaseActivity;
import com.example.riane.hanbaoreader_app.ui.fragment.SettingFragment;

/**
 * Created by Riane on 2016/4/14.
 */
public class SettingsActivity extends BaseActivity{
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        toolbar = (Toolbar) findViewById(R.id.toolbar_setting);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("设置");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        SettingFragment settingFragment = SettingFragment.newInstance();
        getFragmentManager().beginTransaction().replace(R.id.framelayout_setting,settingFragment).commit();
    }
}
