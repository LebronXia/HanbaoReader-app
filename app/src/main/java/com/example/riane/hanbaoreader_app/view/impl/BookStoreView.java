package com.example.riane.hanbaoreader_app.view.impl;

import com.example.riane.hanbaoreader_app.modle.Book;
import com.example.riane.hanbaoreader_app.modle.entity.BookVO;
import com.example.riane.hanbaoreader_app.modle.entity.UserVO;
import com.example.riane.hanbaoreader_app.view.View;

import java.util.List;

/**
 * Created by Riane on 2016/4/15.
 */
public interface BookStoreView extends View{
    void showData(List<BookVO> bookVOs);

    void showLoadEmpty();

    void hideLoadEmpty();

    void showProgress();

    void hideProgress();
}
