package com.example.riane.hanbaoreader_app.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.riane.hanbaoreader_app.R;

/**
 * Created by Riane on 2016/4/11.
 */
public class BookMarkPopWinsow extends PopupWindow{
    public BookMarkPopWinsow(Context context) {
        super(context);
    }
    private Context mContext;

    private View view;

    private TextView tv_toactivity, tv_deletemark;


    public BookMarkPopWinsow(Context mContext, View.OnClickListener itemsOnClick) {

        view = LayoutInflater.from(mContext).inflate(R.layout.pop_bookmark_item, null);

        tv_toactivity = (TextView) view.findViewById(R.id.tv_toactivity);
        tv_deletemark = (TextView) view.findViewById(R.id.tv_deletemark);

        // 设置按钮监听
        tv_toactivity.setOnClickListener(itemsOnClick);
        tv_deletemark.setOnClickListener(itemsOnClick);

        // 设置外部可点击
        this.setOutsideTouchable(true);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        this.view.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = view.findViewById(R.id.pop_layout).getTop();

                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });


    /* 设置弹出窗口特征 */
        // 设置视图
        this.setContentView(this.view);
        // 设置弹出窗体的宽和高
        this.setHeight(RelativeLayout.LayoutParams.MATCH_PARENT);
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);

        // 设置弹出窗体可点击
        this.setFocusable(true);

        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置弹出窗体的背景
        this.setBackgroundDrawable(dw);

        // 设置弹出窗体显示时的动画，从底部向上弹出
        this.setAnimationStyle(R.style.anim_menu_bottombar);

    }
}
