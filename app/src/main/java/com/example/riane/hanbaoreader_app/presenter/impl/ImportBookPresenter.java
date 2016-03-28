package com.example.riane.hanbaoreader_app.presenter.impl;

import android.view.View;

import com.example.riane.hanbaoreader_app.presenter.Presenter;
import com.example.riane.hanbaoreader_app.view.impl.ImportBookView;

/**
 * Created by Xiamu on 2016/3/26.
 */
public class ImportBookPresenter implements Presenter{
    private ImportBookView mImportBookView;

    @Override
    public void attachView(View view) {
        this.mImportBookView = (ImportBookView)view;
    }

    public void initRecyclerView(){
        mImportBookView.initRecyclerView();
    }

    public void initToolBar(){
        mImportBookView.initToolbar();
    }

    public void loadData(){
        mImportBookView.loadData();
    }
}
