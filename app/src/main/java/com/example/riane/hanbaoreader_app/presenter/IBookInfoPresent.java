package com.example.riane.hanbaoreader_app.presenter;

import com.example.riane.hanbaoreader_app.modle.entity.BookInfoVO;
import com.example.riane.hanbaoreader_app.modle.entity.BookVO;

import java.util.List;

/**
 * Created by Riane on 2016/5/8.
 */
public interface IBookInfoPresent {

    void loadDataSuccess(BookInfoVO bookInfoVO);

    void loadDataFailure();


}
