package com.example.riane.hanbaoreader_app.presenter.impl;

import com.example.riane.hanbaoreader_app.model.BookStoreModel;
import com.example.riane.hanbaoreader_app.modle.entity.BookInfoVO;
import com.example.riane.hanbaoreader_app.presenter.IBookInfoPresent;
import com.example.riane.hanbaoreader_app.presenter.Presenter;
import com.example.riane.hanbaoreader_app.view.impl.BookInfoView;

/**
 * Created by Riane on 2016/5/8.
 */
public class BookInfoPresent implements Presenter<BookInfoView>, IBookInfoPresent {

    private BookInfoView mBookInfoView;
    private BookStoreModel mBookStoreModel;

    public BookInfoPresent(BookInfoView bookInfoView){
        attachView(bookInfoView);
        mBookStoreModel = new BookStoreModel(this);

    }

    public void loadData(String isbn){
        mBookInfoView.showProgress();
        mBookStoreModel.loadBookInfo(isbn);
    }

    @Override
    public void loadDataSuccess(BookInfoVO bookInfoVO) {
        mBookInfoView.showData(bookInfoVO);
        mBookInfoView.hideProgress();
        mBookInfoView.hideLoadEmpty();
    }

    @Override
    public void loadDataFailure() {
        mBookInfoView.hideProgress();
        mBookInfoView.showLoadEmpty();
    }

    @Override
    public void attachView(BookInfoView view) {
        this.mBookInfoView = view;
    }

    @Override
    public void detachView() {
        this.mBookInfoView = null;
    }
}
