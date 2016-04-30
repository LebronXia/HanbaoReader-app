package com.example.riane.hanbaoreader_app.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.riane.hanbaoreader_app.R;
import com.example.riane.hanbaoreader_app.modle.BookMark;
import com.example.riane.hanbaoreader_app.modle.BookMenu;

import java.util.List;

/**
 * Created by Riane on 2016/4/30.
 */
public class BookMenuAdapter extends RecyclerView.Adapter<BookMenuAdapter.BookMenuViewHold>{

    private List<BookMenu> mBookMenus;
    private LayoutInflater mInflater;
    private BookMarkAdapter.OnItemClickListener mOnItemClickListener;

    public BookMenuAdapter(Context context, List<BookMenu> mBookMenus){
        this.mBookMenus = mBookMenus;
        mInflater = LayoutInflater.from(context);
    }
    @Override
    public BookMenuViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_bookmenu,parent,false);
        BookMenuViewHold menusViewHold = new BookMenuViewHold(view);
        return menusViewHold;
    }

    @Override
    public void onBindViewHolder(final BookMenuViewHold holder, int position) {
        holder.bookMenuTitle.setText(mBookMenus.get(position).getTitle());

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
    public void setOnItemClickListener(BookMarkAdapter.OnItemClickListener onItemClickListener){
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return mBookMenus.size();
    }

    public class BookMenuViewHold extends RecyclerView.ViewHolder{
        TextView bookMenuTitle;
        public BookMenuViewHold(View itemView) {
            super(itemView);
            bookMenuTitle = (TextView) itemView.findViewById(R.id.tv_bookmenutitle);
        }
    }
}
