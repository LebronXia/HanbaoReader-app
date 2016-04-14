package com.example.riane.hanbaoreader_app.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.riane.hanbaoreader_app.R;
import com.example.riane.hanbaoreader_app.modle.Book;
import com.example.riane.hanbaoreader_app.modle.BookMark;

import java.util.List;

/**
 * Created by Riane on 2016/4/10.
 */
public class BookMarkAdapter extends RecyclerView.Adapter<BookMarkAdapter.BookMarksViewHold>{

    private List<BookMark> bookMarks;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;

    public BookMarkAdapter(Context context, List<BookMark> bookMarks){
        this.bookMarks = bookMarks;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemCount() {
        return bookMarks.size();
    }

    @Override
    public BookMarksViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_book_mark,parent,false);
        BookMarksViewHold marksViewHold = new BookMarksViewHold(view);
        return marksViewHold;
    }

    @Override
    public void onBindViewHolder(final BookMarksViewHold holder, int position) {

        holder.bookMarkTitle.setText(bookMarks.get(position).getTextword());
        holder.bookMarkDate.setText(bookMarks.get(position).getTime());

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

    public class BookMarksViewHold extends RecyclerView.ViewHolder{
        TextView bookMarkTitle;
        TextView bookMarkDate;
        public BookMarksViewHold(View itemView) {
            super(itemView);
            bookMarkTitle = (TextView) itemView.findViewById(R.id.tv_mark_title);
            bookMarkDate = (TextView) itemView.findViewById(R.id.tv_mark_date);
        }
    }

}
