package com.example.riane.hanbaoreader_app.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.riane.hanbaoreader_app.R;
import com.example.riane.hanbaoreader_app.app.BaseActivity;
import com.example.riane.hanbaoreader_app.modle.Book;
import com.example.riane.hanbaoreader_app.util.BookPageFactory;
import com.example.riane.hanbaoreader_app.widget.BookPageView;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.IOException;
import java.io.Serializable;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Xiamu on 2016/4/5.
 */
public class ReadBookActivity extends BaseActivity implements View.OnClickListener {

    private static final String EXTRA_READ_BOOK = "com.riane.extra_read_book";
    private static final String TAG = "Read2";
    private PopupWindow pop_bottommenu, pop_toolmenu, pop_text;
    private View toolpop, bottompop, textpop;
    private ImageView iv_bookmenu, iv_booksize, iv_booklight, iv_bookmark, iv_bookhome, iv_bookmore;
    @Bind(R.id.btn_bigtext)
    Button btn_bigtext;
    @Bind(R.id.btn_smalltext)
    Button btn_smalltext;
    @Bind(R.id.tv_textsize)
    TextView tv_textsize;

    private BookPageView mBookPageView;
    private Canvas mCurPageCanvas, mNextPageCanvas;  //画布
    private Bitmap mCurPageBitmap, mNextPageBitmap;  //图像

    private BookPageFactory mBookPageFactory;
    private int readHeight;//电子书显示高度
    private int screenHeight;
    private int screenWidth;
    private long fileLenth = 1L;
    private Book book;
    private int begin = 0; //记录书籍开始位置
    private String word = "";//记录当前页面的文字
    private String bookPath;
    private int defaultSize = 0;  //默认字体大小
    private int size;
    private boolean isBottomAndTopMenuShow = false; //是处于底部吗
    private Toast toast = null;

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Android获取屏幕的宽度和高度
        Display display = getWindowManager().getDefaultDisplay();
        screenWidth = display.getWidth();
        screenHeight = display.getHeight();
        setContentView(R.layout.activity_readbook);
        defaultSize = (screenWidth * 20) / 320;
        readHeight = screenHeight - (13 * screenHeight) / 320;
        initData();
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
        bookPath = book.getFilePath();
        try {
            fileLenth = mBookPageFactory.openBook(book);
            mBookPageFactory.setM_fontSize(size);
            //绘制进度百分比
            mBookPageFactory.onDraw(mCurPageCanvas);
        } catch (IOException e) {
            Log.e(TAG, "打开电子书失败", e);
            Toast.makeText(this, "打开电子书失败", Toast.LENGTH_SHORT).show();
        }

        mBookPageView.setBitmaps(mCurPageBitmap, mCurPageBitmap);

        //设置自定义View的触屏事件
        mBookPageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean ret = false;
                if (v == mBookPageView) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {   //屏幕按下
                        int screenWidth = getResources().getDisplayMetrics().widthPixels;
                        int x = (int) event.getX();
                        if (screenWidth * 1 / 3 < x && x < screenWidth * 2 / 3) {
                            //Toast.makeText(ReadBookActivity.this, "你按到中间拉", Toast.LENGTH_SHORT).show();
                            if (pop_bottommenu.isShowing()) {
                                popDismiss();
                            } else {
                                pop_bottommenu.showAtLocation(mBookPageView, Gravity.BOTTOM, 0, 0);
                                pop_toolmenu.showAtLocation(mBookPageView, Gravity.TOP, 0, 0);
                            }
                        } else {
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
                                    begin = mBookPageFactory.getM_mbBufBegin();//获取当前阅读位置
                                    word = mBookPageFactory.getFirstLineText();//获取当前阅读位置的首行
                                } catch (IOException e1) {
                                    // TODO Auto-generated catch block
                                    e1.printStackTrace();
                                }
                                if (mBookPageFactory.isfirstPage())
                                    return false;
                                mBookPageFactory.onDraw(mNextPageCanvas);
                            } else {
                                try {
                                    //false，显示下一页
                                    mBookPageFactory.nextPage();
                                    begin = mBookPageFactory.getM_mbBufBegin();//获取当前阅读位置
                                    word = mBookPageFactory.getFirstLineText();
                                } catch (IOException e1) {

                                    e1.printStackTrace();
                                }
                                if (mBookPageFactory.islastPage()){
                                    Toast.makeText(ReadBookActivity.this, "已经是最后一页了", Toast.LENGTH_SHORT).show();
                                    return false;
                                }

                                mBookPageFactory.onDraw(mNextPageCanvas);
                            }
                            mBookPageView.setBitmaps(mCurPageBitmap, mNextPageBitmap);
                        }

                    }
                    editor.putInt(bookPath + "begin",begin).commit();
                    ret = mBookPageView.doTouchEvent(event);
                    return ret;
                }
                return false;
            }
        });
        initPopupWindow();


    }

    private void initData() {
        //提取记录子啊sharePreferences的各种状态
        sp = getSharedPreferences("config", MODE_PRIVATE);
        editor = sp.edit();
        //读取配置文件中字体大小
        size = sp.getInt("size",defaultSize);
    }


    //初始化popupWindow
    private void initPopupWindow() {
        toolpop = this.getLayoutInflater().inflate(R.layout.pop_toolmenu, null);
        pop_toolmenu = new PopupWindow(toolpop, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置动画
        pop_toolmenu.setAnimationStyle(R.style.anim_menu_toolbar);

        //底部弹出框
        bottompop = this.getLayoutInflater().inflate(R.layout.pop_bottommenu, null);
        pop_bottommenu = new PopupWindow(bottompop, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        pop_bottommenu.setAnimationStyle(R.style.anim_menu_bottombar);

        //字体弹出框
        textpop = this.getLayoutInflater().inflate(R.layout.pop_text, null);
        pop_text = new PopupWindow(textpop, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        pop_text.setAnimationStyle(R.style.anim_menu_bottombar);
        //设置这两个方法实现点击外面取消弹出框
        pop_text.setFocusable(true); // 这个很重要
        pop_text.setBackgroundDrawable(new ColorDrawable(0x00000000));
        ButterKnife.bind(this,textpop);

        iv_bookhome = (ImageView) toolpop.findViewById(R.id.iv_bookhome);
        iv_bookmore = (ImageView) toolpop.findViewById(R.id.iv_bookmore);
        iv_bookmenu = (ImageView) bottompop.findViewById(R.id.iv_bookmenu);
        iv_booksize = (ImageView) bottompop.findViewById(R.id.iv_booksize);
        iv_booklight = (ImageView) bottompop.findViewById(R.id.iv_booklight);
        iv_bookmark = (ImageView) bottompop.findViewById(R.id.iv_bookmark);


        iv_bookhome.setOnClickListener(this);
        iv_bookmore.setOnClickListener(this);
        iv_bookmenu.setOnClickListener(this);
        iv_booksize.setOnClickListener(this);
        iv_booklight.setOnClickListener(this);
        iv_bookmark.setOnClickListener(this);
    }

    //记录配置文件中字体大小
    public void setSize() {

    }

    //隐藏popupWindow
    public void popDismiss() {
        pop_toolmenu.dismiss();
        pop_bottommenu.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //字体按钮
            case R.id.iv_booksize:
                popDismiss();
                pop_text.showAtLocation(mBookPageView, Gravity.BOTTOM, 0, 0);
                break;
        }
    }

    @OnClick(R.id.btn_smalltext)
    public void onsmalltextbtn(){
        size -= 5;
        if (size < 10){
            showTextToast("已到达最小字号");
            size = 10;
        }
        tv_textsize.setText(size + "");
        editor.putInt("size", size);
        editor.commit();
        mBookPageFactory.setM_fontSize(size);
        mBookPageFactory.setM_mbBufBegin(begin);
        mBookPageFactory.setM_mbBufEnd(begin);
        postInvalidateUI();
    }

    private void postInvalidateUI() {
        mBookPageView.abortAnimation();
        mBookPageFactory.onDraw(mCurPageCanvas);
        try {
            mBookPageFactory.currentPage();
            begin = mBookPageFactory.getM_mbBufBegin();// 获取当前阅读位置
            word = mBookPageFactory.getFirstLineText();// 获取当前阅读位置的首行文字
        } catch (IOException e1) {
            Log.e(TAG, "postInvalidateUI->IOException error", e1);
        }
        mBookPageFactory.onDraw(mNextPageCanvas);

        mBookPageView.setBitmaps(mCurPageBitmap, mNextPageBitmap);
        mBookPageView.postInvalidate();
    }

    @OnClick(R.id.btn_bigtext)
    public void onBigTextBtn(){
        size += 5;
        if (size > 50){
            showTextToast("已到达最大字号");
            size = 50;
        }
        tv_textsize.setText(size + "");
        editor.putInt("size",size);
        editor.commit();
        mBookPageFactory.setM_fontSize(size);
        mBookPageFactory.setM_mbBufBegin(begin);
        mBookPageFactory.setM_mbBufEnd(begin);
        postInvalidateUI();
    }

    //点击显示导航栏
//    private void setNavigate() {
//        if (!isBottomAndTopMenuShow) {
//            isBottomAndTopMenuShow = true;
//            ll_bottommenu.setVisibility(View.VISIBLE);
//            rl_topMenu.setVisibility(View.VISIBLE);
//            ll_bottommenu.startAnimation(AnimationUtils.loadAnimation(this, R.anim.layout_enter));
//            rl_topMenu.startAnimation(AnimationUtils.loadAnimation(this, R.anim.layout_enter));
//            ll_bottommenu.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    return true;
//                }
//            });
//        } else {
//            isBottomAndTopMenuShow = false;
//            ll_bottommenu.setVisibility(View.GONE);
//            rl_topMenu.setVisibility(View.GONE);
//            ll_bottommenu.startAnimation(AnimationUtils.loadAnimation(this, R.anim.layout_exit));
//            rl_topMenu.startAnimation(AnimationUtils.loadAnimation(this, R.anim.layout_exit));
//            ll_bottommenu.setFocusable(false);
//        }
//    }

    //    @OnClick(R.id.iv_booksize)
//    public void onBooksizeClick() {
//        Toast.makeText(ReadBookActivity.this, "调节字体", Toast.LENGTH_SHORT).show();
//        ll_bottommenu.setVisibility(View.GONE);
//        rl_topMenu.setVisibility(View.GONE);
//        initPopupWindow();
//
//    }
    public void backPress() {
        startActivity(MainActivity.getCallingIntent(this));
        ReadBookActivity.this.finish();
    }

    public static Intent getCallingIntent(Context context, Book book) {
        Intent intent = new Intent(context, ReadBookActivity.class);
        intent.putExtra(EXTRA_READ_BOOK, (Serializable) book);
        return intent;
    }

    //展示Toast
    private void showTextToast(String msg){
        if (toast == null){
            toast = Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
