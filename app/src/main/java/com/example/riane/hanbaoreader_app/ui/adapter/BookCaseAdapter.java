package com.example.riane.hanbaoreader_app.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.riane.hanbaoreader_app.R;
import com.example.riane.hanbaoreader_app.modle.Book;
import com.example.riane.hanbaoreader_app.ui.fragment.BookCaseFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Xiamu on 2016/3/23.
 */
public class BookCaseAdapter extends RecyclerView.Adapter<BookCaseAdapter.MyViewHolder>{

    private List<Book> mDatas;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;

    public BookCaseAdapter(Context context, List<Book> datas){
        mInflater = LayoutInflater.from(context);
        mDatas = datas;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (BookCaseFragment.isCardVIew){
            view = mInflater.inflate(R.layout.item_book_case,parent,false);
        } else {
            view = mInflater.inflate(R.layout.item_book_listcase,parent,false);
        }

        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        if (BookCaseFragment.isCardVIew){
            holder.mImg.setImageResource(R.mipmap.cover_txt);
            holder.mTxt.setText(mDatas.get(position).getName());
            holder.mProgress.setText("已读" + mDatas.get(position).getProgress());
        } else {
            holder.mImg.setImageResource(R.mipmap.cover_txt);
            holder.mTxt.setText(mDatas.get(position).getName());
            holder.mProgress.setText("读书进度：" + mDatas.get(position).getProgress());
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm ss");
            String time = sf.format(mDatas.get(position).getLastReadTime());
            holder.mBookDate.setText("最后阅读：" + time);

        }



        //如果设置了回调，则设置点击事件
        if (mOnItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(v, pos);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemLongClick(v,pos);
                    return false;
                }
            });
        }
    }

    //设置点击事件
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public class MyViewHolder extends ViewHolder{
        TextView mTxt;
        ImageView mImg;
        TextView mProgress;
        TextView mBookDate;
        public MyViewHolder(View itemView) {
            super(itemView);
            mImg = (ImageView) itemView.findViewById(R.id.iv_book_picture);
            mTxt = (TextView) itemView.findViewById(R.id.tv_itemtext);
            mProgress = (TextView) itemView.findViewById(R.id.tv_progress);
            mBookDate = (TextView) itemView.findViewById(R.id.tv_readdate);
        }

    }
}
