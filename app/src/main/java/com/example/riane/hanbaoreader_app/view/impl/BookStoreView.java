package com.example.riane.hanbaoreader_app.view.impl;

import com.example.riane.hanbaoreader_app.modle.entity.BookVO;
import com.example.riane.hanbaoreader_app.view.View;

/**
 * Created by Riane on 2016/4/15.
 */
public interface BookStoreView extends View{
    void showData(BookVO bookVO);

    void showProgress();

    void hideProgress();
}
