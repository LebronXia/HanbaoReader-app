package com.example.riane.hanbaoreader_app.network;

import com.example.riane.hanbaoreader_app.config.Constant;
import com.example.riane.hanbaoreader_app.modle.entity.BookVO;
import com.example.riane.hanbaoreader_app.modle.entity.HttpResult;
import com.example.riane.hanbaoreader_app.modle.entity.UserVO;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Riane on 2016/4/15.
 */
public interface BookApi {

//    @GET(URL_ALLBOOK)
//    Observable<BookVO> getBook();

    @GET(Constant.URL_USER)
    Observable<HttpResult<UserVO>> getUser(@Path("name") String name);
//    @GET(Constant.URL_USER)
//    Call<>
}
