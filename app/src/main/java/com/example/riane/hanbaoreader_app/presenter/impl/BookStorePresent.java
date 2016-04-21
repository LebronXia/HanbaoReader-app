package com.example.riane.hanbaoreader_app.presenter.impl;

import com.example.riane.hanbaoreader_app.model.BookStoreModel;
import com.example.riane.hanbaoreader_app.modle.entity.BookVO;
import com.example.riane.hanbaoreader_app.modle.entity.UserVO;
import com.example.riane.hanbaoreader_app.presenter.IBookStorePresenter;
import com.example.riane.hanbaoreader_app.presenter.Presenter;
import com.example.riane.hanbaoreader_app.view.impl.BookStoreView;

import java.util.List;

/**
 * Created by Riane on 2016/4/15.
 *
 * View和Model的桥梁，它从Model层检索数据后，返回给VIew
 */
public class BookStorePresent implements Presenter<BookStoreView>,IBookStorePresenter{
    private BookStoreView bookStoreView;
    private BookStoreModel mBookStoreModel;

    public BookStorePresent(BookStoreView bookStoreView) {
        attachView(bookStoreView);
        this.mBookStoreModel = new BookStoreModel(this);
    }


    @Override
    public void attachView(BookStoreView view) {
        this.bookStoreView = view;
    }

    @Override
    public void detachView() {
        this.bookStoreView = null;
    }

    public void loadData(String tag){
        bookStoreView.showProgress();
        mBookStoreModel.loadData(tag);
    }

    @Override
    public void loadDataSuccess(List<BookVO> bookVOs) {
        bookStoreView.showData(bookVOs);
        bookStoreView.hideProgress();
        bookStoreView.hideLoadEmpty();
    }

    @Override
    public void loadDataFailure() {
        bookStoreView.hideProgress();
        bookStoreView.showLoadEmpty();
    }
}
