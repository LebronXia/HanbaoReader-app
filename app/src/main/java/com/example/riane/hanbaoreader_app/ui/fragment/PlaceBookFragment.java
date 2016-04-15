package com.example.riane.hanbaoreader_app.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.riane.hanbaoreader_app.R;
import com.example.riane.hanbaoreader_app.app.BaseFragment;
import com.example.riane.hanbaoreader_app.config.Constant;

/**
 * Created by Riane on 2016/4/14.
 */
public class PlaceBookFragment extends BaseFragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frament_placebook,container,false);
        return view;
    }

    public static PlaceBookFragment newInstance(String bookPath){
        //通过Bundle保存数据
        Bundle args = new Bundle();
        args.putString(Constant.BUNDLE_PLACE, bookPath);
        PlaceBookFragment fragment = new PlaceBookFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
