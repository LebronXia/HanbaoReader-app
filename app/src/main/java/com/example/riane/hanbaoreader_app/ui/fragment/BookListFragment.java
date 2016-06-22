package com.example.riane.hanbaoreader_app.ui.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.riane.hanbaoreader_app.R;
import com.example.riane.hanbaoreader_app.config.Constant;
import com.example.riane.hanbaoreader_app.modle.Book;
import com.example.riane.hanbaoreader_app.modle.entity.BookVO;
import com.example.riane.hanbaoreader_app.modle.entity.UserVO;
import com.example.riane.hanbaoreader_app.presenter.impl.BookStorePresent;
import com.example.riane.hanbaoreader_app.ui.activity.BookViewActivity;
import com.example.riane.hanbaoreader_app.ui.adapter.BookCaseAdapter;
import com.example.riane.hanbaoreader_app.ui.adapter.BookStroeListAdapter;
import com.example.riane.hanbaoreader_app.util.FileDownloadThread;
import com.example.riane.hanbaoreader_app.util.LogUtils;
import com.example.riane.hanbaoreader_app.util.ToastUtils;
import com.example.riane.hanbaoreader_app.view.impl.BookStoreView;
import com.yalantis.phoenix.PullToRefreshView;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Riane on 2016/4/20.
 */
public class BookListFragment extends Fragment implements BookStoreView {
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.progre_wheel)
    ContentLoadingProgressBar progreWheel;
    @Bind(R.id.rl_booklist)
    RelativeLayout rlBookstore;
    @Bind(R.id.pull_to_refresh)
    PullToRefreshView mPullToRefreshView;
    @Bind(R.id.tv_load_empty)
    TextView mTvLoadEmpty;
    @Bind(R.id.tv_load_error)
    TextView mTvLoadError;
    private PopupWindow pop_downloadbook;
    private Button btn_download;
    private Button btn_close;
    private View view;
    private BookStroeListAdapter mBookStroeListAdapter;
    private BookStorePresent mBookStorePresent;
    private boolean withRefreshView = true;  //是否刷新
    private List<BookVO> bookList;
    private String tag;
    private int position;
    private static final String TAG = BookListFragment.class.getSimpleName();

    //使用Handler更新UI界面
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            //下载进度
            float temp = msg.getData().getInt("size")
                    /msg.getData().getInt("fileSize");
            int progress = (int) (temp * 20);
            if (progress == 100){
                ToastUtils.showShort(getActivity(),"下载完成");
            }
            btn_download.setText("下载进度" + progress + "%");
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        mParentView = View.inflate(getContext(), R.layout.fragment_bookstore, false);
//        mViewPager = (ViewPager) mParentView.findViewById(R.id.book_viewpager);
//        mSmartTabLayout = (SmartTabLayout) getActivity().findViewById(R.id.tab_layout);
        view = inflater.inflate(R.layout.fragmnet_bookstore_list,container,false);
        ButterKnife.bind(this, view);
        initData();
        initView();
        return view;
    }

    private void initData() {
        mBookStorePresent = new BookStorePresent(this);
        position = getArguments().getInt(BookStoreFragment.id_pos);
        tag = getArguments().getString(BookStoreFragment.id_category);
    }

    private void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //如果确定每个item的高度是固定的，设置这个选项可以提高性能
        recyclerView.setHasFixedSize(true);

        progreWheel.setVisibility(View.VISIBLE);
        mBookStorePresent.loadData(tag);
//        //制造延时
//        new Handler().postDelayed(new Runnable()  {
//            @Override
//            public void run() {
//                mBookStorePresent.loadData(tag);
//            }
//        },5000);
        if (withRefreshView){
            mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener(){
                @Override
                public void onRefresh() {
                    mBookStorePresent.loadData(tag);
                }
            });
        }
    }

    private void doDownload(int position){
        //获取SD卡路径
        String path = Environment.getExternalStorageDirectory()
                + "/bookDownload/";

        File file =new File(path);

        if (!file.exists()){
            file.mkdir();
        }
          String downloadUrl = Constant.BASEURL + "books/downloadText?name=" + bookList.get(position).getBookResource();
       // String[] fileNames = bookList.get(position).getBookResource().split("/");
        //String downloadUrl = "http://gdown.baidu.com/data/wisegame/91319a5a1dfae322/baidu_16785426.apk";

        String fileName =  bookList.get(position).getBookResource();

        int threadNum = 5;
        String filepath = path + fileName;
        //LogUtils.d(TAG + filepath);

        downloadTask task = new downloadTask(downloadUrl,threadNum,filepath);
        task.start();
    }

    //多线程文件下载
    class downloadTask extends Thread {
        private String downloadUrl;// 下载链接地址
        private int threadNum;// 开启的线程数
        private String filePath;// 保存文件路径地址
        private int blockSize;// 每一个线程的下载量

        public downloadTask(String downloadUrl, int threadNum, String fileptah) {
            this.downloadUrl = downloadUrl;
            this.threadNum = threadNum;
            this.filePath = fileptah;
        }

        @Override
        public void run() {

            FileDownloadThread[] threads = new FileDownloadThread[threadNum];
            try {
                URL url = new URL(downloadUrl);
                Log.d(TAG, "download file http path:" + downloadUrl);
                URLConnection conn = url.openConnection();
                // 读取下载文件总大小
                int fileSize = conn.getContentLength();
                if (fileSize <= 0) {
                    System.out.println("读取文件失败");
                    return;
                }
                // 设置ProgressBar最大的长度为文件Size
                //mProgressbar.setMax(fileSize);

                // 计算每条线程下载的数据长度
                blockSize = (fileSize % threadNum) == 0 ? fileSize / threadNum
                        : fileSize / threadNum + 1;

                Log.d(TAG, "fileSize:" + fileSize + "  blockSize:");

                File file = new File(filePath);
                for (int i = 0; i < threads.length; i++) {
                    // 启动线程，分别下载每个线程需要下载的部分
                    threads[i] = new FileDownloadThread(url, file, blockSize,
                            (i + 1));
                    threads[i].setName("Thread:" + i);
                    threads[i].start();
                }

                boolean isfinished = false;
                int downloadedAllSize = 0;
                while (!isfinished) {
                    isfinished = true;
                    // 当前所有线程下载总量
                    downloadedAllSize = 0;
                    for (int i = 0; i < threads.length; i++) {
                        downloadedAllSize += threads[i].getDownloadLength();
                        if (!threads[i].isCompleted()) {
                            isfinished = false;
                        }
                    }
                    // 通知handler去更新视图组件
                    Message msg = new Message();
                    msg.getData().putInt("size", downloadedAllSize);
                    msg.getData().putInt("fileSize",fileSize);
                    mHandler.sendMessage(msg);
                    // Log.d(TAG, "current downloadSize:" + downloadedAllSize);
                    Thread.sleep(1000);// 休息1秒后再读取下载进度
                }
                Log.d(TAG, " all of downloadSize:" + downloadedAllSize);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void showData(final List<BookVO> bookVOs) {
        //LogUtils.d("user的数据" + bookVOs.get(0).getName());
        if (bookVOs != null){
            bookList = bookVOs;
        }
       ToastUtils.showShort(getActivity(), "user的路劲" + bookVOs.get(0).getBookResource());
        mBookStroeListAdapter = new BookStroeListAdapter(getActivity(),bookVOs);
        recyclerView.setAdapter(mBookStroeListAdapter);

        mBookStroeListAdapter.setOnItemClickListener(new BookStroeListAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(View view, int position) {
                //LogUtils.d("单击如数");
                startActivity(BookViewActivity.getCallingIntent(getActivity(), bookVOs.get(position)));
            }

            @Override
            public void onItemLongClick(View view, int position) {
                showDialog(position);
            }
        });

        // 设置item动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    private void showDialog(final int position) {
        final String items[] = {"下载图书", "取消"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(bookList.get(position).getName());
        //设置列表显示
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //dialog.dismiss();
                if (items[which].equals("下载图书")) {
                    showPopWindow(position);

                } else if (items[which].equals("取消")){
                    //dialog.dismiss();
                }
            }
        });
        builder.create().show();
    }

    public void showPopWindow(final int position){
        View downloadbookPop = getActivity().getLayoutInflater().inflate(R.layout.pop_downloadbook,null);
        pop_downloadbook = new PopupWindow(downloadbookPop, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        pop_downloadbook.setAnimationStyle(R.style.anim_menu_bottombar);
        btn_download = (Button) downloadbookPop.findViewById(R.id.btn_download_book);
        btn_close = (Button) downloadbookPop.findViewById(R.id.btn_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop_downloadbook.dismiss();
            }
        });
        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doDownload(position);
            }
        });
        pop_downloadbook.showAtLocation(rlBookstore, Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void showProgress() {
        progreWheel.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progreWheel.setVisibility(View.GONE);
        mPullToRefreshView.setRefreshing(false);
    }

    @Override
    public void showLoadEmpty() {
        mTvLoadEmpty.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadEmpty() {
        mTvLoadEmpty.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        mBookStorePresent.detachView();
        super.onDestroyView();
        ButterKnife.unbind(this);

    }

}
