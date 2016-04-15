package com.example.riane.hanbaoreader_app.presenter;

import com.example.riane.hanbaoreader_app.modle.entity.BookVO;

/**
 * Created by Riane on 2016/4/15.
 * 此接口作用连接Mode
 */
public interface IBookStorePresenter {
    void loadDataSuccess(BookVO bookVO);

    void loadDataFailure();
}
