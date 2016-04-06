package com.example.riane.hanbaoreader_app.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.riane.hanbaoreader_app.R;
import com.example.riane.hanbaoreader_app.app.BaseActivity;
import com.example.riane.hanbaoreader_app.modle.Book;
import com.example.riane.hanbaoreader_app.util.BookPageFactory;
import com.example.riane.hanbaoreader_app.widget.BookPageView;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by Xiamu on 2016/4/5.
 */
public class ReadBookActivity extends BaseActivity{

    private static final String EXTRA_READ_BOOK = "com.riane.extra_read_book";
    private static final String TAG = "Read2";
    private BookPageView mBookPageView;
    private Canvas mCurPageCanvas, mNextPageCanvas;  //画布
    private Bitmap mCurPageBitmap, mNextPageBitmap;  //图像

    private BookPageFactory mBookPageFactory;
    int readHeight;//电子书显示高度
    int screenHeight;
    int screenWidth;
    long fileLenth = 1L;
    Book book;
    int defaultSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);

        //Android获取屏幕的宽度和高度
        Display display = getWindowManager().getDefaultDisplay();
        screenWidth = display.getWidth();
        screenHeight = display.getHeight();
        setContentView(R.layout.activity_readbook);
        defaultSize = (screenWidth * 20) / 320;
        readHeight = screenHeight - (50 * screenHeight) / 320;
        // 实例化自定义View
        mBookPageView = new BookPageView(this, screenWidth, readHeight);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.rl_readview);
        relativeLayout.addView(mBookPageView);
        // 创建当前的图片
        mCurPageBitmap = Bitmap.createBitmap(screenWidth, readHeight, Bitmap.Config.ARGB_8888);
        // 创建下一页的图片
        mNextPageBitmap = Bitmap.createBitmap(screenWidth, readHeight, Bitmap.Config.ARGB_8888);
        // 转化成画布类
        mCurPageCanvas = new Canvas(mCurPageBitmap);
        mNextPageCanvas = new Canvas(mNextPageBitmap);
        // 实例化工具类
        mBookPageFactory = new BookPageFactory(this, screenWidth, readHeight);
        book = (Book) getIntent().getSerializableExtra(EXTRA_READ_BOOK);
           try{
            fileLenth = mBookPageFactory.openBook(book);
               mBookPageFactory.setM_fontSize(defaultSize);
            //绘制进度百分比
            mBookPageFactory.onDraw(mCurPageCanvas);
        } catch (IOException e) {
            Log.e(TAG, "打开电子书失败", e);
            Toast.makeText(this, "打开电子书失败", Toast.LENGTH_SHORT).show();
        }

        mBookPageView.setBitmaps(mCurPageBitmap,mCurPageBitmap);

        //设置自定义View的触屏事件
        mBookPageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean ret = false;
                if (v == mBookPageView){
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {   //屏幕按下
                        // 设置动画效果
                        mBookPageView.abortAnimation();
                        // 修改点击的坐标，从而判断是上一页还是下一页
                        mBookPageView.calcCornerXY(event.getX(), event.getY());

                        mBookPageFactory.onDraw(mCurPageCanvas);
                        if (mBookPageView.DragToRight()) {
                            //是否从左边翻向右边
                            try {
                                //true，显示上一页
                                mBookPageFactory.prePage();
                            } catch (IOException e1) {
                                // TODO Auto-generated catch block
                                e1.printStackTrace();
                            }
                            if(mBookPageFactory.isfirstPage())
                                return false;
                            mBookPageFactory.onDraw(mNextPageCanvas);
                        } else {
                            try {
                                //false，显示下一页
                                mBookPageFactory.nextPage();
                            } catch (IOException e1) {
                                // TODO Auto-generated catch block
                                e1.printStackTrace();
                            }
                            if(mBookPageFactory.islastPage())
                                return false;
                            mBookPageFactory.onDraw(mNextPageCanvas);
                        }
                        mBookPageView.setBitmaps(mCurPageBitmap, mNextPageBitmap);
                    }
                    ret = mBookPageView.doTouchEvent(event);
                    return ret;
                }
                return false;
            }
        });

    }

    public static Intent getCallingIntent(Context context, Book book){
        Intent intent = new Intent(context, ReadBookActivity.class);
        intent.putExtra(EXTRA_READ_BOOK, (Serializable)book);
        return intent;
    }
}
