package com.example.riane.hanbaoreader_app.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Gallery;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.example.riane.hanbaoreader_app.R;
import com.example.riane.hanbaoreader_app.ui.activity.ReadBookActivity;

/**
 * Created by Riane on 2016/4/13.
 */
public class ToPopwindow extends PopupWindow{
    private View conentView;

    public ToPopwindow(View view ,int weight, int height) {
//        LayoutInflater inflater = (LayoutInflater) context
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        conentView = inflater.inflate(R.layout.pop_more, null);
//        int h = context.getWindowManager().getDefaultDisplay().getHeight();
//        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        int w = weight;
        conentView = view;
        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(w / 2 + 50);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        // mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimationPreview);
//        LinearLayout tokLayout = (LinearLayout) conentView
//                .findViewById(R.id.to_layout);
//        Gallery gl_background = (Gallery) conentView
//                .findViewById(R.id.gl_background);
//        tokLayout.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//               //ReadBookActivity.showDialog();
//                ToPopwindow.this.dismiss();
//            }
//        });

//        teamMemberLayout.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                ToPopwindow.this.dismiss();
//            }
//        });
    }

    /**
     * 显示popupWindow
     *
     * @param parent
     */
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
            //this.showAsDropDown(parent, parent.getLayoutParams().width / 2, 18);
            this.showAtLocation(parent,Gravity.TOP,140,100);
        } else {
            this.dismiss();
        }
    }
}
