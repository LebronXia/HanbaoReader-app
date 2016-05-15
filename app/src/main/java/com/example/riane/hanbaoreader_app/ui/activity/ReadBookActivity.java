package com.example.riane.hanbaoreader_app.ui.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.AlteredCharSequence;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.riane.hanbaoreader_app.R;
import com.example.riane.hanbaoreader_app.app.BaseActivity;
import com.example.riane.hanbaoreader_app.cache.BookDao;
import com.example.riane.hanbaoreader_app.cache.BookMarkDao;
import com.example.riane.hanbaoreader_app.modle.Book;
import com.example.riane.hanbaoreader_app.modle.BookMark;
import com.example.riane.hanbaoreader_app.util.BookPageFactory;
import com.example.riane.hanbaoreader_app.util.LogUtils;
import com.example.riane.hanbaoreader_app.util.ToastUtils;
import com.example.riane.hanbaoreader_app.widget.BookPageView;
import com.example.riane.hanbaoreader_app.widget.ToPopwindow;


import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Xiamu on 2016/4/5.
 */
public class ReadBookActivity extends BaseActivity implements View.OnClickListener,SeekBar.OnSeekBarChangeListener {

    private static final String EXTRA_READ_BOOK = "com.riane.extra_read_book";
    private final static int DIALOG_BOOKLOAD = 2;
    private static final String TAG = "Read2";
    private ToPopwindow morePopWindow;
    private PopupWindow pop_bottommenu, pop_toolmenu, pop_text, pop_light;
    private View toolpop, bottompop, textpop, lightpop, morepop;
    private ImageView iv_bookmenu, iv_booksize, iv_booklight, iv_bookmark, iv_bookhome,bookmore;
    private Button btn_light, btn_black;
    private LinearLayout to_layout;
    private SeekBar lightSeekBar;

    private TextView dialog_tv_progress;
    private SeekBar dialog_seekbar;
    private Button btn_ok,btn_cancel;
    private ProgressDialog progressDialog;

    private Dialog dialog;
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
    private int dialog_progess;
    private Book book;
    private int begin = 0; //记录书籍开始位置
    private String word = "";//记录当前页面的文字
    private String bookPath;
    private int defaultSize = 0;  //默认字体大小
    private int m_backColor = Color.rgb(199, 237, 204);
    private int m_backColor_black = Color.rgb(0,0,0);
    private int size;
    private int light;
    private boolean isBottomAndTopMenuShow = false; //是处于底部吗
    private Toast toast = null;
    private BookMarkDao bookMarkDao;

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private WindowManager.LayoutParams lp;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            switch(msg.what){
                case DIALOG_BOOKLOAD:
                    progressDialog.dismiss();
                    break;

            }
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Android获取屏幕的宽度和高度
        Display display = getWindowManager().getDefaultDisplay();
        screenWidth = display.getWidth();
        screenHeight = display.getHeight();
        //getWindow().setFlags(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.FILL_PARENT);  //设置全屏
        setContentView(R.layout.activity_readbook);
        bookMarkDao = new BookMarkDao(ReadBookActivity.this);
        defaultSize = (screenWidth * 20) / 320;
        initData();
        LogUtils.d("defaultSize +" + defaultSize + "screenwidth:" +screenWidth);
        readHeight = screenHeight ;
        lp = getWindow().getAttributes();
        lp.screenBrightness = light / 10.0f < 0.01f ? 0.01f : light / 10.0f;
        getWindow().setAttributes(lp);
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

        progressDialog = ProgressDialog.show(ReadBookActivity.this, "温馨提示", "正在加载图书", true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    fileLenth = mBookPageFactory.openBook(book);
                    mBookPageFactory.setM_fontSize(size);
                    //将文字绘制于手机屏幕
                    mBookPageFactory.onDraw(mCurPageCanvas);
                } catch (IOException e) {
                    Log.e(TAG, "打开电子书失败", e);
                    Toast.makeText(ReadBookActivity.this, "打开电子书失败", Toast.LENGTH_SHORT).show();
                }

                Message message = new Message();
                message.what = DIALOG_BOOKLOAD;
                mHandler.sendMessage(message);
            }
        }).start();


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
                            // 停止动画
                            mBookPageView.abortAnimation();
                            // 计算拖拽点对应的拖拽角
                            mBookPageView.calcCornerXY(event.getX(), event.getY());

                            mBookPageFactory.onDraw(mCurPageCanvas);
                            if (mBookPageView.DragToRight()) {
                                //是否从左边翻向右边
                                try {
                                    //true，显示上一页
                                    mBookPageFactory.prePage();
                                    begin = mBookPageFactory.getM_mbBufBegin();//获取当前阅读位置
                                    word = mBookPageFactory.getFirstLineText();//获取当前阅读位置的首行
                                    LogUtils.d("当前阅读位置的首行" + word);
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }
                                if (mBookPageFactory.isfirstPage()){
                                    ToastUtils.showShort(ReadBookActivity.this,"当前是第一页");
                                    return false;
                                }

                                mBookPageFactory.onDraw(mNextPageCanvas);
                            } else {
                                //右翻
                                try {
                                    //false，显示下一页
                                    mBookPageFactory.nextPage();
                                    begin = mBookPageFactory.getM_mbBufBegin();//获取当前阅读位置
                                    word = mBookPageFactory.getFirstLineText();
                                } catch (IOException e1) {

                                    e1.printStackTrace();
                                }
                                if (mBookPageFactory.islastPage()) {
                                    //Toast.makeText(ReadBookActivity.this, "已经是最后一页了", Toast.LENGTH_SHORT).show();
                                    ToastUtils.showShort(ReadBookActivity.this,"已经是最后一页了");
                                    return false;
                                }
                                //绘制图形在nextBitmap
                                mBookPageFactory.onDraw(mNextPageCanvas);
                            }
                            mBookPageView.setBitmaps(mCurPageBitmap, mNextPageBitmap);
                        }

                    }
                    editor.putInt(bookPath + "begin", begin).commit();
                    ret = mBookPageView.doTouchEvent(event);
                    return ret;
                }
                return false;
            }
        });
        initPopupWindow();
        begin = sp.getInt(bookPath + "begin",0);
        LogUtils.d("begin + " + begin);
        tv_textsize.setText(size + "");
    }

    private void initData() {
        //提取记录子啊sharePreferences的各种状态
        sp = getSharedPreferences("config", MODE_PRIVATE);
        editor = sp.edit();
        //读取配置文件中字体大小
        size = sp.getInt("size", defaultSize);


        LogUtils.d("size" + size);
        light = sp.getInt("light", 5);
    }


    //初始化popupWindow
    private void initPopupWindow() {
        //顶部弹出框
        toolpop = this.getLayoutInflater().inflate(R.layout.pop_toolmenu, null);
        pop_toolmenu = new PopupWindow(toolpop, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置动画
        pop_toolmenu.setAnimationStyle(R.style.AnimationPreview);

        //更多弹出框
        morepop = this.getLayoutInflater().inflate(R.layout.pop_more,null);
        morePopWindow = new ToPopwindow(morepop,ReadBookActivity.this.getWindowManager().getDefaultDisplay().getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);

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
        ButterKnife.bind(this, textpop);

        //亮度弹出框
        lightpop = this.getLayoutInflater().inflate(R.layout.pop_light, null);
        pop_light = new PopupWindow(lightpop, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        pop_light.setAnimationStyle(R.style.anim_menu_bottombar);
        //设置这两个方法实现点击外面取消弹出框
        pop_light.setFocusable(true); // 这个很重要
        // 实例化一个ColorDrawable颜色为半透明
        pop_light.setBackgroundDrawable(new ColorDrawable(0x00000000));
        //ButterKnife.bind(this, lightpop);


        iv_bookhome = (ImageView) toolpop.findViewById(R.id.iv_bookhome);
        bookmore = (ImageView) toolpop.findViewById(R.id.iv_bookmore);
        iv_bookmenu = (ImageView) bottompop.findViewById(R.id.iv_bookmenu);
        iv_booksize = (ImageView) bottompop.findViewById(R.id.iv_booksize);
        iv_booklight = (ImageView) bottompop.findViewById(R.id.iv_booklight);
        iv_bookmark = (ImageView) bottompop.findViewById(R.id.iv_bookmark);


        iv_bookhome.setOnClickListener(this);
        bookmore.setOnClickListener(this);
        iv_bookmenu.setOnClickListener(this);
        iv_booksize.setOnClickListener(this);
        iv_booklight.setOnClickListener(this);
        iv_bookmark.setOnClickListener(this);
    }

    //记录配置文件中字体大小
    public void setSize() {

    }

    private void setLight(){
        light = lightSeekBar.getProgress();
        editor.putInt("light", light);
        editor.commit();
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
            //书签按钮
            case R.id.iv_bookmark:
                SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm ss");
                String time = sf.format(new Date());
                LogUtils.d("加入书签获的首行" + word);
                BookMark bookMark = bookMarkDao.getBookMark(begin);
                if (bookMark == null) {
                    bookMark = new BookMark(bookPath, begin, word, time);
                    bookMarkDao.add(bookMark);
                    showTextToast("书签添加成功");
                } else {
                    showTextToast("已经添加过了");
                }

                break;
            //返回键
            case R.id.iv_bookhome:
                backPress();
                break;

            //目录按钮
            case R.id.iv_bookmenu:
                startActivity(BookmenuActivity.getCallingIntent(ReadBookActivity.this,bookPath));
                break;
            //亮度按钮
            case R.id.iv_booklight:
                popDismiss();
                pop_light.showAtLocation(mBookPageView, Gravity.BOTTOM, 0, 0);
                btn_black = (Button) lightpop.findViewById(R.id.btn_black);
                btn_light = (Button) lightpop.findViewById(R.id.btn_light);
                lightSeekBar = (SeekBar) lightpop.findViewById(R.id.sk_light);
                lightSeekBar.setProgress(light);

                btn_black.setOnClickListener(this);
                btn_light.setOnClickListener(this);
                lightSeekBar.setOnSeekBarChangeListener(this);
                break;
            //增强亮度
            case R.id.btn_light:
                mBookPageFactory.setM_textColor(Color.BLACK);
                //mBookPageFactory.setBgBitmap(BitmapFactory.decodeResource(this.getResources(), R.mipmap.brown_bg));
                mBookPageFactory.setM_backColor(m_backColor);
                mBookPageFactory.setM_mbBufBegin(begin);
                mBookPageFactory.setM_mbBufEnd(begin);
                postInvalidateUI();
                break;
            //黑夜模式
            case R.id.btn_black:
                mBookPageFactory.setM_textColor(Color.rgb(128, 128, 128));
                //mBookPageFactory.setBgBitmap(BitmapFactory.decodeResource(this.getResources(), R.mipmap.black_bg));
                mBookPageFactory.setM_backColor(m_backColor_black);
                mBookPageFactory.setM_mbBufBegin(begin);
                mBookPageFactory.setM_mbBufEnd(begin);
                postInvalidateUI();
                break;
            //更多按钮
            case R.id.iv_bookmore:
                morePopWindow.showPopupWindow(mBookPageView);
                to_layout = (LinearLayout) morepop.findViewById(R.id.to_layout);
//                dialog_tv_progress = (TextView) morepop.findViewById(R.id.dialog_tv_progress);
//                dialog_seekbar = (SeekBar) morepop.findViewById(R.id.dialog_seekbar);
//                btn_ok = morepop
                to_layout.setOnClickListener(this);
                break;
            //跳转进度
            case R.id.to_layout:
                showDialog();
                morePopWindow.dismiss();
                break;
            case R.id.btn_ok:
                begin = (mBookPageFactory.getM_mbBufLen() * dialog_progess) / 100;
                editor.putInt(bookPath + "begin", begin).commit();
                mBookPageFactory.setM_mbBufBegin(begin);
                mBookPageFactory.setM_mbBufEnd(begin);
                try {
                    if (dialog_progess == 100) {
                        mBookPageFactory.prePage();
                        mBookPageFactory.getM_mbBufBegin();
                        begin = mBookPageFactory.getM_mbBufEnd();
                        mBookPageFactory.setM_mbBufBegin(begin);
                        mBookPageFactory.setM_mbBufBegin(begin);
                    }
                } catch (IOException e) {
                    Log.e(TAG, "onProgressChanged seekBar4-> IOException error", e);
                }
                postInvalidateUI();
                dialog.dismiss();
                break;
            case R.id.btn_cancel:
                dialog.dismiss();
                break;

        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        switch (seekBar.getId()){
            //亮度进度条
            case R.id.sk_light:
                light = seekBar.getProgress();
                setLight();
                lp.screenBrightness = light / 10.0f < 0.01f ? 0.01f : light / 10.0f;
                getWindow().setAttributes(lp);
                break;
            case R.id.dialog_seekbar:
                 dialog_progess = dialog_seekbar.getProgress();
                dialog_tv_progress.setText(dialog_progess + "%");
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    //减小字体
    @OnClick(R.id.btn_smalltext)
    public void onsmalltextbtn() {
        size -= 5;
        if (size < 10) {
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

    //增大字体
    @OnClick(R.id.btn_bigtext)
    public void onBigTextBtn() {
        size += 5;
        if (size > 50) {
            showTextToast("已到达最大字号");
            size = 50;
        }
        tv_textsize.setText(size + "");
        editor.putInt("size", size);
        editor.commit();
        mBookPageFactory.setM_fontSize(size);
        mBookPageFactory.setM_mbBufBegin(begin);
        mBookPageFactory.setM_mbBufEnd(begin);
        postInvalidateUI();
    }

    //白天模式
//    @OnClick(R.id.btn_light)
//    public void onLightBtnclick() {
//        mBookPageFactory.setM_textColor(Color.BLACK);
//        mBookPageFactory.setBgBitmap(BitmapFactory.decodeResource(this.getResources(), R.mipmap.brown_bg));
//        mBookPageFactory.setM_mbBufBegin(begin);
//        mBookPageFactory.setM_mbBufEnd(begin);
//        postInvalidateUI();
//    }
//
//    //黑夜模式
//    @OnClick(R.id.btn_black)
//    public void onBlackBtnClick() {
//        mBookPageFactory.setM_textColor(Color.rgb(128, 128, 128));
//        mBookPageFactory.setBgBitmap(BitmapFactory.decodeResource(this.getResources(), R.mipmap.black_bg));
//        mBookPageFactory.setM_mbBufBegin(begin);
//        mBookPageFactory.setM_mbBufEnd(begin);
//        postInvalidateUI();
//    }

    public void showDialog(){
        RelativeLayout layout = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.dialog_progress,null);
        dialog = new AlertDialog.Builder(ReadBookActivity.this).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);

        dialog_tv_progress = (TextView) layout.findViewById(R.id.dialog_tv_progress);
        dialog_seekbar = (SeekBar) layout.findViewById(R.id.dialog_seekbar);
        btn_ok = (Button) layout.findViewById(R.id.btn_ok);
        btn_cancel = (Button) layout.findViewById(R.id.btn_cancel);
        //获取开始值
        float fPercent = (float) (begin * 1.0 / mBookPageFactory.getM_mbBufLen());
        DecimalFormat df = new DecimalFormat("#0");
        String strPercent = df.format(fPercent * 100) + "%";

        dialog_tv_progress.setText(strPercent);
        dialog_seekbar.setProgress(Integer.parseInt(df.format(fPercent * 100)));
        dialog_seekbar.setOnSeekBarChangeListener(this);
        btn_ok.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
    }

    //重新绘制书本
    private void postInvalidateUI() {
        mBookPageView.abortAnimation();
        mBookPageFactory.onDraw(mCurPageCanvas);
        try {
            mBookPageFactory.currentPage();
            begin = mBookPageFactory.getM_mbBufBegin();// 获取当前阅读位置
            word = mBookPageFactory.getFirstLineText();// 获取当前阅读位置的首行文字
            LogUtils.d("重绘" + word);
        } catch (IOException e1) {
            Log.e(TAG, "postInvalidateUI->IOException error", e1);
        }

        mBookPageFactory.onDraw(mNextPageCanvas);

        mBookPageView.setBitmaps(mCurPageBitmap, mNextPageBitmap);
        mBookPageView.postInvalidate();
    }

    //回退
    public void backPress() {
        super.onBackPressed();
        ReadBookActivity.this.finish();
    }

    public static Intent getCallingIntent(Context context, Book book) {
        Intent intent = new Intent(context, ReadBookActivity.class);
        intent.putExtra(EXTRA_READ_BOOK, (Serializable) book);
        return intent;
    }

    //展示Toast
    private void showTextToast(String msg) {
        if (toast == null) {
            toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }

    @Override
    public void onPause() {
        super.onPause();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
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
}
