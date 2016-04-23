package com.example.riane.hanbaoreader_app.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.riane.hanbaoreader_app.R;
import com.example.riane.hanbaoreader_app.config.Constant;
import com.example.riane.hanbaoreader_app.modle.Book;
import com.example.riane.hanbaoreader_app.modle.entity.BookVO;

import java.util.List;

/**
 * Created by Riane on 2016/4/20.
 */
public class BookStroeListAdapter extends RecyclerView.Adapter<BookStroeListAdapter.BookStoreViewHolder>{

    private List<BookVO> mDatas;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;
    private Context mContext;

    public BookStroeListAdapter(Context context, List<BookVO> datas) {
        super();
        mInflater = LayoutInflater.from(context);
        mDatas = datas;
        this.mContext = context;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public BookStoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_bookstore_list,parent,false);
        BookStoreViewHolder viewHolder = new BookStoreViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final BookStoreViewHolder holder, int position) {
       // holder.mImg.setImageResource(R.mipmap.cover_txt);
        Glide.with(mContext).load(Constant.BASEURL + mDatas.get(position).getPicture())
                .placeholder(R.mipmap.cover_txt)
                .crossFade()
                .into(holder.mImg);
        holder.mBookTitle.setText(mDatas.get(position).getName());
        holder.mBookInfo.setText("作者 " + mDatas.get(position).getAuthor() + "\r\n"
                + "出版社 " + mDatas.get(position).getPublish());



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

    public class BookStoreViewHolder extends RecyclerView.ViewHolder {
        //TextView mTxt;
        ImageView mImg;
        TextView mBookTitle;
        TextView mBookInfo;
        public BookStoreViewHolder(View itemView) {
            super(itemView);
            mImg = (ImageView) itemView.findViewById(R.id.iv_book);
            mBookTitle = (TextView) itemView.findViewById(R.id.item_list_bookTitle);
            mBookInfo = (TextView) itemView.findViewById(R.id.item_list_bookinfo);
        }

    }
}
