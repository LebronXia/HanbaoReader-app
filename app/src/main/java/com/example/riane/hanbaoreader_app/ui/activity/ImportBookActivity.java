package com.example.riane.hanbaoreader_app.ui.activity;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.riane.hanbaoreader_app.R;
import com.example.riane.hanbaoreader_app.app.BaseActivity;
import com.example.riane.hanbaoreader_app.ui.fragment.DirectoryFragment;
import com.example.riane.hanbaoreader_app.util.FileUtils;
import com.example.riane.hanbaoreader_app.view.impl.ImportBookView;
import com.mingle.widget.ShapeLoadingDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Xiamu on 2016/3/26.
 */
public class ImportBookActivity extends BaseActivity implements ImportBookView,DirectoryFragment.FileClickListener{
    public static final String STATE_START_PATH = "state_start_path";  //开始路径
    private static final String STATE_CURRENT_PATH = "state_current_path";  //当前路径
    private static final String ARG_DIRECTORIES_FIFLTER = "arg_directories_fiflter";  //允许使用过滤器
    private static final String ARG_FILE_FILTER = "arg_file_filter";  //过滤器
    public static final String ARG_SHOW_HIDDEN = "arg_show_hidden";  //显示隐藏文件和文件夹的选择

    @Bind(R.id.id_toolbar)
    Toolbar mToolbar;
    @Bind(R.id.tv_path)
    TextView mPathTextView;

    private ShapeLoadingDialog mShapeLoadingDialog;
    private String mStartPath = Environment.getExternalStorageDirectory().getAbsolutePath();//获取外部存储目录即 SDCard
    private String mCurrentPath = mStartPath;
    private String path = Environment.getExternalStorageDirectory()+"/";

    private Pattern mFileFilter;  //过滤器
    private boolean mDirectoriesFilter;  //允许使用过滤器
    private boolean mShownHidden;  //显示隐藏文件和文件夹的选择
    private ArrayList<String> mSearchitems;  //搜索结果

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    mShapeLoadingDialog.show();
                    break;
                case  1:
                    mShapeLoadingDialog.dismiss();
                    if(mSearchitems != null && mSearchitems.size() > 0){
                        startActivity(SearchBookActivity.getCallingIntent(ImportBookActivity.this,mSearchitems));
                    } else {
                        Toast.makeText(ImportBookActivity.this,"未匹配到文件！",Toast.LENGTH_LONG).show();
                    }
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import);
        ButterKnife.bind(this);
        initArguments();
        initViews();

        if (savedInstanceState != null){
            mStartPath = savedInstanceState.getString(STATE_START_PATH);
            mCurrentPath = savedInstanceState.getString(STATE_CURRENT_PATH);
        } else {
            initFragmnet();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mSearchitems = new ArrayList<String>();
    }

    public void initViews(){
        //清空默认title
        mToolbar.setTitle("");// 标题的文字需在setSupportActionBar之前，不然会无效
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ImportBookActivity.this.onBackPressed();
                finish();
            }
        });
        mShapeLoadingDialog = new ShapeLoadingDialog(this);
        mShapeLoadingDialog.setLoadingText("搜索中...");
        updatePathText();
    }

    public void initArguments(){
        if (getIntent().hasExtra(ARG_DIRECTORIES_FIFLTER)){
            mDirectoriesFilter = getIntent().getBooleanExtra(ARG_DIRECTORIES_FIFLTER,false);
        }
        if (getIntent().hasExtra(ARG_FILE_FILTER)){
            mFileFilter = (Pattern) getIntent().getSerializableExtra(ARG_FILE_FILTER);
        }
        if (getIntent().hasExtra(ARG_SHOW_HIDDEN)){
            mShownHidden = getIntent().getBooleanExtra(ARG_SHOW_HIDDEN,false);
        }

    }

    public void initFragmnet(){
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container,DirectoryFragment.getInstance(
                        mStartPath, mFileFilter, mDirectoriesFilter, mShownHidden))   //创建一个Fragment
                .commit();
    }

    //更新路劲
    public void updatePathText(){
        String title = mCurrentPath.isEmpty() ? "/" : mCurrentPath;
        //是该字符串所表示的字符序列的前缀
//        if (title.startsWith(mStartPath)){
//            //替换第一个给定的正则表达式的字符串相匹配的子串.
//            title = title.replaceFirst(mStartPath, "");
//        }
        //文字过长时 实现滚动
       // mPathTextView.setMovementMethod(new ScrollingMovementMethod());
        mPathTextView.setText(title);
    }

    //切换Fragment
    private void addFragmentToBackStack(String path){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container,DirectoryFragment.getInstance(
                        path,mFileFilter, mDirectoriesFilter, mShownHidden) )   //创建一个Fragment
                .addToBackStack(null)
                .commit();
    }

    @OnClick(R.id.tv_pre)
    public void setPreviousTextView(){
        onBackPressed();
    }

    @OnClick(R.id.toolbar_txt_btn)
    public void setSearchbook(){
        Message message = new Message();
        message.what = 0;
        mHandler.sendMessage(message);
        new SearchThread().start();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0){
            //弹出一个栈，回到前一个
            fm.popBackStack();
            mCurrentPath = FileUtils.cutLastSegmentOfPath(mCurrentPath);
            updatePathText();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STATE_CURRENT_PATH, mCurrentPath);
        outState.putString(STATE_START_PATH, mStartPath);
    }

    @Override
    public void onFileClicked(final File clickedFile) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                handleFileClicked(clickedFile);
            }
        }, 15);
    }

    private void handleFileClicked(final File clickedFile) {
        if (clickedFile.isDirectory()) {
            addFragmentToBackStack(clickedFile.getPath());
            mCurrentPath = clickedFile.getPath();
            updatePathText();
        } else {
            //setResultAndFinish(clickedFile.getPath());
        }
    }

    //扫描图书线程
    class SearchThread extends Thread{

        public SearchThread() {
            super();
        }
        @Override
        public void run() {
            super.run();
            File f = new File(mCurrentPath);
            getFiles(f);
            Message message = new Message();
            message.what = 1;
            mHandler.sendMessage(message);
        }
    }

    //递归遍历扫描文件夹
    public void getFiles(File filePath){
        //列出该文件夹路径下的文件
        File[] files = filePath.listFiles();
        if (files != null){
            for (int i = 0; i < files.length; i++){
                //判断是不是文件
                if (files[i].isDirectory()){
                    getFiles(files[i]);
                } else {
                    if (files[i].getName().endsWith(".txt")){
                        mSearchitems.add(files[i].getAbsolutePath());

                    }
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSearchitems = null;
    }

    @Override
    public void initToolbar() {

    }

    @Override
    public void initRecyclerView() {

    }

    @Override
    public void loadData() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
