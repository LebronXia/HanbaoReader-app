package com.example.riane.hanbaoreader_app.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.riane.hanbaoreader_app.R;
import com.example.riane.hanbaoreader_app.app.BaseFragment;
import com.example.riane.hanbaoreader_app.config.Constant;
import com.example.riane.hanbaoreader_app.modle.entity.BookInfoVO;
import com.example.riane.hanbaoreader_app.modle.entity.BookVO;
import com.example.riane.hanbaoreader_app.presenter.impl.BookInfoPresent;
import com.example.riane.hanbaoreader_app.util.LogUtils;
import com.example.riane.hanbaoreader_app.view.impl.BookInfoView;
import com.example.riane.hanbaoreader_app.widget.RippleView;

import java.io.Serializable;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Riane on 2016/5/7.
 */
public class BookViewFragment extends BaseFragment implements BookInfoView{
    //图书图片
    @Bind(R.id.iv_book_icon)
    ImageView mIvBookIcon;
    @Bind(R.id.ll_book_icon)
    LinearLayout mLlBookIcon;
    //图书作者
    @Bind(R.id.tv_book_author)
    TextView mTvBookAuthor;
    //图书出版时间
    @Bind(R.id.tv_book_time)
    TextView mTvBookTime;
    //图书出版社
    @Bind(R.id.tv_book_publicer)
    TextView mTvBookPublicer;
    //图书isbn号
    @Bind(R.id.tv_book_isbn)
    TextView mTvBookIsbn;
    //图书的标签
    @Bind(R.id.tv_book_tag)
    TextView mTvBookTag;
    //开始下载按钮
    @Bind(R.id.rl_review)
    RippleView mRlReview;
    //本书简介内容
    @Bind(R.id.tv_book_intro_content)
    TextView mTvBookIntroContent;

    @Bind(R.id.ll_book_intro)
    LinearLayout mLlBookIntro;
    @Bind(R.id.tv_book_mulu_content)
    //本书目录
    TextView mTvBookMuluContent;
    @Bind(R.id.ll_book_mulu)
    LinearLayout mLlBookMulu;
    @Bind(R.id.progress_view)
    ContentLoadingProgressBar mProgressView;
    @Bind(R.id.container)
    FrameLayout mContainer;
    @Bind(R.id.tv_load_empty)
    TextView mTvLoadEmpty;
    @Bind(R.id.tv_load_error)
    TextView mTvLoadError;
    private View view;
    private BookVO mBookVO;
    private BookInfoPresent mBookInfoPresent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_bookview, container, false);
        mBookVO = (BookVO) getArguments().getSerializable(Constant.BUNDLE_BOOK_VIEW);
        LogUtils.d("Isbn:" + mBookVO.getIsbn());
        ButterKnife.bind(this, view);
        initView(mBookVO.getIsbn());
        return view;
    }

    public static BookViewFragment newInstance(BookVO bookVO) {
        //通过Bundle保存数据
        Bundle args = new Bundle();
        //args.putString(Constant.BUNDLE_PLACE, bookPath);
        //args.putStringArrayList(Constant.BUNDLE_BOOK_VIEW, bookVO);
        args.putSerializable(Constant.BUNDLE_BOOK_VIEW, (Serializable) bookVO);
        BookViewFragment fragment = new BookViewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void initView(String isbn){
        mBookInfoPresent = new BookInfoPresent(this);
        mBookInfoPresent.loadData(isbn);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void showData(BookInfoVO bookInfoVO) {
        LogUtils.d("开始下载bookInfo");
        if (bookInfoVO != null){
            LogUtils.d(bookInfoVO.getPicture() + bookInfoVO.getAuthor());
            Glide.with(getActivity()).load(Constant.BASEURL + bookInfoVO.getPicture())
                    .placeholder(R.mipmap.cover_txt)
                    .crossFade()
                    .into(mIvBookIcon);
            mTvBookAuthor.setText(bookInfoVO.getAuthor());
            mTvBookTime.setText("2013-01");
            mTvBookIsbn.setText(bookInfoVO.getIsbn());
            mTvBookPublicer.setText(bookInfoVO.getPublish());

            if (bookInfoVO.getBookDescription() == null)
                mLlBookIntro.setVisibility(View.GONE);
            else
                mTvBookIntroContent.setText(bookInfoVO.getBookDescription());

            if (bookInfoVO.getBookList() == null)
                mLlBookMulu.setVisibility(View.GONE);
            else
                mTvBookMuluContent.setText(bookInfoVO.getBookList());
            StringBuilder stringBuilder = new StringBuilder();
            for (String tag : bookInfoVO.getTags()){
                stringBuilder.append(tag + "、");
            }
            mTvBookTag.setText(stringBuilder);
        }
    }

    @Override
    public void showLoadEmpty() {
        mTvLoadEmpty.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadEmpty() {
        mTvLoadEmpty.setVisibility(View.GONE);
    }

    @Override
    public void showProgress() {
        mProgressView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mProgressView.setVisibility(View.GONE);
    }
}
