package com.example.riane.hanbaoreader_app.app;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by Xiamu on 2016/3/25.
 */
public class MyApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
