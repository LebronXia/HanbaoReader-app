package com.example.riane.hanbaoreader_app.ui.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.riane.hanbaoreader_app.R;
import com.example.riane.hanbaoreader_app.app.BaseFragment;
import com.example.riane.hanbaoreader_app.config.Constant;
import com.example.riane.hanbaoreader_app.modle.Book;
import com.example.riane.hanbaoreader_app.modle.BookMark;
import com.example.riane.hanbaoreader_app.modle.BookMenu;
import com.example.riane.hanbaoreader_app.ui.activity.ReadBookActivity;
import com.example.riane.hanbaoreader_app.ui.adapter.BookMarkAdapter;
import com.example.riane.hanbaoreader_app.ui.adapter.BookMenuAdapter;
import com.example.riane.hanbaoreader_app.util.LogUtils;
import com.example.riane.hanbaoreader_app.widget.RecycleViewDivider;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.RandomAccess;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Riane on 2016/4/30.
 */
public class BookMenuFragment extends BaseFragment {
    private final static int DIALOG_BOOKMENU = 1;
    @Bind(R.id.bookmark_recycle_view)
    RecyclerView mBookmarkRecycleView;
    @Bind(R.id.empty_view)
    RelativeLayout mEmptyView;
    private ProgressDialog dialog;
    private View view;
    private String bookPath;
    private JSONArray mJSONArray;
    private List<BookMenu> mBookMenus;
    private BookMenuAdapter mBookMenuAdapter;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case DIALOG_BOOKMENU:
                    dialog.dismiss();
                    if (mJSONArray != null){
                        LogUtils.d("你好" + mJSONArray.toString());
                        mBookmarkRecycleView.setVisibility(View.VISIBLE);
                        mEmptyView.setVisibility(View.GONE);
                        Gson gson = new Gson();
                        mBookMenus = gson.fromJson(mJSONArray.toString(), new TypeToken<List<BookMenu>>(){}
                                .getType());
                        mBookMenuAdapter = new BookMenuAdapter(getActivity(),mBookMenus);
                        mBookmarkRecycleView.setAdapter(mBookMenuAdapter);

                        mBookMenuAdapter.setOnItemClickListener(new BookMarkAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                BookMenu bookMenu = mBookMenus.get(position);
                                Book book = new Book();
                                book.setFilePath(bookPath);
                                book.setBegin(mBookMenus.get(position).getPos());
                                startActivity(ReadBookActivity.getCallingIntent(getActivity(), book));
                            }

                            @Override
                            public void onItemLongClick(View view, int position) {
                                //showPopFormBottom(view, position);
                            }
                        });
                        //LogUtils.d(mBookMenus.get(0).getTitle());
                    } else {
                        mBookmarkRecycleView.setVisibility(View.GONE);
                        mEmptyView.setVisibility(View.VISIBLE);
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_bookmenu, container, false);

        ButterKnife.bind(this, view);
        initData();
        initRecycleView();
        return view;
    }

    private void initData() {

        bookPath = getArguments().getString(Constant.BUNDLE_MENU_PATH);
        dialog = ProgressDialog.show(getActivity(), "温馨提示", "正在读取目录", true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                analysisChapterList();

                Message message = new Message();
                message.what = DIALOG_BOOKMENU;
                mHandler.sendMessage(message);
            }
        }).start();
       //analysisChapterList();

    }

    private void initRecycleView() {
        //创建默认的线性LayoutManager
        mBookmarkRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        //如果确定每个item的高度是固定的，设置这个选项可以提高性能
        mBookmarkRecycleView.setHasFixedSize(true);
        //加下划线
        mBookmarkRecycleView.addItemDecoration(new RecycleViewDivider(getContext(), LinearLayoutManager.VERTICAL));




        // 设置item动画
        mBookmarkRecycleView.setItemAnimator(new DefaultItemAnimator());
    }

    public void analysisChapterList(){
        JSONArray sing = new JSONArray();
        String regexl = "(\\t|\\x0B){0,6}[第]([零一二三四五六七八九十百佰千仟万0-9]{1,9})[章节回卷集部篇](.{0,30})";
        Pattern p = Pattern.compile(regexl);
        String s = null;
        Matcher m = null;
        File file1 = new File(bookPath);
        long m_mbBufLen = file1.length();
        RandomAccessFile in;
        try {
            in = new RandomAccessFile(file1,"r");
            //高效获取源文件
            MappedByteBuffer m_mbBuf = in.getChannel().map(
                    FileChannel.MapMode.READ_ONLY,0,m_mbBufLen
            );
            in.close();

            int start = 0; //当街章节开始位置
            int start1 = -10000; //上章节开始位置
            int count = 0; //统计每章节字节次数
            int minNum = 10000; //保存最小章节字节数作为基本字节数
            while (start < m_mbBufLen){
                byte[] buf = new byte[500];
                int i ;
                for (i = 0; i < 500 && start+i < m_mbBufLen;){
                    if (m_mbBuf.get(start + i) == 13
                            && m_mbBuf.get(start + i + 1) == 10){
                        i += 2;
                        break;
                    }
                    buf[i] = m_mbBuf.get(start + i);
                    i++;
                }
                if (i < 55){//判断文本编码
                    s = new String(buf, "gbk").trim();
                    //LogUtils.d(s);
                    m = p.matcher(s);
                    while (m.find()){
                        JSONObject temp = new JSONObject();
                        temp.put("title", m.group());
                        temp.put("pos", start);

                        sing.put(temp);
                        if (count < 10 && start - start1 > 500){
                            if (minNum > start - start1)
                                minNum = start - start1;
                            start1 = start;
                        }else
                            start += minNum - 1000;
                        count++;
                    }
                }
                start = start + i;
            }
            m_mbBuf.clear();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mJSONArray = sing;
    }

    public static BookMenuFragment newInstance(String bookPath) {
        //通过Bundle保存数据
        Bundle args = new Bundle();
        // args.putString(Constant.BUNDLE_PLACE, bookPath);
        args.putString(Constant.BUNDLE_MENU_PATH, bookPath);
        BookMenuFragment fragment = new BookMenuFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
