package com.example.riane.hanbaoreader_app.view.impl;

import com.example.riane.hanbaoreader_app.modle.entity.BookInfoVO;
import com.example.riane.hanbaoreader_app.modle.entity.BookVO;

import java.util.List;

/**
 * Created by Riane on 2016/5/8.
 */
public interface BookInfoView {

    void showData(BookInfoVO bookInfoVO);

    void showLoadEmpty();

    void hideLoadEmpty();

    void showProgress();

    void hideProgress();

}
