package com.example.riane.hanbaoreader_app.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Xiamu on 2016/3/23.
 */
public class BookCaseAdapter extends RecyclerView.Adapter<BookCaseAdapter.ViewHolder>{

    private List<Integer> mDatas;
    public BookCaseAdapter(Context context, List<Integer> datas){

    }
    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(View itemView) {
            super(itemView);
        }
        TextView mTxt;
        ImageView mImg;
    }
}
