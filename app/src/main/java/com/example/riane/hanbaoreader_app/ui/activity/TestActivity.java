package com.example.riane.hanbaoreader_app.ui.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.riane.hanbaoreader_app.R;
import com.example.riane.hanbaoreader_app.app.BaseActivity;
import com.example.riane.hanbaoreader_app.util.LogUtils;
import com.example.riane.hanbaoreader_app.widget.FlowLayout;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Riane on 2016/4/14.
 */
public class TestActivity extends BaseActivity{

    private TestActivity testActivity = TestActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);

        FlowLayout flowLayout = (FlowLayout) findViewById(R.id.fl_text);
        String[] res = new String[]{"玄幻","言情","武侠","科幻","历史","科学"};
        List<String> list = Arrays.asList(res);
       // FlowLayout flowLayout = new FlowLayout(testActivity);
        flowLayout.setHorizontalSpacing(20);
        flowLayout.setVerticalSpacing(20);
        for (String re : list){
            LogUtils.d("分类" + re);
            Button classButton = new Button(testActivity);
            classButton.setText(re);
            classButton.setWidth(30);
            flowLayout.addView(classButton);
        }
        LogUtils.d("flowLayout数量"+ flowLayout.getChildCount());
        View view = LayoutInflater.from(testActivity).inflate(R.layout.layout_dialog_classify,null);
        //RelativeLayout classlayout = (RelativeLayout) view.findViewById(R.id.rl_classlist);
        //relativeLayout.addView(flowLayout);
    }
}
