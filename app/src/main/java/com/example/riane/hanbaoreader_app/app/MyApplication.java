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
        initStetho();
    }

    private void initStetho() {
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(
                                Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(
                                Stetho.defaultInspectorModulesProvider(this))
                        .build());
    }
}
