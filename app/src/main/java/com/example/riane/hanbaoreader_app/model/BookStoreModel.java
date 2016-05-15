package com.example.riane.hanbaoreader_app.model;

import android.widget.Toast;

import com.example.riane.hanbaoreader_app.config.Constant;
import com.example.riane.hanbaoreader_app.modle.Book;
import com.example.riane.hanbaoreader_app.modle.entity.BookInfoVO;
import com.example.riane.hanbaoreader_app.modle.entity.BookVO;
import com.example.riane.hanbaoreader_app.modle.entity.HttpResult;
import com.example.riane.hanbaoreader_app.modle.entity.UserVO;
import com.example.riane.hanbaoreader_app.network.BookApi;
import com.example.riane.hanbaoreader_app.presenter.IBookInfoPresent;
import com.example.riane.hanbaoreader_app.presenter.IBookStorePresenter;
import com.example.riane.hanbaoreader_app.presenter.Presenter;
import com.example.riane.hanbaoreader_app.presenter.impl.BookStorePresent;
import com.example.riane.hanbaoreader_app.util.LogUtils;


import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Riane on 2016/4/15.
 *
 * 业务具体处理，包括负责存储、检索、操纵数据
 */
public class BookStoreModel {
    IBookStorePresenter mIBookStorePresent;
    IBookInfoPresent mIBookInfoPresent;
    private static final int DEAULT_TIMEOUT = 10;
    private Retrofit retrofit;

    public BookStoreModel(Object object) {
        if (object instanceof IBookStorePresenter){
            this.mIBookStorePresent = (IBookStorePresenter) object;
        } else if (object instanceof IBookInfoPresent){
            this.mIBookInfoPresent = (IBookInfoPresent) object;
        }

    }

    public void init(){
        //手建一个OKHttpClient并设置超时时间
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEAULT_TIMEOUT, TimeUnit.SECONDS);

        retrofit = new Retrofit.Builder()
                .client(builder. build())
                .baseUrl(Constant.BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    public void loadData(String tag){
        init();
        BookApi bookApi = retrofit.create(BookApi.class);

        bookApi.getUser(tag)
                .map(new HttpResultFunc<List<BookVO>>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<BookVO>>() {
                    @Override
                    public void onCompleted() {
                        LogUtils.d("到达");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.d("下载出错error:" + e.getMessage());
                        mIBookStorePresent.loadDataFailure();
                    }

                    @Override
                    public void onNext(List<BookVO> bookVOs) {
                        if (bookVOs != null || bookVOs.size() > 1){
                            LogUtils.d("下载中"+ bookVOs.get(0).getName());
                            mIBookStorePresent.loadDataSuccess(bookVOs);
                        }
                    }
                });
    }

    public void loadBookInfo(String isbn){

        init();
        BookApi bookApi = retrofit.create(BookApi.class);
        bookApi.getBookInfo(isbn)
                .map(new HttpResultFunc<BookInfoVO>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BookInfoVO>() {
                    @Override
                    public void onCompleted() {
                        LogUtils.d("到达");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.d("下载出错error:" + e.getMessage());
                        //mIBookStorePresent.loadDataFailure();
                        mIBookInfoPresent.loadDataFailure();
                    }

                    @Override
                    public void onNext(BookInfoVO bookInfoVO) {
//                        if (bookVOs != null || bookVOs.size() > 1) {
//                            LogUtils.d("下载中" + bookVOs.get(0).getName());
//                            mIBookStorePresent.loadDataSuccess(bookVOs);
//                        }
                        if (bookInfoVO != null){
                            LogUtils.d(bookInfoVO.getAuthor());
                            mIBookInfoPresent.loadDataSuccess(bookInfoVO);
                        }
                    }
                });
    }

    //用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
    private class HttpResultFunc<T> implements Func1<HttpResult<T> ,T>{
        @Override
        public T call(HttpResult<T> httpResult) {
            if (httpResult.getResultCode() != 0){
                throw new RuntimeException();
            }
            return httpResult.getObject();
        }
    }
}

