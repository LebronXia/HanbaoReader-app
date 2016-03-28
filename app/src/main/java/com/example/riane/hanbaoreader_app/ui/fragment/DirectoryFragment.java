package com.example.riane.hanbaoreader_app.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.riane.hanbaoreader_app.R;
import com.example.riane.hanbaoreader_app.app.BaseFragment;
import com.example.riane.hanbaoreader_app.ui.adapter.DirectoryAdapter;
import com.example.riane.hanbaoreader_app.util.FileUtils;

import java.io.File;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Xiamu on 2016/3/28.
 */
public class DirectoryFragment extends BaseFragment{

    private static final String ARG_FILE_PATH = "arg_file_path";
    private static final String ARG_FILE_FILTER = "arg_regexp_file_filter";
    private static final String ARG_DIRECTORIES_FILTER = "arg_directories_filter";
    private static final String ARG_SHOW_HIDDEN = "arg_show_hidden";

    private Pattern mFileFilter;
    private boolean mShowHidden;
    private boolean mDirectoryFilter;
    private String mPath;

    @Bind(R.id.directory_recycle_view)
    RecyclerView mDirectoryRecyclerView;
    @Bind(R.id.directory_empty_view)
    LinearLayout mEmptyView;

    private FileClickListener mFileClickListener;   //文件夹选择监听器
    private DirectoryAdapter mDirectoryAdapter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mFileClickListener = (FileClickListener)activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mFileClickListener = null;
    }

    public static DirectoryFragment getInstance(
            String path, @Nullable Pattern filter, boolean directoriesFilter, boolean showHidden){
            DirectoryFragment instance = new DirectoryFragment();

        Bundle args = new Bundle();
        args.putString(ARG_FILE_PATH, path);
        args.putSerializable(ARG_FILE_FILTER, filter);
        args.putBoolean(ARG_DIRECTORIES_FILTER, directoriesFilter);
        args.putBoolean(ARG_SHOW_HIDDEN, showHidden);
        instance.setArguments(args);

        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_import,container,false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initArgs();
        initFilesList();
    }

    //初始化初始值
    private void initArgs() {
        if (getArguments().getString(ARG_FILE_PATH) != null){
            mPath = getArguments().getString(ARG_FILE_PATH);
        }

        mDirectoryFilter = getArguments().getBoolean(ARG_DIRECTORIES_FILTER);
        mFileFilter = (Pattern) getArguments().getSerializable(ARG_FILE_FILTER);
        mShowHidden = getArguments().getBoolean(ARG_SHOW_HIDDEN, false);
    }

    private void initFilesList() {
        mDirectoryAdapter = new DirectoryAdapter(getActivity(),
                FileUtils.getFileListByDirPath(mPath, mFileFilter,mDirectoryFilter, mShowHidden));

        //点击选中Item
        mDirectoryAdapter.setOnItemClickListener(new DirectoryAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(View view, int position) {
                if (mFileClickListener != null){
                    mFileClickListener.onFileClicked(mDirectoryAdapter.getModel(position));
                }
            }
        });
        mDirectoryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mDirectoryRecyclerView.setAdapter(mDirectoryAdapter);
        //当没有文件的时候显示空
        mEmptyView.setVisibility(mDirectoryAdapter.getItemCount() > 0 ? View.GONE : View.VISIBLE);

    }

    public interface  FileClickListener{
        void onFileClicked(File clickedFile);
    }
}
