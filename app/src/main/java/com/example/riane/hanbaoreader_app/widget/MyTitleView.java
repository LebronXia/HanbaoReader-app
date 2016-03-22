package com.example.riane.hanbaoreader_app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.riane.hanbaoreader_app.R;

import butterknife.ButterKnife;

/**
 * Created by Xiamu on 2016/3/10.
 */
public class MyTitleView extends RelativeLayout {
    LinearLayout ll_firstTab;
    LinearLayout ll_secondTab;
    RelativeLayout btn_hamburger;
    RelativeLayout btn_search;
    View firstTabview;
    View secondTabview;

    public MyTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view =  LayoutInflater.from(context).inflate(R.layout.titlebar,null);
        ButterKnife.bind(view);
        this.addView(view);
        ll_firstTab = (LinearLayout)view.findViewById(R.id.ll_firstTab);
        ll_secondTab = (LinearLayout) view.findViewById(R.id.ll_secondTab);
        btn_hamburger = (RelativeLayout) view.findViewById(R.id.btn_hamburger);
        btn_search = (RelativeLayout) view.findViewById(R.id.btn_search);


    }

    public void setFirsttabListener(OnClickListener l){
        ll_firstTab.setOnClickListener(l);
    }

    public void setSecondtabListener(OnClickListener l){
        ll_secondTab.setOnClickListener(l);
    }

    public void setBtn_hamburger(OnClickListener l){
        btn_hamburger.setOnClickListener(l);
    }

    public void setBtn_search(OnClickListener l){
        btn_search.setOnClickListener(l);
    }

}
