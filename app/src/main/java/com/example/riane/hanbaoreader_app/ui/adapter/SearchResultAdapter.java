package com.example.riane.hanbaoreader_app.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.riane.hanbaoreader_app.R;
import com.example.riane.hanbaoreader_app.modle.FileItem;

import java.util.List;

import cn.refactor.library.SmoothCheckBox;

/**
 * Created by Xiamu on 2016/3/31.
 */
public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.SearchResultViewHolder >{

    private List<FileItem> mFileItems;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;

    public SearchResultAdapter(Context context, List<FileItem> fileItems) {
        super();
        mContext = context;
        mFileItems = fileItems;
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mOnItemClickListener = listener;
    }

    @Override
    public SearchResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_import_book,parent,false);
        return new SearchResultViewHolder(view,mOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(SearchResultViewHolder holder, int position) {
          final FileItem fileItem = mFileItems.get(position);
          holder.mFileImage.setImageDrawable(fileItem.getFileIcon());
          holder.mFileSubtitle.setText(fileItem.getFileSize());
          holder.mFileTitle.setText(fileItem.getFileName());
          holder.mCheckBox.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                    fileItem.setIsChecked(isChecked);
                }
            });
        if (fileItem.getFileName().endsWith(".txt")){
            if (fileItem.isImport()){
                holder.mCheckBox.setVisibility(View.GONE);
                holder.mImportText.setVisibility(View.VISIBLE);
            } else {
                holder.mCheckBox.setVisibility(View.VISIBLE);
                holder.mImportText.setVisibility(View.GONE);
                holder.mCheckBox.setChecked(fileItem.isChecked());
            }
        }
    }

    @Override
    public int getItemCount() {
        return mFileItems.size();
    }

    public class SearchResultViewHolder extends RecyclerView.ViewHolder{

        private ImageView mFileImage;
        private TextView mFileTitle;
        private TextView mFileSubtitle;
        private SmoothCheckBox mCheckBox;
        private TextView mImportText;

        public SearchResultViewHolder(View itemView,final OnItemClickListener clickListener) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClick(v, getAdapterPosition());
                }
            });

            mFileImage = (ImageView) itemView.findViewById(R.id.ig_item_file);
            mFileTitle = (TextView) itemView.findViewById(R.id.item_file_title);
            mFileSubtitle = (TextView) itemView.findViewById(R.id.item_file_subtitle);
            mCheckBox = (SmoothCheckBox) itemView.findViewById(R.id.scb_check);
            mImportText = (TextView) itemView.findViewById(R.id.tv_import);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }
}
